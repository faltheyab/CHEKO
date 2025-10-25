package com.faisal.cheko.config;

import com.faisal.cheko.interceptor.RequestInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.List;


@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final RequestInterceptor requestInterceptor;
    
    private final List<String> SECURED_PATHS = Arrays.asList(
        "/api/**",              // API endpoints
        "/swagger-ui.html",     // Swagger UI HTML page
        "/swagger-ui/**",       // Swagger UI resources
        "/api-docs/**",         // OpenAPI documentation
        "/v3/api-docs/**"       // OpenAPI v3 documentation
    );
    
    private final String[] ALLOWED_ORIGINS = {
        "http://localhost:3000", "https://localhost:3000",
        "http://localhost:8080", "https://localhost:8080",
        "http://127.0.0.1:3000", "https://127.0.0.1:3000",
        "http://127.0.0.1:8080", "https://127.0.0.1:8080",
        "https://0.0.0.0:8080", "http://0.0.0.0:8080",
        "http://cheko.fae.sale","https://cheko.fae.sale"
    };
    
    private final String[] ALLOWED_METHODS = {
        "GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH", "HEAD"
    };
    
    private final long MAX_AGE = 3600; // 1 hour

    @Autowired
    public WebConfig(RequestInterceptor requestInterceptor) {
        this.requestInterceptor = requestInterceptor;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        for (String path : SECURED_PATHS) {
            registry.addMapping(path)
                    .allowedOrigins(ALLOWED_ORIGINS)
                    .allowedMethods(ALLOWED_METHODS)
                    .allowedHeaders("*")
                    .allowCredentials(true)
                    .maxAge(MAX_AGE);
        }
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(requestInterceptor)
                .addPathPatterns("/api/**")  // Apply to all API endpoints
                .excludePathPatterns("/api/health"); // Exclude health check endpoint
    }
}