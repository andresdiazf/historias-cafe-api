package com.historias_de_cafe.backend.config;

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
                registry.addMapping("/**")
                        .allowedOrigins(
                                "http://localhost:5503",
                                "http://127.0.0.1:5503",
                                "http://127.0.0.1:5500",
                                "http://localhost:5173",
                                "http://127.0.0.1:5173",
                                "https://historias-cafe-client.onrender.com/pages/home/home.html"
                                /*"https://historiasdecafe.github.io",
                                "https://proyecto-historiasdecafe-frontend-vue-1.onrender.com",
                                "https://e-commerce-historias-de-cafe-frontend-r0cz.onrender.com"*/
                        )
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }
}
