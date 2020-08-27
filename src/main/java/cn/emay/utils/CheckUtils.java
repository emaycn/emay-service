package cn.emay.utils;

import cn.emay.utils.string.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 校验工具类
 *
 * @author lijunjian
 */
public class CheckUtils {

    /**
     * 校验邮箱
     *
     * @param email 邮箱
     */
    public static boolean isEmail(String email) {
        return !isNotEmail(email);
    }

    /**
     * 是否只含有中文和英文
     */
    public static boolean isChineseOrEnglish(String val) {
        return !isNotChineseAndEnglish(val);
    }

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
     * @param email 邮箱
     * @return 是否正确
     */
    public static boolean isNotEmail(String email) {
        if (StringUtils.isEmpty(email)) {
            return true;
        }
        String regExp = "^([a-zA-Z0-9]+[_|.\\-]?)*[\\u4e00-\\u9fa5a-zA-Z0-9]+@([a-zA-Z0-9]+[_|.\\-]?)*[a-zA-Z0-9]*[\\u4e00-\\u9fa5a-zA-Z0-9]+\\.[a-zA-Z]*[\\u4e00-\\u9fa5a-zA-Z0-9]{2,3}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(email);
        return !m.matches();
    }

    /**
     * 校验金额，9位整数，4位小数
     *
     * @return 是否正确
     */
    public static Boolean checkAmountOfMoney(String number) {
        if (null == number || number.length() == 0) {
            return false;
        }
        String regExp = "^[1-9]\\d{0,8}(\\.\\d{1,4})?$|^0(\\.\\d{1,4})?$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(number);
        return m.matches();
    }

    /**
     * 验证手机号
     *
     * @param mobile 手机号
     * @return 是否正确
     */
    public static boolean isMobile(String mobile) {
        if (null == mobile || "".equals(mobile)) {
            return false;
        }
        mobile = mobile.trim();
        return mobile.startsWith(MOBILE_START) && mobile.length() == MOBILE_LENGTH;
    }

    /**
     * 是否不是只含有中文和英文
     *
     * @param val 值
     * @return 是否正确
     */
    public static boolean isNotChineseAndEnglish(String val) {
        if (null == val || "".equals(val)) {
            return true;
        }
        String regExp = "^[\u2E80-\u9FFFa-zA-Z]+$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(val);
        return !m.matches();
    }

    /**
     * 是否包含特殊字符
     *
     * @param value 值
     * @return 是否正确
     */
    public static boolean existSpecial(String value) {
        String regExp = "[\u4e00-\u9fa5\\w]+";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(value);
        return !m.matches();
    }

}
