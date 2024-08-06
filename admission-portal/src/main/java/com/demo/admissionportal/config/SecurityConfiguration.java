package com.demo.admissionportal.config;

import com.demo.admissionportal.config.authentication.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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
    private static final String CHAT_API = "/api/v1/chat-user/**";
    private static final String POST_API = "/api/v1/post/**";
    private static final String ADDRESS_API = "/api/v1/address/**";
    private static final String TEST_API = "/test/**";
    private static final String FILE_API = "/api/v1/file/**";
    private static final String STUDENT_REPORT = "/api/v1/student-report/**";
    private static final String COMMENT_API = "api/v1/comment/**";
    private static final String REPORTS_API = "/api/v1/reports/**";
    private static final String MAJOR_API = "/api/v1/major/**";
    private static final String METHOD_API = "/api/v1/method/**";
    private static final String SEARCH_API = "/api/v1/search/**";
    private static final String UNIVERSITY_CAMPUS_API = "/api/v1/university-campus/**";
    private static final String ADMISSION_API = "/api/v1/admission/**";
    private static final String EXAM_SCORE_API = "/api/v1/exam-scores/**";
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        req -> req
                                .requestMatchers(USER_API).hasAnyAuthority("STAFF", "USER")
                                .requestMatchers(STAFF_API).hasAuthority("STAFF")
                                .requestMatchers(ADMIN_API).hasAuthority("ADMIN")
                                .requestMatchers(STUDENT_REPORT, REPORTS_API).hasAnyAuthority("USER","STAFF")
                                .requestMatchers(CHAT_API).hasAnyAuthority("STAFF", "USER", "CONSULTANT")
                                .requestMatchers(CREATE_UNI_REQUEST_API).hasAnyAuthority("STAFF", "ADMIN")
                                .requestMatchers(UNIVERSITY_API).hasAnyAuthority("STAFF", "ADMIN", "UNIVERSITY")
                                .requestMatchers(CONSULTANT_API).hasAnyAuthority("STAFF", "ADMIN", "UNIVERSITY", "CONSULTANT")
                                .requestMatchers(HttpMethod.GET,MAJOR_API).permitAll()
                                .requestMatchers(MAJOR_API).hasAnyAuthority("STAFF", "ADMIN")
                                .requestMatchers(HttpMethod.GET,METHOD_API).permitAll()
                                .requestMatchers(METHOD_API).hasAnyAuthority("STAFF", "ADMIN")
                                .requestMatchers(UNIVERSITY_CAMPUS_API).hasAuthority("UNIVERSITY")
                                .requestMatchers(ADMISSION_API).hasAnyAuthority("ADMIN", "STAFF", "UNIVERSITY", "CONSULTANT")
                                .requestMatchers(AUTHENTICATION_API,
                                        EXAM_SCORE_API,
                                        COMMENT_API,
                                        TEST_API,
                                        ADDRESS_API,
                                        POST_API,
                                        FILE_API,
                                        MAJOR_API,
                                        METHOD_API,
                                        SEARCH_API,
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
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173", "https://main--uap-portal.netlify.app")); // Change to your front-end origin
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
        configuration.setAllowCredentials(true); // Allow credentials
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public CorsFilter corsFilter() {
        return new CorsFilter(corsConfigurationSource());
    }
}
