package com.lrpcn.quickdev.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 功能: 限流
 * 作者: lrpcn
 * 日期: 2024/2/11 2:14
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MyRedisLimiter {

    String key() default "";

    long qps() default 10L;
}
