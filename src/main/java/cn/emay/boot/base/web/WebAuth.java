package cn.emay.boot.base.web;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import cn.emay.boot.base.constant.ResourceEnum;

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
	 * 可以访问此类/方法的资源代码<br/>
	 * 
	 * @return
	 */
	ResourceEnum[] value() default {};

}
