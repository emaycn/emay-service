package cn.emay.utils;


import java.text.NumberFormat;
import java.text.ParseException;

/**
 * 计算工具类
 *
 * @author chang
 */
public class MathUtils {

    /**
     * double转百分数
     *
     * @param num 需要转的数字
     * @return 转换后的百分数
     */
    public static String doubleToPercentage(Double num) {
        NumberFormat percentInstance = NumberFormat.getPercentInstance();
        // 保留小数两位
        percentInstance.setMaximumFractionDigits(2);
        return percentInstance.format(num);
    }


    /**
     * 百分数转double
     *
     * @param percentage 需要的百分数
     * @return 转换后的数字
     */
    public static Double percentageToDouble(String percentage) throws ParseException {
        // 接口返回的是Number对象，但是实际是Double类型
        return (Double) NumberFormat.getPercentInstance().parse(percentage);
    }


}
