package com.lrpcn.quickdev.config;

import cn.hutool.core.util.ObjUtil;
import lombok.Getter;
import lombok.Setter;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 功能:
 * 作者: lrpcn
 * 日期: 2024/2/7 4:18
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "spring.redis")
public class RedissonConfig {

    /**
     * 数据库几号
     */
    private Integer database;

    /**
     * 地址
     */
    private String host;

    /**
     * 端口号
     */
    private Integer port;

//    /**
//     * 密码
//     */
//    private String password;

    @Bean
    public RedissonClient getRedissonClient() {
        Config config = new Config();
        // 设置单机redisson配置
        if (ObjUtil.hasNull(database, host, port)) {
            database = 0;
            host = "localhost";
            port = 6379;
        }
        config.useSingleServer()
                .setDatabase(database)
                .setAddress("redis://" + host + ":" + port);
        return Redisson.create(config);
    }
}
