package cn.emay.config;

import cn.emay.base.web.AuthInterceptor;
import cn.emay.utils.string.StringUtils;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.servlet.config.annotation.*;

import javax.annotation.Resource;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;
import java.util.*;

/**
 * web 配置
 *
 * @author Frank
 */
@Configuration
@Order(0)
public class WebMvcConfig implements WebMvcConfigurer {

    @Resource
    private AuthInterceptor authInterceptor;
    @Resource
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

    /**
     * 跨域
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        if (propertiesConfig.isDev()) {
            registry.addMapping("/**").allowedOrigins("*").allowCredentials(true).allowedMethods("GET", "POST", "PUT", "OPTIONS", "DELETE").allowedHeaders("*")
                    .exposedHeaders("cookies");
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

    /**
     * 增加XSS过滤器
     */
    @Bean
    public FilterRegistrationBean<Filter> filterRegistrationBean() {
        FilterRegistrationBean<Filter> registration = new FilterRegistrationBean<>();
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
     * 可以指定排除的URL和参数<br/>
     * 仅防御K=V形式的参数
     *
     * @author Frank
     */
    public static class XssFilter implements Filter {

        private final Set<String> excludedUrls = new HashSet<>();

        private final Map<String, Set<String>> excludedParams = new HashMap<>();

        @Override
        public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
            String url = ((HttpServletRequest) request).getServletPath();
            if (excludedUrls.contains(url)) {
                chain.doFilter(request, response);
            } else {
                chain.doFilter(new XssHttpServletRequestWrapper((HttpServletRequest) request, excludedParams.get(url)), response);
            }
        }

        public void setExcludedPageArray(String[] excludeXssCheck) {
            if (excludeXssCheck == null || excludeXssCheck.length == 0) {
                return;
            }
            for (String url : excludeXssCheck) {
                if (StringUtils.isEmpty(url)) {
                    continue;
                }
                if (url.contains("?")) {
                    String[] urlAndParams = url.split("\\?");
                    String realUrl = urlAndParams[0].trim();
                    String params = urlAndParams[1];
                    String[] paramArray = params.split(",");
                    Set<String> paramset = new HashSet<>();
                    Arrays.stream(paramArray).forEach(param -> paramset.add(param.trim()));
                    excludedParams.put(realUrl, paramset);
                } else {
                    excludedUrls.add(url);
                }
            }
        }
    }

    /**
     * request转化器
     *
     * @author Frank
     */
    public static class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {

        private final Set<String> excludedParams;

        public XssHttpServletRequestWrapper(HttpServletRequest servletRequest, Set<String> excludedParams) {
            super(servletRequest);
            this.excludedParams = excludedParams == null ? new HashSet<>() : excludedParams;
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
                encodedValues[i] = cleanXss(parameter, values[i]);
            }
            return encodedValues;
        }

        @Override
        public String getParameter(String parameter) {
            String value = super.getParameter(parameter);
            if (value == null) {
                return null;
            }
            return cleanXss(parameter, value);
        }

        private String cleanXss(String name, String value) {
            if (value == null || value.length() == 0) {
                return value;
            }
            if (excludedParams.contains(name)) {
                return value;
            }
            StringBuilder buffer = new StringBuilder();
            char[] vchars = value.toCharArray();
            for (char vc : vchars) {
                buffer.append(this.htmlEncode(vc));
            }
            return buffer.toString();
        }

        private String htmlEncode(char c) {
            switch (c) {
                case '>':
                    return "＞";
                case '<':
                    return "＜";
                case '\"':
                    return "“";
                case '\'':
                    return "‘";
                default:
                    return String.valueOf(c);
            }
        }

        public void processUrlEncoder(StringBuilder sb, String s, int index) {
            if (s.length() >= index + 2) {
                if (s.charAt(index + 1) == '3' && (s.charAt(index + 2) == 'c' || s.charAt(index + 2) == 'C')) { // %3c, %3C
                    sb.append('＜');
                    return;
                }
                if (s.charAt(index + 1) == '6' && s.charAt(index + 2) == '0') { // %3c (0x3c=60)
                    sb.append('＜');
                    return;
                }
                if (s.charAt(index + 1) == '3' && (s.charAt(index + 2) == 'e' || s.charAt(index + 2) == 'E')) { // %3e, %3E
                    sb.append('＞');
                    return;
                }
                if (s.charAt(index + 1) == '6' && s.charAt(index + 2) == '2') { // %3e (0x3e=62)
                    sb.append('＞');
                    return;
                }
            }
            sb.append(s.charAt(index));
        }

    }

}