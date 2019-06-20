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
	 * Session 中的用户标识
	 */
	public final static String SESSION_USER_KEY = "systemUser";
	
	/**
	 * 验证码登录标识
	 */
	public final static String CAPTCHA_TAG_LOGIN = "LOGIN";

}
