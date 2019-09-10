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

	/**
	 * 排除的权限URL
	 */
	private String[] excludeUrlAuth;
	/**
	 * 排除的XSS防御URL
	 */
	private String[] excludeUrlXss;
	/**
	 * 是否开发环境
	 */
	private boolean dev;
	
	private String uploadFilePath;

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

	public boolean isDev() {
		return dev;
	}

	public void setDev(boolean dev) {
		this.dev = dev;
	}

	public String getUploadFilePath() {
		return uploadFilePath;
	}

	public void setUploadFilePath(String uploadFilePath) {
		this.uploadFilePath = uploadFilePath;
	}

}
