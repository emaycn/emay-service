package cn.emay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

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

    @Order(2)
    @EventListener(ContextRefreshedEvent.class)
    public void start() {
        // 这里可以在启动前加载其他资源
    }

}
