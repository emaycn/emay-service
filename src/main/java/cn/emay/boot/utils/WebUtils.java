package cn.emay.boot.utils;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import cn.emay.boot.base.constant.WebConstant;
import cn.emay.boot.base.web.WebToken;
import cn.emay.boot.business.system.pojo.Resource;
import cn.emay.boot.business.system.pojo.User;
import cn.emay.boot.business.system.service.UserService;
import cn.emay.boot.config.PropertiesConfig;
import cn.emay.json.support.JsonServletSupport;
import cn.emay.redis.RedisClient;
import cn.emay.utils.result.Result;
import cn.emay.utils.web.servlet.RequestUtils;
import cn.emay.utils.web.servlet.ResponseUtils;

public class WebUtils {

	/**
	 * 获取当前线程的Request
	 */
	public static HttpServletRequest getCurrentHttpRequest() {
		return ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
	}

	/**
	 * 获取当前线程的Response
	 */
	public static HttpServletResponse getCurrentHttpResponse() {
		return ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getResponse();
	}

	/**
	 * 获取当前线程的Request
	 */
	public static HttpSession getCurrentHttpSession() {
		return ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest().getSession(true);
	}

	/**
	 * 登陆
	 */
	public static void login(User user, List<Resource> auth) {
		HttpSession session = getCurrentHttpSession();
		session.setAttribute(WebConstant.SESSION_USER_KEY, user);
		String sessionId = getSessionId();
		WebToken token = new WebToken(sessionId, user.getId(), auth);
		RedisClient redis = ApplicationContextUtils.getBean(RedisClient.class);
		redis.set(sessionId, token, WebConstant.LOGIN_TIMEOUT);
	}

	/**
	 * 登出
	 */
	public static void logout() {
		HttpSession session = getCurrentHttpSession();
		session.removeAttribute(WebConstant.SESSION_USER_KEY);
		String sessionId = getSessionId();
		RedisClient redis = ApplicationContextUtils.getBean(RedisClient.class);
		redis.del(sessionId);
	}

	/**
	 * 获取当前登录用户<br/>
	 * 非实时更新数据，如果需要实时真是数据，请根据ID自行查询
	 */
	public static User getCurrentUser() {
		HttpSession session = getCurrentHttpSession();
		User user = (User) session.getAttribute(WebConstant.SESSION_USER_KEY);
		if (user != null) {
			return user;
		}
		String sessionId = getSessionId();
		RedisClient redis = ApplicationContextUtils.getBean(RedisClient.class);
		WebToken token = redis.get(sessionId, WebToken.class);
		if (token == null) {
			return null;
		}
		UserService userService = ApplicationContextUtils.getBean(UserService.class);
		user = userService.findById(token.getUserId());
		if (user == null) {
			redis.del(sessionId);
			return null;
		}
		session.setAttribute(WebConstant.SESSION_USER_KEY, user);
		return user;
	}

	/**
	 * 是否有用户登录
	 */
	public static boolean isLogin() {
		return getCurrentUser() != null;
	}

	/**
	 * 获取Token
	 */
	public static WebToken getWebToken() {
		String sessionId = getSessionId();
		RedisClient redis = ApplicationContextUtils.getBean(RedisClient.class);
		WebToken token = redis.get(sessionId, WebToken.class);
		return token;
	}

	/**
	 * 获取SessionId
	 */
	public static String getSessionId() {
		HttpServletRequest request = getCurrentHttpRequest();
		HttpServletResponse response = getCurrentHttpResponse();
		Cookie cookie = RequestUtils.findCookie(request, WebConstant.SESSION_ID);
		String sessionId = null;
		if (cookie != null) {
			sessionId = cookie.getValue();
		}
		if (sessionId == null) {
			sessionId = "WEB-" + UUID.randomUUID().toString().replace("-", "").toUpperCase();
			ResponseUtils.addCookie(response, WebConstant.SESSION_ID, sessionId, -1);
		}
		return sessionId;
	}

	/**
	 * 跳到错误页面或者提示错误信息
	 */
	public static void toError(String errorMassage) throws IOException {
		HttpServletRequest request = getCurrentHttpRequest();
		HttpServletResponse response = getCurrentHttpResponse();
		if (RequestUtils.isAjaxRequest(request)) {
			JsonServletSupport.outputWithJson(response, Result.badResult("-1", errorMassage, null));
		} else {
			PropertiesConfig proper = ApplicationContextUtils.getBean(PropertiesConfig.class);
			response.sendRedirect(proper.getErrorPage() + "?msg=" + errorMassage);
		}
	}

	/**
	 * 跳转到登陆页面或者提示未登陆信息
	 */
	public static void toNoLogin() throws IOException {
		HttpServletRequest request = getCurrentHttpRequest();
		HttpServletResponse response = getCurrentHttpResponse();
		if (RequestUtils.isAjaxRequest(request)) {
			JsonServletSupport.outputWithJson(response, Result.badResult("-222", "您还未登陆，请先登录", null));
		} else {
			PropertiesConfig proper = ApplicationContextUtils.getBean(PropertiesConfig.class);
			response.sendRedirect(proper.getLoginPage());
		}
	}

}
