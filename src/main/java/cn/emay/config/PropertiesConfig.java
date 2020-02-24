package cn.emay.config;

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
	 * 文件存储目录
	 */
	private String fileDirPath;
	/**
	 * 接口编码[必填参数]
	 */
	private String interfaceCode;
	/**
	 * 排除的XSS防御URL
	 */
	private String[] excludeUrlXss;

	public boolean isDev() {
		return dev;
	}

	public void setDev(boolean dev) {
		this.dev = dev;
	}

	public String getInterfaceCode() {
		return interfaceCode;
	}

	public void setInterfaceCode(String interfaceCode) {
		this.interfaceCode = interfaceCode;
	}

	public String getFileDirPath() {
		return fileDirPath;
	}

	public void setFileDirPath(String fileDirPath) {
		this.fileDirPath = fileDirPath;
	}

	public String[] getExcludeUrlXss() {
		return excludeUrlXss;
	}

	public void setExcludeUrlXss(String[] excludeUrlXss) {
		this.excludeUrlXss = excludeUrlXss;
	}

}
