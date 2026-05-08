package com.memo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

/**
 * Web 配置 - 静态资源映射
 * <p>
 * 将 /api/v1/files/** 路径映射到本地文件上传目录，
 * 使上传的头像和备忘录图片可以通过 HTTP 访问。
 * </p>
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${file.upload-path}")
    private String uploadPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 确保路径以 / 结尾
        String location = Paths.get(uploadPath).toUri().toString();
        registry.addResourceHandler("/api/v1/files/**")
                .addResourceLocations(location);
    }
}
