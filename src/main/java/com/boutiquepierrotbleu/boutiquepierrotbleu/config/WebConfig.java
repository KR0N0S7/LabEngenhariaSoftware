package com.boutiquepierrotbleu.boutiquepierrotbleu.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/chat") // Or a broader mapping if needed
                .allowedOrigins("http://localhost:8080")
                .allowedMethods("POST"); // Adjust allowed methods if necessary
    }
}
