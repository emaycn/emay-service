package cn.emay.utils;

/**
 * 唯一序列生成器
 *
 * @author Frank
 *
 */
public class BusinessIdUtils {

	private static long LAST_MILLIS_INDEX;

	private static final long RESET_TIME = 1000L * 1000L * 1L;

	static {
		resetMillis();
	}

	/**
	 * 生成18位唯一序列<br/>
	 * 每毫秒最多生成1000个<br/>
	 * 如果每毫秒的请求生成数多余1000个，允许超当前时间生成
	 * 
	 * @param serverCode 唯一编码，两位
	 * @return
	 */
	public synchronized static String genId(String serverCode) {
		resetMillis();
		return (LAST_MILLIS_INDEX++) + serverCode;
	}

	private static void resetMillis() {
		long now = System.currentTimeMillis() * 1000L;
		if (RESET_TIME + LAST_MILLIS_INDEX < now) {
			LAST_MILLIS_INDEX = now;
		}
	}

}
