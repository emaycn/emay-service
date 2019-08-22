package cn.emay.boot.utils;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 校验工具类
 * 
 * @author lijunjian
 *
 */
public class CheckUtil {

	/**
	 * 
	 * @Title: specialCodeEscape
	 * @Description: 特殊字符转义
	 * @param @param
	 *            value
	 * @param @return
	 * @return String
	 */
	public static String specialCodeEscape(String value) {
		if (null != value && !"".equals(value)) {
			value = value.replace("%", "\\%").replace("_", "\\_");
		}
		return value;
	}

	/**
	 * 校验是否包含特殊字符
	 * 
	 * @return false 包含
	 * @return true 不包含
	 */
	public static boolean notExistSpecial(String value) {
		String regExp = "[\u4e00-\u9fa5\\w]+";
		Pattern p = Pattern.compile(regExp);
		Matcher m = p.matcher(value);
		return m.matches();
	}

	/**
	 * 英文加数字不能以数字开头
	 * 
	 * @param val
	 * @return
	 */
	public static boolean checkUserName(String val) {
		if (null == val || "".equals(val)) {
			return false;
		}
		val = val.toLowerCase();
		String regExp = "^[a-zA-Z][a-zA-Z0-9]{3,15}$";
		Pattern p = Pattern.compile(regExp);
		Matcher m = p.matcher(val);
		return m.matches();
	}

	/**
	 * 验证只含有中文和英文
	 * 
	 * @param val
	 * @return
	 */
	public static boolean checkString(String val) {
		if (null == val || "".equals(val)) {
			return false;
		}
		String regExp = "^[\u2E80-\u9FFFa-zA-Z]+$";
		Pattern p = Pattern.compile(regExp);
		Matcher m = p.matcher(val);
		return m.matches();
	}

	/**
	 * 校验邮箱
	 * 
	 * @param email
	 *            邮箱
	 * @return
	 */
	public static boolean checkEmail(String email) {
		if (null == email || "".equals(email)) {
			return false;
		}
		String regExp = "^([a-zA-Z0-9]+[_|\\_|\\.|\\-]?)*[\\u4e00-\\u9fa5a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\\_|\\.|\\-]?)*[a-zA-Z0-9]*[\\u4e00-\\u9fa5a-zA-Z0-9]+\\.[a-zA-Z]*[\\u4e00-\\u9fa5a-zA-Z0-9]{2,3}$";

		Pattern p = Pattern.compile(regExp);
		Matcher m = p.matcher(email);
		return m.matches();
	}

	/**
	 * 验证手机号码格式
	 * 
	 * @param mobile
	 * @return
	 */
	public static boolean checkMobileFormat(String mobile) {
		if (null == mobile || "".equals(mobile)) {
			return false;
		}
		String regExp = "^1[3|4|5|6|7|8|9]\\d{9}$";
		Pattern p = Pattern.compile(regExp);
		Matcher m = p.matcher(mobile);
		return m.matches();
	}

	/**
	 * 判断是否为空
	 * 
	 * @param val
	 * @return
	 */
	public static boolean isEmpty(String val) {
		if (val == null || (val.trim()).length() == 0 || "null".equals(val.trim())) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 1-8位正整数
	 * 
	 * @Title: checkNumber
	 */
	public static boolean checkOneToEightNumber(String val) {
		if (null == val || "".equals(val)) {
			return false;
		}
		String regExp = "^\\+?[1-9]\\d{0,7}$";
		Pattern p = Pattern.compile(regExp);
		Matcher m = p.matcher(val);
		return m.matches();
	}

	/**
	 * 正整数校验
	 * 
	 * @param number
	 * @return
	 */
	public static boolean checkPositiveInteger(String number) {
		if (null == number || "".equals(number)) {
			return false;
		}
		String regExp = "^\\+?[1-9]\\d*$";
		Pattern p = Pattern.compile(regExp);
		Matcher m = p.matcher(number);
		return m.matches();
	}

	/**
	 * 效验IP、效验IP段
	 * 
	 * @param value
	 * @return
	 */
	public static boolean checkIpParagraph(String value) {
		String regExp = "(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]?\\d)){2}(\\.(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]?\\d|\\*))";
		Pattern p = Pattern.compile(regExp);
		Matcher m = p.matcher(value);
		return m.matches();
	}

	/**
	 * 效验1到9位 正整数 第一位不为0
	 * 
	 * @param number
	 * @return
	 */
	public static Boolean checkIntegernine(String number) {
		if (null == number || number.length() == 0 || "".equals(number)) {
			return false;
		}
		String regExp = "^[1-9][0-9]{0,8}$";
		Pattern p = Pattern.compile(regExp);
		Matcher m = p.matcher(number);
		return m.matches();
	}

	/**
	 * 校验金额，8位整数，3位小数
	 */
	public static Boolean checkAmountOfMoney(String number) {
		if (null == number || number.length() == 0 || "".equals(number)) {
			return false;
		}
		String regExp = "^[1-9]\\d{0,7}(\\.\\d{1,3})?$|^0(\\.\\d{1,3})?$";
		Pattern p = Pattern.compile(regExp);
		Matcher m = p.matcher(number);
		return m.matches();
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
