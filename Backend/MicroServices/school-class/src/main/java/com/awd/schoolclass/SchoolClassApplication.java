package com.awd.schoolclass;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class SchoolClassApplication {
    public static void main(String[] args) {
        SpringApplication.run(SchoolClassApplication.class, args);
    }
}
