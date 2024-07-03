package com.demo.admissionportal.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

@Configuration
public class JwtConfig {
    @Bean
    public JwtDecoder jwtDecoder() {
        String jwkSetUri = "https://localhost:8080/api/v1/auth/jwks";
        return NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build();
    }
}
