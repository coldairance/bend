package com.blog.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    // 图片访问路径
    @Value("${picture.mask}")
    private String maskPath_pic;

    // 图片真实路径
    // 用户信息图片路径
    @Value("${picture.real.info}")
    private String info;

    // 普通图片路径
    @Value("${picture.real.common}")
    private String common;

    // 处理路径
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 注意加 /**
        registry
                .addResourceHandler(maskPath_pic+"/**")
                // 添加图片真实路径
                .addResourceLocations("file:" + info,"file:" + common);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowCredentials(true)
                // 允许所有域名访问
                .allowedOriginPatterns("*")
                .allowedMethods("POST","GET")
                .allowedHeaders("*")
                .maxAge(3600);
    }
}
