package com.lrpcn.quickdev.aop;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.StopWatch;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 功能: 请求响应日志记录
 * 作者: lrpcn
 * 日期: 2024/2/10 23:34
 */
@Aspect
@Component
@Slf4j
public class LogInterceptor {

    // todo 日志没有脱敏
    @Around("execution(* com.lrpcn.quickdev.controller.*.*(..))")
    public Object doInterceptor(ProceedingJoinPoint point) throws Throwable {
        StopWatch stopWatch = new StopWatch();
        // 开始计时
        stopWatch.start();
        // 获取请求路径
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        HttpServletRequest httpServletRequest = ((ServletRequestAttributes) requestAttributes).getRequest();
        // uuid确保唯一性
        String requestId = IdUtil.fastSimpleUUID();
        String url = httpServletRequest.getRequestURI();
        Object[] args = point.getArgs();
        for (Object o : args) {

        }

        String reqParam = "[" + StrUtil.join(",", args) + "]";
        // 日志打印
        log.info("请求开始，请求ID: {}，请求路径: {}，ip: {} 请求参数: {}",
                requestId, url, httpServletRequest.getRemoteHost(), reqParam);
        Object result = point.proceed();
        // 日志打印结束
        stopWatch.stop();
        long totalTimeMillis = stopWatch.getTotalTimeMillis();
        log.info("请求结束，请求ID: {}，执行时间: {}ms", requestId, totalTimeMillis);
        return result;
    }
}
