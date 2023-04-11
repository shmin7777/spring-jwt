package com.example.jwt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        // 내 서버가 응답을 할 때 json을 자바스크립트에서 처리할 수 있게 할지를 설정
        // false로 한다면 ajax 등으로 요청해서 처리를 할 때 응답이 오지 않는다.
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*"); // 모든 ip에 응답을 허용
        config.addAllowedHeader("*"); // 모든 header에 응답 허용
        config.addAllowedMethod("*"); // 모든 post, get, put, delete, patch 요청 허용하겠다.
        source.registerCorsConfiguration("/api/**", config); // /api/** 경로에 접글할 때 위의 config를 따르겠다.
        return new CorsFilter(source);
    }
}
