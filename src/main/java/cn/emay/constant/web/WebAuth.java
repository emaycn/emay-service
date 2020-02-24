package cn.emay.constant.web;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 权限控制
 * 
 * @author Frank
 *
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface WebAuth {

	/**
	 * 可以访问此方法的资源代码<br/>
	 * 
	 * @return
	 */
	ResourceEnum[] value() default {};

}
