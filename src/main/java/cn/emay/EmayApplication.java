package cn.emay;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import cn.emay.config.PropertiesConfig;
import cn.emay.task.spring.TaskSchedulerSpring;
import cn.emay.utils.ApplicationContextUtils;

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
	
	@Component
	public class CheckParamsApplicationRunner implements ApplicationRunner {

		Logger log = LoggerFactory.getLogger(getClass());

		@Autowired
		private PropertiesConfig config;
		@Autowired
		private TaskSchedulerSpring taskSchedulerSpring;

		@Override
		public void run(ApplicationArguments args) throws Exception {
			if (!this.iSoK()) {
				log.error("interface code is not config or not find !!!");
				SpringApplication.exit(ApplicationContextUtils.getApplicationContext(), new ExitCodeGenerator() {
					@Override
					public int getExitCode() {
						return -999;
					}
				});
			}
			try {
				taskSchedulerSpring.start();
			} catch (Exception e) {
				log.error("error",e);
				SpringApplication.exit(ApplicationContextUtils.getApplicationContext(), new ExitCodeGenerator() {
					@Override
					public int getExitCode() {
						return -999;
					}
				});
			}
		}

		private boolean iSoK() {
			String code = config.getInterfaceCode();
			if (code == null) {
				return false;
			}
			if (code.length() != 2) {
				return false;
			}
			try {
				Integer.valueOf(code);
			} catch (Exception e) {
				return false;
			}
			return true;
		}
	}

}
