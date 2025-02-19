package com.kdt_proj2_be.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CustomConfig implements WebMvcConfigurer {


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        // http://localhost:8080/images/123.png 같은 형식의 이미지 요청시 처리 핸들러 등록

        // jar을 생성할 때 이미 저장된 리소스 호출
        // classpath ==> .jar/BOOT-INF/classes/ 를 의미
        // 이클립스 Package Explorer에서는 src/main/resources를 의미
        registry.addResourceHandler("/images/**")
//                .addResourceLocations("classpath:static/images/","file:C:/DevWorkspaces/kdt_proj2_be_images/");
                .addResourceLocations("file:C:/DevWorkspaces/kdt_proj2_be_images/");

        // 특정 폴더에 저장되어 있는 리소스 호출
        registry.addResourceHandler("/images1/**")
                .addResourceLocations("file:c:/Temp/upload1/");
    }
}