package com.kdt_proj2_be.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CustomConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 차량 번호판
        registry.addResourceHandler("/registerCar/**")
                .addResourceLocations("classpath:static/images/","file:c:/DevWorkspaces/kdt_proj2_be/src/main/resources/static/images");
    }
}