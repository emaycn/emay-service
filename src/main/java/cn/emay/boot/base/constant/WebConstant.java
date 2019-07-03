package cn.emay.boot.base.constant;

/**
 * 常量
 * 
 * @author Frank
 *
 */
public class WebConstant {

	/**
	 * SESSION ID
	 */
	public final static String SESSION_ID = "EM_WEB_2002";

	/**
	 * 登录过期时间(秒)
	 */
	public final static Integer LOGIN_TIMEOUT = 7200;

	/**
	 * 验证码登录标识
	 */
	public final static String CAPTCHA_TAG_LOGIN = "LOGIN";

	/**
	 * request中token的标识
	 */
	public final static String REQUEST_TOKEN = "TOKEN_CURRENT";

	/**
	 * request中用户的标识
	 */
	public final static String REQUEST_USER = "USER_CURRENT";

}
