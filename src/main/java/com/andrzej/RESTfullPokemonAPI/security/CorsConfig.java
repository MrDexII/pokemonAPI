package com.andrzej.RESTfullPokemonAPI.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;

@Configuration
public class CorsConfig {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry
                        .addMapping("/**")
                        .allowedOriginPatterns("*")
                        .allowedHeaders("Origin", "Content-Type", "Accept", "responseType", "Authorization")
                        .allowedMethods("GET", "POST", "PUT", "OPTIONS", "DELETE", "PATCH")
                        .exposedHeaders("Authorization")
                        .allowCredentials(true);
            }
        };
    }
}
