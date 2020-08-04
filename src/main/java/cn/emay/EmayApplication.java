package cn.emay;

import cn.emay.redis.RedisClient;
import cn.emay.task.spring.TaskSchedulerSpring;
import cn.emay.utils.ApplicationContextUtils;
import cn.emay.utils.OnlyBusinessCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.annotation.Resource;

/**
 * 启动入口类
 *
 * @author Frank
 */
@SpringBootApplication(scanBasePackages = "cn.emay")
@EnableConfigurationProperties
@EnableWebMvc
@EnableTransactionManagement
@EnableScheduling
public class EmayApplication {

    public static void main(String[] args) {
        SpringApplication.run(EmayApplication.class, args);
    }

    /**
     * 定时续租业务编码
     */
    @Scheduled(initialDelay = 10000, fixedDelay = 10000)
    protected void heartBeatTask() {
        RedisClient redis = ApplicationContextUtils.getBean(RedisClient.class);
        OnlyBusinessCode.renew(redis);
    }

    @EventListener
    public void closed(ContextClosedEvent event) {
        TaskSchedulerSpring taskSchedulerSpring = ApplicationContextUtils.getBean(TaskSchedulerSpring.class);
        taskSchedulerSpring.stop();
        RedisClient redis = ApplicationContextUtils.getBean(RedisClient.class);
        OnlyBusinessCode.unsubscribe(redis);
    }

    @Component
    public static class CheckParamsApplicationRunner implements ApplicationRunner {

        Logger log = LoggerFactory.getLogger(getClass());

        @Resource
        private TaskSchedulerSpring taskSchedulerSpring;
        @Resource
        private RedisClient redis;

        @Override
        public void run(ApplicationArguments args) {
            try {
                OnlyBusinessCode.occpy(redis);
                log.info("BUSINESS_CODE:[{}]", OnlyBusinessCode.getCode());
                taskSchedulerSpring.start();
            } catch (Exception e) {
                log.error("error", e);
                SpringApplication.exit(ApplicationContextUtils.getApplicationContext(), () -> -999);
            }

        }

    }

}
