package cn.emay.boot.web;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import cn.emay.boot.dto.web.WebToken;
import cn.emay.boot.pojo.system.Resource;
import cn.emay.boot.pojo.system.User;
import cn.emay.boot.service.system.UserService;

/**
 * 拦截没有权限的请求
 * 
 * @author Frank
 *
 */
@Component
public class AuthInterceptor extends HandlerInterceptorAdapter {

	private Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	private UserService userService;

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
			User user = WebUtils.getCurrentUser();
			// 未登陆，拦截
			if (user == null) {
				WebUtils.logout();
				WebUtils.toNoLogin();
				return false;
			}
			String[] codes = auth.value();
			// 仅需要登录权限的，通过--有注解无Code即视为仅需要登录
			if (codes == null || codes.length == 0) {
				return true;
			}
			Set<String> codeSet = new HashSet<>();
			for (String code : codes) {
				codeSet.add(code);
			}
			WebToken token = WebUtils.getWebToken();
			// 没有Token，拦截
			if (token == null) {
				return false;
			}
			List<Resource> auths = token.getAuth();
			boolean isHasAuth = false;
			for (Resource resource : auths) {
				if (codeSet.contains(resource.getResourceCode())) {
					isHasAuth = true;
					break;
				}
			}
			// 无资源权限的，拦截
			if (!isHasAuth) {
				WebUtils.toError("无权访问");
				return false;
			}
			user = userService.findById(user.getId());
			// 被删除或者被锁定，拦截
			if (user == null || user.getState() == User.STATE_OFF) {
				WebUtils.logout();
				WebUtils.toNoLogin();
				return false;
			}
			return true;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return false;
		}
		
	}

	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

	}

	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

	}

}
