package cn.emay.boot.base.web;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import cn.emay.boot.base.constant.ResourceEnum;
import cn.emay.boot.business.system.pojo.User;
import cn.emay.boot.utils.WebUtils;

/**
 * 拦截没有权限的请求
 * 
 * @author Frank
 *
 */
@Component
public class AuthInterceptor extends HandlerInterceptorAdapter {

	private Logger log = LoggerFactory.getLogger(getClass());

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		try {
			// 不符合继承关系，通过
			if (!(handler instanceof HandlerMethod)) {
				return true;
			}
			Method method = ((HandlerMethod) handler).getMethod();
			WebAuth auth = method.getAnnotation(WebAuth.class);
			// 资源没有要求权限的，通过---有注解即视为需要登录
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
			User user = WebUtils.getCurrentUser();
			// 登陆状态异常，拦截
			if (user == null) {
				WebUtils.logout();
				WebUtils.toNoLogin();
				return false;
			}
			// 被删除或者被锁定，拦截
			if (user == null || user.getUserState() == User.STATE_LOCKING) {
				WebUtils.logout();
				WebUtils.toNoLogin();
				return false;
			}
			ResourceEnum[] codes = auth.value();
			// 仅需要登录权限的，通过--有注解无Code即视为仅需要登录
			if (codes == null || codes.length == 0) {
				return true;
			}
			// 有资源权限的，通过
			for (ResourceEnum code : codes) {
				if (token.getResources().containsKey(code.getCode())) {
					return true;
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

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

	}

}
