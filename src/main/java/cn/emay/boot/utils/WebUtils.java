package cn.emay.boot.utils;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

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

/**
 * Web工具类
 * 
 * @author Frank
 *
 */
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
	public static WebToken login(User user, List<Resource> resources) {
		String sessionId = "WEB-" + UUID.randomUUID().toString().replace("-", "").toUpperCase();
		WebToken token = new WebToken(sessionId, user, resources);
		RedisClient redis = ApplicationContextUtils.getBean(RedisClient.class);
		redis.set(sessionId, token, WebConstant.LOGIN_TIMEOUT);
		return token;
	}

	/**
	 * 登出
	 */
	public static void logout() {
		String sessionId = getSessionId();
		if (sessionId != null) {
			RedisClient redis = ApplicationContextUtils.getBean(RedisClient.class);
			redis.del(sessionId);
		}
	}

	/**
	 * 获取当前登录用户<br/>
	 * 每一次请求都要查询一次数据库来验证用户状态，后续可以把用户状态放到redis管理，状态的改变也随时更新redis
	 */
	public static User getCurrentUser() {
		User user = (User) getCurrentHttpRequest().getAttribute(WebConstant.REQUEST_USER);
		if (user != null) {
			return user;
		}
		WebToken token = getWebToken();
		if (token == null) {
			return null;
		}
		UserService userService = ApplicationContextUtils.getBean(UserService.class);
		user = userService.findById(token.getUser().getId());
		if (user == null) {
			String sessionId = getSessionId();
			if (sessionId != null) {
				RedisClient redis = ApplicationContextUtils.getBean(RedisClient.class);
				redis.del(sessionId);
			}
			return null;
		} else {
			getCurrentHttpRequest().setAttribute(WebConstant.REQUEST_USER, user);
		}
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
		WebToken token = (WebToken) getCurrentHttpRequest().getAttribute(WebConstant.REQUEST_TOKEN);
		if (token != null) {
			return token;
		}
		String sessionId = getSessionId();
		if (sessionId == null) {
			return null;
		}
		RedisClient redis = ApplicationContextUtils.getBean(RedisClient.class);
		token = redis.get(sessionId, WebToken.class);
		if (token != null) {
			if (token.getCreateTime().getTime() + (WebConstant.LOGIN_TIMEOUT - 1 * 60 * 60) * 1000L < System.currentTimeMillis()) {
				redis.expire(sessionId, WebConstant.LOGIN_TIMEOUT);
			}
			getCurrentHttpRequest().setAttribute(WebConstant.REQUEST_TOKEN, token);
		}
		return token;
	}

	/**
	 * 获取SessionId
	 */
	public static String getSessionId() {
		return getCurrentHttpRequest().getHeader(WebConstant.SESSION_ID);
	}

	/**
	 * 跳到错误页面或者提示错误信息
	 */
	public static void toError(String errorMassage) throws IOException {
		HttpServletResponse response = getCurrentHttpResponse();
		PropertiesConfig propertiesConfig = ApplicationContextUtils.getBean(PropertiesConfig.class);
		if (propertiesConfig.isDev()) {
			String origin = getCurrentHttpRequest().getHeader("Origin");
			response.setHeader("Access-Control-Allow-Origin", origin);
			response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
			response.setHeader("Access-Control-Allow-Credentials", "true");
			response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, X-CSRF-TOKEN");
		}
		JsonServletSupport.outputWithJson(response, Result.badResult("-1", errorMassage, null));

	}

	/**
	 * 跳转到登陆页面或者提示未登陆信息
	 */
	public static void toNoLogin() throws IOException {
		HttpServletResponse response = getCurrentHttpResponse();
		PropertiesConfig propertiesConfig = ApplicationContextUtils.getBean(PropertiesConfig.class);
		if (propertiesConfig.isDev()) {
			String origin = getCurrentHttpRequest().getHeader("Origin");
			response.setHeader("Access-Control-Allow-Origin", origin);
			response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
			response.setHeader("Access-Control-Allow-Credentials", "true");
			response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, X-CSRF-TOKEN");
		}
		JsonServletSupport.outputWithJson(response, Result.badResult("-222", "您还未登陆，请先登录", null));
	}

}
