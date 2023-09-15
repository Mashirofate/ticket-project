package com.tickets.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 权限校验接口
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Authentication {

    /**
     *  是否不需要验证验证登入就可以访问
     *
     * @return
     */
    boolean required() default true;

    /**
     * 必须登录
     *
     * @return
     */
    boolean isLogin() default false;

    /**
     * 是否需要获取用户的信息
     * 注意：想要获取用户信息，必须登录
     *
     * @return
     */
    boolean isRequiredUserInfo() default false;

    /**
     * 角色验证（暂时未做）
     *
     * @return
     */
    int authority() default 0;
}
