package cn.emay.constant.redis;

public class BusinessCacheKeys {

    /**
     * 缓存余额_企业ID=Long
     */
    public final static String KV_CLIENT_BALANCE_ = "KV_CLIENT_BALANCE_";
    /**
     * 业务编码计数器
     */
    public final static String KV_BUSINESS_CODE_NOW = "KV_BUSINESS_CODE_NOW";
    /**
     * 业务编码锁占用key前缀
     */
    public static final String KV_BUSINESS_CODE_LOCK = "KV_BUSINESS_CODE_LOCK_";
}
