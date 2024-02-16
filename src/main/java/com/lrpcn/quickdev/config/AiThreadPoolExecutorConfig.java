package com.lrpcn.quickdev.config;

import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 功能: 自定义线程池配置
 * 作者: lrpcn
 * 日期: 2024/2/12 17:15
 */
@Configuration
public class AiThreadPoolExecutorConfig {

    @Bean(name = "aiThreadPoolExecutor")
    public ThreadPoolExecutor aiThreadPoolExecutor() {
        // 创建线程工厂
        ThreadFactory threadFactory = new ThreadFactory() {
            // 初始化线程线程数 1
            private int count = 1;

            @Override
            public Thread newThread(@NotNull Runnable r) {
                Thread thread = new Thread(r);
                thread.setName("线程" + count);
                count++;
                return thread;
            }
        };
        // 核心线程设置为2，最大线程设置为4，非核心线程空闲时间设置为100秒，任务队列为阻塞队列
        return new ThreadPoolExecutor(2, 4, 100, TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(4), threadFactory);
    }
}
