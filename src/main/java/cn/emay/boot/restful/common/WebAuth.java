package cn.emay.boot.restful.common;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface WebAuth {

	/**
	 * 可以访问此类/方法的资源代码<br/>
	 * 
	 * @return
	 */
	String[] value() default {};

}
