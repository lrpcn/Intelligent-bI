package com.lrpcn.quickdev.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * 功能:
 * 作者: lrpcn
 * 日期: 2024/1/14 18:16
 */
@Configuration
public class Knife4jConfig {

    @Bean
    public Docket docket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfoBuilder()
                        .title("接口文档")
                        .description("接口")
                        .version("1")
                        .build())
                .groupName("def")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.lrpcn.quickdev.controller"))
                .paths(PathSelectors.any())
                .build();
    }
}
