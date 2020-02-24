package cn.emay.config;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import cn.emay.utils.ApplicationContextUtils;

/**
 * Spring上下文配置，为ApplicationContextUtils动态获取bean服务
 * 
 * @author Frank
 *
 */
@Component
@Order(0)
public class ApplicationContextConfig implements ApplicationContextAware {

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		ApplicationContextUtils.setApplicationContext(applicationContext);
	}

}