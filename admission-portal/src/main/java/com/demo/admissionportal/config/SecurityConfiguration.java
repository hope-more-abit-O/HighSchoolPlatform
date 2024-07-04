package com.demo.admissionportal.config;

import com.demo.admissionportal.config.authentication.filter.JwtAuthenticationFilter;
import com.demo.admissionportal.constants.Role;
import com.demo.admissionportal.entity.User;
import com.demo.admissionportal.repository.UserRepository;
import com.demo.admissionportal.service.UserService;
import com.demo.admissionportal.service.oauth2.CustomOAuth2UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import java.io.IOException;
import java.util.Map;

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
    private final UserService userService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CustomOAuth2UserService customOAuth2UserService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        req -> req
                                .requestMatchers(USER_API).hasAnyAuthority("STAFF","USER")
                                .requestMatchers(STAFF_API).hasAuthority("STAFF")
                                .requestMatchers(ADMIN_API).hasAuthority("ADMIN")
                                .requestMatchers(AUTHENTICATION_API,
                                        "/account/**",
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
                                        "/api/v1/file/**"
                                )
                                .permitAll()
                                .anyRequest()
                                .authenticated())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(STATELESS))
                .authenticationProvider(authenticationProvider)
                .oauth2Login(oauth -> oauth
                        .authorizationEndpoint(authorization -> authorization
                                .baseUri("/oauth2/authorization"))
                        .redirectionEndpoint(redirection -> redirection
                                .baseUri("/login/oauth2/code/*"))
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService))
                        .successHandler(authenticationSuccessHandler())
                        .failureHandler(authenticationFailureHandler()))
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .logout(
                        log -> log.logoutUrl(AUTHENTICATION_API + "/logout")
                                .addLogoutHandler(logoutHandler)
                                .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext()));

        return http.build();
    }

    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder);
    }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        SimpleUrlAuthenticationFailureHandler failureHandler = new SimpleUrlAuthenticationFailureHandler("/signup");
        failureHandler.setUseForward(true);
        return failureHandler;
    }


    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new AuthenticationSuccessHandler() {
            private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

            @Override
            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
                String targetUrl = determineTargetUrl(authentication);
                redirectStrategy.sendRedirect(request, response, targetUrl);
            }

            private String determineTargetUrl(Authentication authentication) {
                DefaultOidcUser oidcUser = (DefaultOidcUser) authentication.getPrincipal();
                String email = oidcUser.getEmail();
                User user = userRepository.findByEmail(email)
                        .orElseGet(() -> {
                            // Register and save a new user if not found
                            Map<String, Object> attributes = oidcUser.getAttributes();
                            return customOAuth2UserService.registerNewUser(attributes);
                        });

                System.out.println("User found or created: " + user.getEmail());
                return "/api/v1/user/profile/" + user.getId();
            }
        };
    }
}