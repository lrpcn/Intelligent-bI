package com.lrpcn.quickdev.config;

import io.github.briqt.spark4j.SparkClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 功能:
 * 作者: lrpcn
 * 日期: 2024/2/14 22:29
 */
@Configuration
public class AiConfig {

    @Value("${xunfei.client.appid}")
    private String appid;

    @Value("${xunfei.client.apiKey}")
    private String apiKey;

    @Value("${xunfei.client.apiSecret}")
    private String apiSecret;

    @Bean
    public SparkClient sparkClient() {
        SparkClient sparkClient = new SparkClient();
        sparkClient.appid = appid;
        sparkClient.apiKey = apiKey;
        sparkClient.apiSecret = apiSecret;
        return sparkClient;
    }
}
