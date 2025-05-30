package com.example.cake.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // cho phép mọi path
                        .allowedOriginPatterns("http://localhost:5173") // KHÔNG dùng allowedOrigins nữa
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // cho phép các method
                        .allowedHeaders("*") // cho phép mọi header (gồm Authorization)
                        .exposedHeaders("Authorization") // cho phép FE đọc được header này nếu cần
                        .allowCredentials(true) // để gửi cookie hoặc header Authorization
                        .maxAge(3600); // cache preflight trong 1h
            }
        };
    }
}
