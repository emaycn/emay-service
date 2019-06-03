package cn.emay.boot.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

/**
 * 自定义参数配置
 * 
 * @author Frank
 *
 */
@Configuration
@Order(0)
public class PropertiesConfig {

	@Value("storeQueueBaseDir")
	private String storeQueueBaseDir;

	public String getStoreQueueBaseDir() {
		return storeQueueBaseDir;
	}

	public void setStoreQueueBaseDir(String storeQueueBaseDir) {
		this.storeQueueBaseDir = storeQueueBaseDir;
	}

}
