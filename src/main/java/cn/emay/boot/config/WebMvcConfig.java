package cn.emay.boot.config;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import cn.emay.boot.web.interceptor.AuthInterceptor;

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
		auth.excludePathPatterns(propertiesConfig.getExcludeUrlAuth());
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		/* 所有静态文件通过/static/*访问 */
		registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
		/* 增加Swagger页面配置 */
		if (propertiesConfig.isSwaggerPageOn()) {
			registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
			registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
		}
	}

	/**
	 * 增加XSS过滤器
	 * 
	 * @return
	 */
	@Bean
	public FilterRegistrationBean<Filter> filterRegistrationBean() {
		FilterRegistrationBean<Filter> registration = new FilterRegistrationBean<Filter>();
		XssFilter xss = new XssFilter();
		xss.setExcludedPageArray(propertiesConfig.getExcludeUrlXss());
		registration.setFilter(xss);
		registration.setOrder(1);
		registration.setEnabled(true);
		registration.addUrlPatterns("/*");
		return registration;
	}

	/**
	 * XSS防御<br/>
	 * 可以指定排除的URL
	 * 
	 * @author Frank
	 *
	 */
	public static class XssFilter implements Filter {

		FilterConfig filterConfig = null;

		private Set<String> excludedPageArray;

		@Override
		public void init(FilterConfig filterConfig) throws ServletException {
			this.filterConfig = filterConfig;
		}

		@Override
		public void destroy() {
			this.filterConfig = null;
		}

		@Override
		public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
			if (excludedPageArray != null && excludedPageArray.contains(((HttpServletRequest) request).getServletPath())) {
				chain.doFilter(request, response);
			} else {
				chain.doFilter(new XssHttpServletRequestWrapper((HttpServletRequest) request), response);
			}
		}

		public void setExcludedPageArray(String[] excludeXssCheck) {
			if (excludeXssCheck == null || excludeXssCheck.length == 0) {
				return;
			}
			excludedPageArray = new HashSet<>();
			for (String url : excludeXssCheck) {
				excludedPageArray.add(url);
			}
		}

	}

	/**
	 * request转化器
	 * 
	 * @author Frank
	 *
	 */
	public static class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {

		public XssHttpServletRequestWrapper(HttpServletRequest servletRequest) {
			super(servletRequest);
		}

		@Override
		public String[] getParameterValues(String parameter) {
			String[] values = super.getParameterValues(parameter);
			if (values == null) {
				return null;
			}
			int count = values.length;
			String[] encodedValues = new String[count];
			for (int i = 0; i < count; i++) {
				encodedValues[i] = cleanXSS(values[i]);
			}
			return encodedValues;
		}

		@Override
		public String getParameter(String parameter) {
			String value = super.getParameter(parameter);
			if (value == null) {
				return null;
			}
			return cleanXSS(value);
		}

		@Override
		public String getHeader(String name) {
			String value = super.getHeader(name);
			if (value == null) {
				return null;
			}
			return cleanXSS(value);
		}

		private String cleanXSS(String value) {
			if (value == null || value.length() == 0) {
				return value;
			}
			StringBuffer buffer = new StringBuffer();
			char[] vchars = value.toCharArray();
			for (char vc : vchars) {
				buffer.append(this.htmlEncode(vc));
			}
			return buffer.toString();
		}

		private String htmlEncode(char c) {
			switch (c) {
			case '&':
				return "&amp;";
			case '<':
				return "&lt;";
			case '>':
				return "&gt;";
			case '"':
				return "&quot;";
			case '`':
				return "&#x27;";
			case '\'':
				return "&#39;";
			default:
				return String.valueOf(c);
			}
		}

	}

}