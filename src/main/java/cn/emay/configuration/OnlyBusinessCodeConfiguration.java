package cn.emay.configuration;

import cn.emay.redis.RedisClient;
import cn.emay.utils.ApplicationContextUtils;
import cn.emay.utils.OnlyBusinessCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

/**
 * 唯一标识维护器
 */
@Configuration
public class OnlyBusinessCodeConfiguration {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Resource
    private RedisClient redis;

    @Scheduled(initialDelay = 10000, fixedDelay = 10000)
    protected void heartBeatTask() {
        OnlyBusinessCode.renew(redis);
    }

    @PreDestroy
    public void closed() {
        OnlyBusinessCode.unsubscribe(redis);
    }

    @PostConstruct
    public void start() {
        try {
            OnlyBusinessCode.occpy(redis);
            log.info("BUSINESS_CODE:[{}]", OnlyBusinessCode.getCode());
        } catch (Exception e) {
            log.error("error", e);
            SpringApplication.exit(ApplicationContextUtils.getApplicationContext(), () -> -999);
        }
    }

}
