package cn.emay.utils;

import cn.emay.constant.redis.BusinessCacheKeys;
import cn.emay.redis.RedisClient;

/**
 * 唯一业务编码
 * 
 * @author Frank
 *
 */
public class OnlyBusinessCode {

	/**
	 * 业务编码
	 */
	private static String BUSINESS_CODE;

	/**
	 * 占用
	 * 
	 * @param redis
	 * @return
	 * @throws Exception
	 */
	public static void occpy(RedisClient redis) throws Exception {
		String numberStr = null;
		int errorTotal = 0;
		do {
			long nowNumber = redis.incr(BusinessCacheKeys.KV_BUSINESS_CODE_NOW);
			nowNumber = nowNumber % 100;
			numberStr = String.valueOf(100 + nowNumber).substring(1);
			boolean isHas = redis.exists(BusinessCacheKeys.KV_BUSINESS_CODE_LOCK + numberStr);
			if (isHas) {
				errorTotal++;
			} else {
				boolean isSuccess = redis.setnx(BusinessCacheKeys.KV_BUSINESS_CODE_LOCK + numberStr, numberStr, 60);
				if (isSuccess) {
					break;
				} else {
					errorTotal++;
				}
			}
			if (errorTotal > 100) {
				throw new Exception("no enough code for occpy .");
			}
		} while (true);
		BUSINESS_CODE = numberStr;
	}

	/**
	 * 续租
	 * 
	 * @param redis
	 * @param numberStr
	 */
	public static void renew(RedisClient redis) {
		redis.set(BusinessCacheKeys.KV_BUSINESS_CODE_LOCK + BUSINESS_CODE, "0", 60);
	}

	/**
	 * 退订
	 * 
	 * @param redis
	 * @param numberStr
	 */
	public static void unsubscribe(RedisClient redis) {
		redis.del(BusinessCacheKeys.KV_BUSINESS_CODE_LOCK + BUSINESS_CODE);
	}

	/**
	 * 获取code
	 * 
	 * @return
	 */
	public static String getCode() {
		return BUSINESS_CODE;
	}

}
