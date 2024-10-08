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
    private static final String UNIVERSITY_TRAINING_PROGRAM = "/api/v1/university-training-program/**";
    private static final String ADMISSION_API = "/api/v1/admission/**";
    private static final String EXAM_SCORE_API = "/api/v1/exam-scores/**";
    private static final String PACKAGE_API = "/api/v1/package/**";
    private static final String FAVORITE_API = "/api/v1/favorite/**";
    private static final String ORDER_API = "/api/v1/order/**";
    private static final String LIKE_API = "/api/v1/like/**";
    private static final String SUBJECT_GROUP_API = "/api/v1/subject-group/**";
    private static final String CHATBOT_API = "/api/v1/chatbot/**";
    private static final String HOLLAND_TEST_API = "/api/v1/holland-test";
    private static final String EXAM_LOCAL = "/api/v1/exam-local/**";
    private static final String SUBJECT_API = "/api/v1/subject/**";
    private static final String STATISTICS_API = "/api/v1/statistics/**";
    private static final String FOLLOW_API = "/api/v1/follow/**";
    private static final String NOTIFICATION_API = "/api/v1/notification/**";

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        req -> req
                                .requestMatchers(HttpMethod.GET,"/api/v1/user/profile/{id}").permitAll()
                                .requestMatchers(USER_API).hasAnyAuthority("STAFF", "USER")

                                .requestMatchers(STAFF_API).hasAuthority("STAFF")

                                .requestMatchers(ADMIN_API).hasAuthority("ADMIN")

                                .requestMatchers(STUDENT_REPORT, REPORTS_API).hasAnyAuthority("USER", "STAFF")

                                .requestMatchers(CHAT_API).hasAnyAuthority("STAFF", "USER", "CONSULTANT")

                                .requestMatchers(CREATE_UNI_REQUEST_API).hasAnyAuthority("STAFF", "ADMIN")

                                .requestMatchers(HttpMethod.GET, "/api/v1/university/info/{id}").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/v1/university").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/v1/university/find-by-province-ids").permitAll()
                                .requestMatchers(UNIVERSITY_API).hasAnyAuthority("STAFF", "ADMIN", "UNIVERSITY", "CONSULTANT")

                                .requestMatchers(CONSULTANT_API).hasAnyAuthority("STAFF", "ADMIN", "UNIVERSITY", "CONSULTANT")

                                .requestMatchers(HttpMethod.GET, "/api/v1/major/all").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/v1/major/available-majors").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/v1/major").permitAll()
                                .requestMatchers(MAJOR_API).hasAnyAuthority("STAFF", "ADMIN")

                                .requestMatchers(HttpMethod.GET, METHOD_API).permitAll()
                                .requestMatchers(METHOD_API).hasAnyAuthority("STAFF", "ADMIN")

                                .requestMatchers(UNIVERSITY_CAMPUS_API).hasAuthority("UNIVERSITY")

                                .requestMatchers(HttpMethod.GET, "/api/v1/admission/search").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/v1/admission/school-directory").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/v1/admission/school-directory-detail").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/v1/admission/score-advice").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/v1/admission/score-advice/v2").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/v1/admission/score-advice/v3").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/v1/admission/a").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/v1/admission/university/{id}/latest-training-program").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/v1/admission").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/v1/admission/{id}").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/v1/admission/v2/{id}").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/v1/admission/v3/{id}").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/v1/admission/score").permitAll()
                                .requestMatchers(HttpMethod.PUT, "/api/v1/admission/score").hasAnyAuthority("UNIVERSITY", "CONSULTANT")
                                .requestMatchers(HttpMethod.PUT, "/api/v1/admission/{id}").hasAnyAuthority("STAFF", "ADMIN", "UNIVERSITY", "CONSULTANT")
                                .requestMatchers(HttpMethod.PUT, "/api/v1/admission/university/{id}").hasAnyAuthority("UNIVERSITY", "CONSULTANT")
                                .requestMatchers(HttpMethod.PUT, "/api/v1/admission/staff/{id}").hasAnyAuthority("STAFF", "ADMIN")
                                .requestMatchers(HttpMethod.POST, "/api/v1/admission/update-admission/**").hasAnyAuthority("CONSULTANT", "UNIVERSITY")
                                .requestMatchers(HttpMethod.POST, "/api/v1/admission/update-admission/{oldAdmissionId}").hasAnyAuthority("CONSULTANT", "UNIVERSITY")
                                .requestMatchers(HttpMethod.GET, "/api/v1/admission/update-admission").hasAnyAuthority("CONSULTANT", "UNIVERSITY", "STAFF", "ADMIN")
                                .requestMatchers(HttpMethod.POST, "/api/v1/admission/forecast").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/v1/admission/compare").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/v1/admission/compare-majors").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/v1/admission/university-got-major/**").permitAll()
                                .requestMatchers(ADMISSION_API).authenticated()

                                .requestMatchers(PACKAGE_API).hasAnyAuthority("ADMIN", "UNIVERSITY", "CONSULTANT")
                                .requestMatchers(ORDER_API).hasAnyAuthority("UNIVERSITY", "CONSULTANT")
                                .requestMatchers(HttpMethod.GET, "/api/v1/subject-group/all").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/v1/subject-group/get-by-major-and-university").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/v1/subject-group/list-all-subject-groups").permitAll()
                                .requestMatchers(SUBJECT_GROUP_API).hasAnyAuthority("STAFF")
                                .requestMatchers(HttpMethod.GET, "/api/v1/university-training-program/info/{university-id}").permitAll()
                                .requestMatchers(UNIVERSITY_TRAINING_PROGRAM).hasAnyAuthority("STAFF", "ADMIN", "UNIVERSITY", "CONSULTANT")
                                .requestMatchers(STATISTICS_API).hasAnyAuthority("UNIVERSITY","ADMIN")
                                .requestMatchers(AUTHENTICATION_API,
                                        NOTIFICATION_API,
                                        EXAM_LOCAL,
                                        HOLLAND_TEST_API,
                                        CHATBOT_API,
                                        EXAM_SCORE_API,
                                        COMMENT_API,
                                        TEST_API,
                                        ADDRESS_API,
                                        POST_API,
                                        FILE_API,
                                        MAJOR_API,
                                        METHOD_API,
                                        SEARCH_API,
                                        FAVORITE_API,
                                        SUBJECT_API,
                                        FOLLOW_API,
                                        LIKE_API,
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
