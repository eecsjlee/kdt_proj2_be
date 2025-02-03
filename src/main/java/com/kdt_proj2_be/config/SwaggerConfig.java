package com.kdt_proj2_be.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(apiInfo());
    }

    private Info apiInfo() {
        return new Info()
                .title("Data Fetching and Management API")
                .description("이 프로젝트의 REST API는 다양한 데이터를 효율적으로 조회하고 관리하는 기능을 제공합니다.")
                .contact(new Contact()
                        .name("eecsjlee")
                        .email("eecsjlee@gmail.com")
                        .url("https://github.com/eecsjlee")
                );
    }
}
