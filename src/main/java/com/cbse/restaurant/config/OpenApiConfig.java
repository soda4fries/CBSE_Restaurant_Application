package com.cbse.restaurant.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;

public class OpenApiConfig {

    @Bean
    public OpenAPI springShopOpenAPI() {
        return new OpenAPI()
            .info(
                new Info()
                    .title("Restaurant Management API")
                    .description("A Restaurant Management App Using Spring Developed for WIF3006")
                    .version("v0.0.1")
                    .license(new License().name("Apache 2.0").url("http://springdoc.org"))
            );
    }
}
