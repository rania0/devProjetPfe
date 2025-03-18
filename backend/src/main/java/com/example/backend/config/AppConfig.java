package com.example.backend.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;



@Configuration
public class AppConfig {

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @PostConstruct
    public void init() {
        System.out.println("✅ JWT SECRET FROM CONFIG: " + jwtSecret);
    }
}
