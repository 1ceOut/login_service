package com.icebuckwheat.oauthserver.Config;

import jakarta.ws.rs.HttpMethod;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("http://localhost:8080","https://reacticeout.icebuckwheat.kro.kr")
                .allowedMethods(
                        HttpMethod.GET,
                        HttpMethod.HEAD,
                        HttpMethod.POST,
                        HttpMethod.PUT,
                        HttpMethod.DELETE
                )
                .allowCredentials(true) // 이 부분이 중요합니다.
                .allowedHeaders("*");
    }
}