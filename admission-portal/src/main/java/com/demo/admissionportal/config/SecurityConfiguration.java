package com.demo.admissionportal.config;

import com.demo.admissionportal.config.authentication.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private final LogoutHandler logoutHandler;
    private static final String AUTHENTICATION_API = "/api/v1/auth/**";
    private static final String USER_API = "/api/v1/user/**";
    private static final String STAFF_API = "/api/v1/staff/**";
    private static final String ADMIN_API = "/api/v1/admin/**";
    private static final String CREATE_UNI_REQUEST_API = "/api/v1/create-university/**";
    private static final String UNIVERSITY_API = "/api/v1/university/**";
    private static final String CONSULTANT_API = "/api/v1/consultant/**";
    private static final String CHATBOT = "/api/v1/chatbot/**";
    private static final String CHATUSER = "/api/v1/chat-user/**";
    private static final String POST_API = "/api/v1/post/**";
    private static final String ADDRESS_API = "/api/v1/address/**";
    private static final String TEST_API = "/test/**";
    private static final String FILE_API = "/api/v1/file/**";

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        req -> req
                                .requestMatchers(USER_API).hasAnyAuthority("STAFF", "USER")
                                .requestMatchers(STAFF_API).hasAuthority("STAFF")
                                .requestMatchers(ADMIN_API).hasAuthority("ADMIN")
                                .requestMatchers(CREATE_UNI_REQUEST_API).hasAnyAuthority("STAFF", "ADMIN")
                                .requestMatchers(UNIVERSITY_API).hasAnyAuthority("STAFF", "ADMIN", "UNIVERSITY")
                                .requestMatchers(CONSULTANT_API).hasAnyAuthority("STAFF", "ADMIN", "UNIVERSITY", "CONSULTANT")
//                                .requestMatchers(POST_API).hasAnyAuthority("STAFF", "CONSULTANT")
                                .requestMatchers(AUTHENTICATION_API, CHATBOT,
                                        TEST_API,
                                        ADDRESS_API,
                                        POST_API,
                                        FILE_API,
                                        CHATUSER,
                                        "/v2/api-docs",
                                        "/v3/api-docs",
                                        "/v3/api-docs/**",
                                        "/swagger-resources",
                                        "/swagger-resources/**",
                                        "/configuration/ui",
                                        "/configuration/security",
                                        "/swagger-ui/**",
                                        "/webjars/**",
                                        "/swagger-ui.html",
                                        "/api/v1/file/**",
                                        "/ws/**"
                                )
                                .permitAll()
                                .anyRequest()
                                .authenticated())
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .logout(
                        log -> log.logoutUrl(AUTHENTICATION_API + "/logout")
                                .addLogoutHandler(logoutHandler)
                                .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext()));

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("https://uaportal.online"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public CorsFilter corsFilter() {
        return new CorsFilter(corsConfigurationSource());
    }
}
