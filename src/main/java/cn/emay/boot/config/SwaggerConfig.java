package cn.emay.boot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@Order(0)
public class SwaggerConfig {

	@Bean
	public Docket customDocket() {
		return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo());
	}

	private ApiInfo apiInfo() {
		Contact contact = new Contact("发送短信接口", "http://127.0.0.1:8080/", "shidongxu@emay.cn");
		return new ApiInfoBuilder().title("发送短信接口").contact(contact).description("这里是 RESTful API 描述").version("1.0.0").build();
	}

}
