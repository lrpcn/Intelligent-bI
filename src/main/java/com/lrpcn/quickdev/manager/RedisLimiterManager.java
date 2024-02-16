package com.lrpcn.quickdev.manager;

import com.lrpcn.quickdev.common.ErrorCodeEnum;
import com.lrpcn.quickdev.exception.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RRateLimiter;
import org.redisson.api.RateIntervalUnit;
import org.redisson.api.RateType;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 功能:  基于redisson的限流
 * 作者: lrpcn
 * 日期: 2024/2/11 1:49
 */
@Service
@Slf4j
public class RedisLimiterManager {

    @Resource
    private RedissonClient redissonClient;

    /**
     * 限流
     *
     * @param key 限流的key
     */
    public void doRedisLimit(String key) {
        RRateLimiter rateLimiter = redissonClient.getRateLimiter(key);
        rateLimiter.trySetRate(RateType.OVERALL, 2, 1, RateIntervalUnit.SECONDS);
        if (!rateLimiter.tryAcquire(1)) {
            throw new CustomException(ErrorCodeEnum.TOO_MANY_REQUEST);
        }
    }

    /**
     * 限流
     *
     * @param key  限流的key
     * @param qps 限流的大小
     */
    public boolean doRedisLimit(String key, long qps) {
        RRateLimiter rateLimiter = redissonClient.getRateLimiter(key);
        rateLimiter.trySetRate(RateType.OVERALL, qps, 1, RateIntervalUnit.SECONDS);
        return rateLimiter.tryAcquire(1);
    }
}
