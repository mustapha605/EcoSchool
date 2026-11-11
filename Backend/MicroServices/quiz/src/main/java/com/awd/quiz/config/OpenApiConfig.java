package com.awd.quiz.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI quizOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Quiz & Educational Content Microservice API")
                        .description("Microservice managing interactive eco-quizzes and daily eco-tips")
                        .version("1.0.0")
                        .contact(new Contact().name("AWD Team ESPRIT")));
    }
}
