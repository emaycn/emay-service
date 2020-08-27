package cn.emay.utils;

import cn.emay.utils.result.Result;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Random;
import java.util.UUID;

/**
 * 应用公钥密钥扩展号的工具类
 *
 * @author liwei
 */
public class AppUtils {

    private static final String[] SERVICECODE_BEGIN_3 = new String[]{"6", "8", "9", "1", "0"};
    private static final String[] SERVICECODE_BEGIN_6 = new String[]{"2", "5", "7", "4", "3"};

    /**
     * 生成AppKey
     */
    public synchronized static String genAppKey() {
        return UUID.randomUUID().toString().replace("-", "").toLowerCase().substring(0, 16);
    }

    /**
     * 生成密钥
     */
    public synchronized static String genSecretKey() {
        return UUID.randomUUID().toString().replace("-", "").toLowerCase().substring(0, 16);
    }

    /**
     * 生成服务号
     */
    public synchronized static String genAppCode(int length) {
        StringBuilder digitRandom = new StringBuilder();
        Random rand = new Random();
        int index = rand.nextInt(5);
        if (length == 3) {
            digitRandom.append(SERVICECODE_BEGIN_3[index]);
        } else {
            digitRandom.append(SERVICECODE_BEGIN_6[index]);
        }
        for (int i = 0; i < length - 1; i++) {
            digitRandom.append(rand.nextInt(10));
        }
        return digitRandom.toString();
    }

    /**
     * 生成随机数字
     */
    public synchronized static String genNumber(int length) {
        StringBuilder digitRandom = new StringBuilder();
        Random rand = new Random();
        for (int i = 0; i < length - 1; i++) {
            digitRandom.append(rand.nextInt(10));
        }
        return digitRandom.toString();
    }

    /**
     * 判断code是否符合规矩
     */
    public static Result checkAppCode(String code) {
        if (StringUtils.isEmpty(code)) {
            return Result.badResult("服务号不能为空");
        }
        if (code.length() == 3) {
            String first = code.substring(0, 1);
            boolean ok = Arrays.asList(SERVICECODE_BEGIN_3).contains(first);
            return ok ? Result.rightResult() : Result.badResult("3位服务号只能是 6,8,9,1,0开头");
        } else if (code.length() == 6) {
            String first = code.substring(0, 1);
            boolean ok = Arrays.asList(SERVICECODE_BEGIN_6).contains(first);
            return ok ? Result.rightResult() : Result.badResult("6位服务号只能是 2,4,3,7,5开头");
        } else {
            return Result.badResult("服务号只能是3位或者6位");
        }
    }


}
