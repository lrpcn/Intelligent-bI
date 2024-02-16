package com.lrpcn.quickdev.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 功能:
 * 作者: lrpcn
 * 日期: 2024/1/14 15:12
 */

@Data
@Configuration
@ConfigurationProperties(prefix = "alioos")
public class AliOOSConfig {

    private String endpoint;

    private String accessKeyId;

    private String accessKeySecret;

    private String bucketName;

    public static final String JPEG = ".jpeg";

    public static final String HTTPS = "https://";
}
