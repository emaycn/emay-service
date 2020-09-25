package cn.emay.configuration.web;

import cn.emay.constant.web.ResourceEnum;
import cn.emay.constant.web.SystemType;
import cn.emay.constant.web.WebAuth;
import cn.emay.constant.web.WebToken;
import cn.emay.core.system.pojo.User;
import cn.emay.utils.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * 拦截没有权限的请求
 *
 * @author Frank
 */
@Component
public class AuthInterceptor extends HandlerInterceptorAdapter {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        try {
            // 不符合继承关系，通过
            if (!(handler instanceof HandlerMethod)) {
                return true;
            }
            Method method = ((HandlerMethod) handler).getMethod();
            WebAuth auth = method.getAnnotation(WebAuth.class);
            // 资源没有要求权限的，通过
            if (auth == null) {
                return true;
            }
            WebToken token = WebUtils.getWebToken();
            // 未登陆，拦截
            if (token == null) {
                WebUtils.logout();
                WebUtils.toNoLogin();
                return false;
            }
            User user = WebUtils.setAndGetCurrentUser();
            // 登陆状态异常，拦截
            if (user == null) {
                WebUtils.logout();
                WebUtils.toNoLogin();
                return false;
            }
            // 被删除或者被锁定，拦截
            if (user.getUserState() == User.STATE_OFF) {
                WebUtils.logout();
                WebUtils.toNoLogin();
                return false;
            }
            // 首次登陆没有修改密码，并且不是访问修改密码的拦截
            String uri = request.getRequestURI().replace(request.getContextPath(), "");
            if (user.getLastChangePasswordTime() == null && !"/p/changePassword".equals(uri)) {
                WebUtils.toNoChangePass();
                return false;
            }
            ResourceEnum[] codes = auth.value();
            // 不需要资源权限，仅需要登录权限的
            if (codes.length == 0) {
                boolean isHasLoginAuth = false;
                for (SystemType st : auth.system()) {
                    if (st.getType().equalsIgnoreCase(user.getUserFor())) {
                        isHasLoginAuth = true;
                        break;
                    }
                }
                if (isHasLoginAuth) {
                    // 用户所属系统允许访问
                    return fillRequest(user);
                } else {
                    // 用户所需系统不允许访问
                    WebUtils.logout();
                    WebUtils.toNoLogin();
                    return false;
                }
            }
            // 有资源权限的，通过
            for (ResourceEnum code : codes) {
                if (token.getResources().containsKey(code.getCode())) {
                    return fillRequest(user);
                }
            }
            // 无资源权限的，拦截
            WebUtils.toError("无权访问");
            return false;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    private boolean fillRequest(User user) {
        if (SystemType.CLIENT.getType().equals(user.getUserFor())) {
            WebUtils.setAndGetCurrentClient();
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {

    }

}
