package cn.emay.constant.web;

import java.lang.annotation.*;

/**
 * 权限控制
 *
 * @author Frank
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface WebAuth {

    /**
     * 访问所需资源权限代码<br/>
     * 多值取或
     */
    ResourceEnum[] value();

    /**
     * 允许访问系统<br/>
     * 多值取或<br/>
     * 在value不予赋值（即不需要资源权限，仅需要登录系统便可访问的情况下），通过此字段来限定哪个系统客户访问
     */
    SystemType[] system() default {SystemType.OPER, SystemType.CLIENT};

}
