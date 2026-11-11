package com.awd.schoolclass.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI schoolClassOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("School & Class Microservice API")
                        .description("Microservice managing Schools, Classes, and Pupils for Eco-awareness platform")
                        .version("1.0.0")
                        .contact(new Contact().name("AWD Team ESPRIT")));
    }
}
