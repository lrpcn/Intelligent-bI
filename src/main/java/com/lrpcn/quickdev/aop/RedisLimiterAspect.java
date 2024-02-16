package com.lrpcn.quickdev.aop;

import com.lrpcn.quickdev.annotation.MyRedisLimiter;
import com.lrpcn.quickdev.common.ErrorCodeEnum;
import com.lrpcn.quickdev.common.ResultResponse;
import com.lrpcn.quickdev.manager.RedisLimiterManager;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 功能: 限流aop实现
 * 作者: lrpcn
 * 日期: 2024/2/11 2:14
 */
@Aspect
@Component
public class RedisLimiterAspect {

    @Resource
    private RedisLimiterManager redisLimiterManager;

    @Around("@annotation(myRedisLimiter)")
    public Object redisLimiterRound(ProceedingJoinPoint joinPoint, MyRedisLimiter myRedisLimiter) throws Throwable {
        String key = myRedisLimiter.key();
        long qps = myRedisLimiter.qps();
        if (redisLimiterManager.doRedisLimit(key, qps)) {
            return joinPoint.proceed();
        }
        return ResultResponse.error(ErrorCodeEnum.TOO_MANY_REQUEST);
    }

}
