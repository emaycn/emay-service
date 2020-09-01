package cn.emay.configuration.scheduler;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * Spring  @Scheduled 注解线程池配置
 */
@Configuration
public class SpringSchedulerConfiguration implements SchedulingConfigurer {

    /**
     * 自定义线程池<br/>
     * 如果线程池不满足，在此处自定义
     */
    @Bean(destroyMethod = "shutdown")
    public ThreadPoolTaskScheduler newThreadPoolTaskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(2);
        scheduler.setThreadNamePrefix("emay-");
        scheduler.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardPolicy());
        scheduler.setAwaitTerminationSeconds(60);
        scheduler.initialize();
        return scheduler;
    }

    /**
     * 往spring的scheduler注册器添加自定义的线程池和SuperScheduled任务
     *
     * @param taskRegistrar spring的scheduler注册器
     */
    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        ThreadPoolTaskScheduler scheduler = newThreadPoolTaskScheduler();
        taskRegistrar.setTaskScheduler(scheduler);
    }

}
