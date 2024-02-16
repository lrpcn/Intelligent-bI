package com.lrpcn.quickdev.aop;

import com.google.common.util.concurrent.RateLimiter;
import com.lrpcn.quickdev.annotation.MyRateLimiter;
import com.lrpcn.quickdev.common.ErrorCodeEnum;
import com.lrpcn.quickdev.common.ResultResponse;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 功能: 基于Guava的限流器
 * 作者: lrpcn
 * 日期: 2024/2/9 10:53
 */
@Slf4j
@Component
@Aspect
@SuppressWarnings({"all"})
public class RateLimiterAspect {

    public Map<String, RateLimiter> map = new ConcurrentHashMap<>();

    /**
     * 功能：aop round处理限流
     * @param joinPoint
     * @param myRateLimiter
     * @return
     * @throws Throwable
     */
    @Around("@annotation(myRateLimiter)")
    public Object rateLimiterRound(ProceedingJoinPoint joinPoint, MyRateLimiter myRateLimiter) throws Throwable {
        long warmupPeriod = myRateLimiter.warmupPeriod();
        double qps = myRateLimiter.qps();
        RateLimiter rateLimiter = null;
        if (warmupPeriod <= 0) {
            rateLimiter = RateLimiter.create(qps);
        } else {
            rateLimiter = RateLimiter.create(qps, warmupPeriod, myRateLimiter.timeUnit());
        }
        RateLimiter limiter = map.putIfAbsent(myRateLimiter.resourceId(), rateLimiter);
        if (limiter.tryAcquire()) {
            return joinPoint.proceed();
        } else {
            log.warn("请求过于频繁");
            return ResultResponse.error(ErrorCodeEnum.TOO_MANY_REQUEST);
        }
    }

}
