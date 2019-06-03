package cn.emay.boot.web.interceptor;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import cn.emay.boot.dto.web.WebToken;
import cn.emay.boot.pojo.system.Resource;
import cn.emay.boot.pojo.system.User;
import cn.emay.boot.web.auth.WebAuth;
import cn.emay.boot.web.utils.WebUtils;

/**
 * 拦截没有权限的请求
 * 
 * @author Frank
 *
 */
@Component
public class AuthInterceptor extends HandlerInterceptorAdapter {

	private Logger log = LoggerFactory.getLogger(getClass());

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		User user = WebUtils.getCurrentUser();
		// 未登陆或者用户被锁，拦截
		if (user == null || user.getState() == User.STATE_OFF) {
			WebUtils.logout();
			WebUtils.toNoLogin();
			return false;
		}
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
		String[] codes = auth.value();
		// 资源没有要求权限的，通过
		if (codes == null || codes.length == 0) {
			return true;
		}
		Set<String> codeSet = new HashSet<>();
		for (String code : codes) {
			codeSet.add(code);
		}
		WebToken token = WebUtils.getWebToken();
		List<Resource> auths = token.getAuth();
		boolean isHasAuth = false;
		for (Resource resource : auths) {
			if (codeSet.contains(resource.getResourceCode())) {
				isHasAuth = true;
				break;
			}
		}
		// 有资源权限的，通过
		if (isHasAuth) {
			return true;
		} else {
			WebUtils.toError("无权访问");
			return false;
		}
	}

	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

	}

	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		if (ex != null)
			log.error(ex.getMessage(), ex);
	}

}
