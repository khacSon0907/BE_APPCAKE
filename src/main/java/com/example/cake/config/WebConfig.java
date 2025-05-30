package com.example.cake.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Khi gọi: http://localhost:8080/static/avatars/abc.jpg
        registry.addResourceHandler("/static/avatars/**")
                .addResourceLocations("file:uploads/avatars/");
    }
}
