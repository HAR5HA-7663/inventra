package com.inventra.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI inventraOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Inventra – Inventory Management API")
                .description("RESTful API for managing products and categories. " +
                             "Supports CRUD operations, pagination, search filtering, and automatic stock status tracking.")
                .version("1.0.0")
                .contact(new Contact()
                    .name("Harsha Vardhan Yellela")
                    .url("https://har5ha.in")
                    .email("harsha.yellela@gmail.com"))
                .license(new License().name("MIT")));
    }
}
