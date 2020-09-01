package cn.emay.configuration.superscheduler;

import cn.emay.superscheduler.SuperScheduler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * super scheduler 配置单元<br/>
 * 将此配置到spring中
 */
@Configuration
public class SuperSchedulerConfiguration {

    @Bean
    public SuperScheduler superScheduler() {
        return new SuperScheduler();
    }

}
