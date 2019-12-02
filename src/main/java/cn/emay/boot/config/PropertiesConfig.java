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
	 * 是否开发环境
	 */
	private boolean dev;
	/**
	 * 上传文件存储目录
	 */
	private String uploadDirPath;

	public boolean isDev() {
		return dev;
	}

	public void setDev(boolean dev) {
		this.dev = dev;
	}

	public String getUploadDirPath() {
		return uploadDirPath;
	}

	public void setUploadDirPath(String uploadDirPath) {
		this.uploadDirPath = uploadDirPath;
	}

}
