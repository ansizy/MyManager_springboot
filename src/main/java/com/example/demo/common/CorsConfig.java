package com.example.demo.common;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * 跨域配置
 */
@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration();

        // 允许所有来源
        corsConfiguration.addAllowedOrigin("*");

        // 允许所有头部信息
        corsConfiguration.addAllowedHeader("*");

        // 允许所有HTTP方法
        corsConfiguration.addAllowedMethod("*");

        // 配置对所有路径生效
        source.registerCorsConfiguration("/**", corsConfiguration);

        return new CorsFilter(source);
    }
}


