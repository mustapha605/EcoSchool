package com.awd.user;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
class SecurityConfig {
  @Bean SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    return http.csrf(csrf -> csrf.disable()).authorizeHttpRequests(auth -> auth
        .requestMatchers("/actuator/**", "/v3/api-docs/**", "/swagger-ui/**", "/api/users/health").permitAll()
        .anyRequest().authenticated()).oauth2ResourceServer(oauth -> oauth.jwt()).build();
  }
}
