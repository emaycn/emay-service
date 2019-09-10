package cn.emay.boot.utils;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.emay.utils.string.StringUtils;

/**
 * 校验工具类
 * 
 * @author lijunjian
 *
 */
public class CheckUtils {

	/**
	 * 完整号码起始
	 */
	private static final String MOBILE_FULL_START = "+861";
	/**
	 * 完整号码位数
	 */
	private static final int MOBILE_FULL_LENGTH = 14;
	/**
	 * 号码起始
	 */
	private static final String MOBILE_START = "1";
	/**
	 * 号码位数
	 */
	private static final int MOBILE_LENGTH = 11;

	/**
	 * 校验邮箱
	 * 
	 * @param email
	 *            邮箱
	 * @return
	 */
	public static boolean isEmail(String email) {
		if (StringUtils.isEmpty(email)) {
			return false;
		}
		String regExp = "^([a-zA-Z0-9]+[_|\\_|\\.|\\-]?)*[\\u4e00-\\u9fa5a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\\_|\\.|\\-]?)*[a-zA-Z0-9]*[\\u4e00-\\u9fa5a-zA-Z0-9]+\\.[a-zA-Z]*[\\u4e00-\\u9fa5a-zA-Z0-9]{2,3}$";
		Pattern p = Pattern.compile(regExp);
		Matcher m = p.matcher(email);
		return m.matches();
	}

	/**
	 * 验证手机号
	 * 
	 * @param mobile
	 *            手机号
	 * @return
	 */
	public static boolean isMobile(String mobile) {
		if (null == mobile || "".equals(mobile)) {
			return false;
		}
		if (mobile.startsWith(MOBILE_FULL_START)) {
			if (mobile.length() == MOBILE_FULL_LENGTH) {
				return true;
			} else {
				return false;
			}
		} else if (mobile.startsWith(MOBILE_START)) {
			if (mobile.length() == MOBILE_LENGTH) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	/**
	 * 是否只含有中文和英文
	 * 
	 * @param val
	 * @return
	 */
	public static boolean isChineseOrEnglish(String val) {
		if (null == val || "".equals(val)) {
			return false;
		}
		String regExp = "^[\u2E80-\u9FFFa-zA-Z]+$";
		Pattern p = Pattern.compile(regExp);
		Matcher m = p.matcher(val);
		return m.matches();
	}

	/**
	 * 是否包含特殊字符
	 * 
	 * @param value
	 * @return
	 */
	public static boolean existSpecial(String value) {
		String regExp = "[\u4e00-\u9fa5\\w]+";
		Pattern p = Pattern.compile(regExp);
		Matcher m = p.matcher(value);
		return !m.matches();
	}
	
	/**
	 * Excel导入的错误信息数组
	 * 
	 */
	public static String[] copyOfRange(String[] obj, String errorMessage) {
		String[] error = Arrays.copyOfRange(obj, 0, obj.length + 1);
		error[obj.length] = errorMessage;
		return error;
	}

}
