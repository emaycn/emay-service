package cn.emay.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * 启动入口类
 * 
 * @author Frank
 */
@ComponentScan("cn.emay")
@SpringBootApplication(scanBasePackages = "cn.emay")
@EnableConfigurationProperties
@EnableAutoConfiguration
@EnableWebMvc
@EnableTransactionManagement
public class EmayApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmayApplication.class, args);
	}

}
