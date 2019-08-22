package cn.emay.boot.utils;

import java.util.Random;

/**
 * 随机工具类
 * 
 * @author Frank
 *
 */
public class RandomUtils {

	/**
	 * 生成随机字符串<br/>
	 * [0-9A-Za-z]
	 * 
	 * @param length
	 *            字符串长度
	 * @return
	 */
	public static String randomCharset(int length) {
		if (length <= 0) {
			return null;
		}
		StringBuffer val = new StringBuffer();
		Random random = new Random();
		for (int i = 0; i < length; i++) {
			if (random.nextInt(2) % 2 == 0) {
				int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;
				val.append((char) (random.nextInt(26) + temp));
			} else {
				val.append(String.valueOf(random.nextInt(10)));
			}
		}
		return val.toString();
	}

	/**
	 * 生成随机Int
	 * 
	 * @param max
	 *            最大值
	 * @return
	 */
	public static int randomInt(int max) {
		Random random = new Random(max);
		return random.nextInt();
	}

}
