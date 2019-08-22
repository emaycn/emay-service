package cn.emay.boot.base.constant;

/**
 * 常量
 * 
 * @author Frank
 *
 */
public class WebConstant {

	/**
	 * Head 中的Token标识
	 */
	public final static String HEAD_AUTH_TOKEN = "AUTH-WEB-TOKEN";

	/**
	 * 登录过期时间(秒)
	 */
	public final static Integer LOGIN_TIMEOUT = 24 * 60 * 60;

	/**
	 * 一小时的毫秒数
	 */
	public final static Long HOUR_MILLIS = 60 * 60 * 1000L;

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

	/**
	 * 时间格式
	 */
	public final static String PARAMETER_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

}
