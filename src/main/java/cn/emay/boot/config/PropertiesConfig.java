package cn.emay.boot.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

/**
 * 自定义参数配置
 * 
 * @author Frank
 *
 */
@Configuration
@ConfigurationProperties(prefix = "properties")
@Order(0)
public class PropertiesConfig {

	private String storeQueueBaseDir;
	
	private String[] excludeUrlAuth;
	
	private String[] excludeUrlXss;
	
	private boolean swaggerPageOn;

	public String getStoreQueueBaseDir() {
		return storeQueueBaseDir;
	}

	public void setStoreQueueBaseDir(String storeQueueBaseDir) {
		this.storeQueueBaseDir = storeQueueBaseDir;
	}

	public String[] getExcludeUrlAuth() {
		return excludeUrlAuth;
	}

	public void setExcludeUrlAuth(String[] excludeUrlAuth) {
		this.excludeUrlAuth = excludeUrlAuth;
	}

	public String[] getExcludeUrlXss() {
		return excludeUrlXss;
	}

	public void setExcludeUrlXss(String[] excludeUrlXss) {
		this.excludeUrlXss = excludeUrlXss;
	}

	public boolean isSwaggerPageOn() {
		return swaggerPageOn;
	}

	public void setSwaggerPageOn(boolean swaggerPageOn) {
		this.swaggerPageOn = swaggerPageOn;
	}

}
