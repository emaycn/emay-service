package cn.emay.configuration.superscheduler;

import cn.emay.redis.RedisClient;
import cn.emay.superscheduler.SuperScheduler;
import cn.emay.superscheduler.core.OnlyLockHandler;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.annotation.Resource;

/**
 * super scheduler 配置单元<br/>
 * 将此配置到spring中
 */
@Configuration
@ConfigurationProperties(prefix = "scheduler")
@DependsOn("RedisClient")
public class SuperSchedulerConfiguration {

    /**
     * 线程池核心线程数
     */
    private int poolSize;
    /**
     * 线程池线程名称前缀
     */
    private String threadNamePrefix;
    /**
     * 线程池停止时等待业务执行完毕时间
     */
    private int awaitTerminationSeconds;
    /**
     * 单节点锁名字
     */
    private String onlyLockName;

    @Bean("SuperScheduler1")
    public SuperScheduler superScheduler() {
        return new SuperScheduler(poolSize, threadNamePrefix, awaitTerminationSeconds, onlyLockName, genOnlyLock());
    }

    @Bean
    public OnlyLockHandler genOnlyLock() {
        return new OnlyLockHandler() {

            @Resource(name = "RedisClient")
            private RedisClient redis;

            private static final String KEY = "KV_TASK_LOCK_APPLY_";

            @Override
            public boolean lock(String onlyLockName, String nodeId, int seconds) {
                String key = KEY + onlyLockName;
                String currentNodeId = redis.get(key);
                if (currentNodeId == null) {
                    return redis.setnx(key, nodeId, seconds);
                } else {
                    if (currentNodeId.equals(nodeId)) {
                        redis.expire(key, seconds);
                        return true;
                    } else {
                        return false;
                    }
                }
            }

            @Override
            public void unLock(String onlyLockName, String nodeId) {
                String key = KEY + onlyLockName;
                redis.del(key);
            }

        };
    }

    public int getPoolSize() {
        return poolSize;
    }

    public void setPoolSize(int poolSize) {
        this.poolSize = poolSize;
    }

    public String getThreadNamePrefix() {
        return threadNamePrefix;
    }

    public void setThreadNamePrefix(String threadNamePrefix) {
        this.threadNamePrefix = threadNamePrefix;
    }

    public int getAwaitTerminationSeconds() {
        return awaitTerminationSeconds;
    }

    public void setAwaitTerminationSeconds(int awaitTerminationSeconds) {
        this.awaitTerminationSeconds = awaitTerminationSeconds;
    }

    public String getOnlyLockName() {
        return onlyLockName;
    }

    public void setOnlyLockName(String onlyLockName) {
        this.onlyLockName = onlyLockName;
    }

}
