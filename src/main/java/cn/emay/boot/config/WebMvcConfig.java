package cn.emay.boot.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import cn.emay.boot.base.web.AuthInterceptor;

/**
 * web 配置
 * 
 * @author Frank
 *
 */
@Configuration
@ConfigurationProperties
@Order(0)
public class WebMvcConfig implements WebMvcConfigurer {

	@Autowired
	private AuthInterceptor authInterceptor;
	@Autowired
	private PropertiesConfig propertiesConfig;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		/* 增加权限验证拦截器 */
		InterceptorRegistration auth = registry.addInterceptor(authInterceptor);
		auth.addPathPatterns("/**");
	}

	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
		configurer.enable();
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		if (propertiesConfig.isDev()) {
			// dev 环境开启 跨域
			registry.addMapping("/**").allowedOrigins("*").allowCredentials(true).allowedMethods("GET", "POST", "PUT", "OPTIONS", "DELETE").allowedHeaders("*").exposedHeaders("cookies");
		}
	}

	/**
	 * 静态资源
	 */
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		/* 增加Swagger页面配置 */
		if (propertiesConfig.isDev()) {
			registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
			registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
		}
	}

}