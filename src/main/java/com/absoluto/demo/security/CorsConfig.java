package com.absoluto.demo.security;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("http://localhost:3000"); // frontend-ul
        config.addAllowedMethod("*"); // Permite toate metodele
        config.addAllowedHeader("*"); // Permite toate anteturile
        config.setAllowCredentials(true); // Permite cookie-uri

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config); // Permite pentru toate endpoint-urile
        return new CorsFilter(source);
    }
}
