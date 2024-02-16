package com.lrpcn.quickdev.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * 功能: 接口限流
 * 作者: lrpcn
 * 日期: 2024/2/9 10:49
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MyRateLimiter {

    /**
     * 资源id
     * @return
     */
    String resourceId() default "";

    /**
     * 令牌生成速度
     * @return
     */
    double qps() default 1.0d;

    /**
     * 预热时间
     * @return
     */
    long warmupPeriod() default 0L;

    /**
     * 预热时间单位
     * @return
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;
}
