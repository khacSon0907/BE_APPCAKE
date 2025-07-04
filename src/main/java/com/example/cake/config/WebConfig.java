//package com.example.cake.config;
//
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@Configuration
//public class WebConfig implements WebMvcConfigurer {
//
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        // Avatar user: http://localhost:8080/static/avatars/abc.jpg
//        registry.addResourceHandler("/static/avatars/**")
//                .addResourceLocations("file:uploads/avatars/");
//        //  Ảnh sản phẩm: http://localhost:8080/static/products/abc.jpg
//        registry.addResourceHandler("/static/products/**")
//                .addResourceLocations("file:uploads/products/");
//
//    }
//}

package com.example.cake.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${app.upload-dir}")
    private String uploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/avatars/**")
                .addResourceLocations("file:" + uploadDir + "avatars/");

        registry.addResourceHandler("/static/products/**")
                .addResourceLocations("file:" + uploadDir + "products/");
    }
}

