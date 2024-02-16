package com.lrpcn.quickdev;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@MapperScan("com.lrpcn.quickdev.mapper")
@SpringBootApplication
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
public class QuickDevApplication {

    public static void main(String[] args) {
        SpringApplication.run(QuickDevApplication.class, args);
    }

}
