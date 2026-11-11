package com.awd.challenge.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI challengeOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Challenge Microservice API")
                        .description("Microservice managing eco-challenges (Waste reduction, Energy saving) and class submissions")
                        .version("1.0.0")
                        .contact(new Contact().name("AWD Team ESPRIT")));
    }
}
