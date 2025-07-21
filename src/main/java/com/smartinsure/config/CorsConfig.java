package com.smartinsure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

        @Override
        public void addCorsMappings(CorsRegistry registry) {
            registry.addMapping("/**") // Apply CORS to all paths in this service
                    .allowedOrigins("http://localhost:3000") // Allow your React frontend's origin
                    .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD") // Allow all necessary HTTP methods, including OPTIONS
                    .allowedHeaders("*") // Allow all headers (e.g., Content-Type, Authorization)
                    .allowCredentials(true) // Allow sending cookies/authentication headers
                    .maxAge(3600); // Max age of preflight request cache in seconds (optional)
        }
    }
