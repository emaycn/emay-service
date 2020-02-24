package cn.emay.utils;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import cn.emay.business.client.pojo.Client;
import cn.emay.business.client.service.ClientService;
import cn.emay.business.system.pojo.Resource;
import cn.emay.business.system.pojo.User;
import cn.emay.business.system.service.UserService;
import cn.emay.config.PropertiesConfig;
import cn.emay.constant.web.WebConstant;
import cn.emay.constant.web.WebToken;
import cn.emay.excel.common.ExcelVersion;
import cn.emay.excel.write.ExcelWriter;
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
	 * 
	 * @return
	 */
	public static HttpServletRequest getCurrentHttpRequest() {
		return ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
	}

	/**
	 * 获取当前线程的Response
	 * 
	 * @return
	 */
	public static HttpServletResponse getCurrentHttpResponse() {
		return ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getResponse();
	}

	/**
	 * 获取当前线程的Request
	 * 
	 * @return
	 */
	public static HttpSession getCurrentHttpSession() {
		return ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest().getSession(true);
	}

	/**
	 * 登陆
	 *
	 * @param user      用户
	 * @param resources 用户权限资源
	 * @return
	 */
	public static WebToken login(User user, List<Resource> resources, Client client) {
		String sessionId = MessageFormat.format(WebConstant.WEB_TOKEN, UUID.randomUUID().toString().replace("-", "").toUpperCase());
		WebToken token = new WebToken(sessionId, user, resources, client);
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
	 * 获取当前登录用户所属系统<br/>
	 * 
	 * @return
	 */
	public static Client getCurrentClient() {
		Client client = (Client) getCurrentHttpRequest().getAttribute(WebConstant.CLIENT_CURRENT);
		if (client != null) {
			return client;
		}
		User user = getCurrentUser();
		if (user == null) {
			return null;
		}
		ClientService clientService = ApplicationContextUtils.getBean(ClientService.class);
		client = clientService.findByUserId(user.getId());
		if (client != null) {
			getCurrentHttpRequest().setAttribute(WebConstant.CLIENT_CURRENT, client);
		}
		return client;
	}

	/**
	 * 获取当前登录用户<br/>
	 * 每一次请求都要查询一次数据库来验证用户状态，后续可以把用户状态放到redis管理，状态的改变也随时更新redis
	 * 
	 * @return
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
	 * 
	 * @return
	 */
	public static boolean isLogin() {
		return getCurrentUser() != null;
	}

	/**
	 * 获取Token
	 * 
	 * @return
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
			// 如果token缓存时间小于1小时,刷新
			if (redis.ttl(sessionId) < WebConstant.HOUR_MILLIS) {
				redis.expire(sessionId, WebConstant.LOGIN_TIMEOUT);
			}
			getCurrentHttpRequest().setAttribute(WebConstant.REQUEST_TOKEN, token);
		}
		return token;
	}

	/**
	 * 获取SessionId
	 * 
	 * @return
	 */
	public static String getSessionId() {
		String sessionId = getCurrentHttpRequest().getHeader(WebConstant.HEAD_AUTH_TOKEN);
		if (null == sessionId) {
			sessionId = getCurrentHttpRequest().getParameter(WebConstant.HEAD_AUTH_TOKEN);
		}
		return sessionId;
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
	
	/**
	 * 跳转到登陆页面或者提示未登陆信息
	 */
	public static void toNoChangePass() throws IOException {
		HttpServletResponse response = getCurrentHttpResponse();
		PropertiesConfig propertiesConfig = ApplicationContextUtils.getBean(PropertiesConfig.class);
		if (propertiesConfig.isDev()) {
			String origin = getCurrentHttpRequest().getHeader("Origin");
			response.setHeader("Access-Control-Allow-Origin", origin);
			response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
			response.setHeader("Access-Control-Allow-Credentials", "true");
			response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, X-CSRF-TOKEN");
		}
		JsonServletSupport.outputWithJson(response, Result.badResult("-111", "首次登陆，未修改密码", null));
	}

	/**
	 * 到处报表
	 * 
	 * @param <T>
	 * @param list
	 */
	public static void exportExcel(ExcelVersion version, List<?> list) {
		HttpServletResponse response = getCurrentHttpResponse();
		response.setContentType("application/octet-stream");
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Content-Disposition", "attachment; filename=appdayreport.xlsx");
		try {
			ExcelWriter.write(response.getOutputStream(), version, list);
		} catch (IOException e) {
			throw new IllegalArgumentException(e);
		}
	}

}
