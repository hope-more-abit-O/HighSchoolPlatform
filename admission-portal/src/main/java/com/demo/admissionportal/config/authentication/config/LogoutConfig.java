package com.demo.admissionportal.config.authentication.config;

import com.demo.admissionportal.repository.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;


/**
 * The type Logout student config.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class LogoutConfig implements LogoutHandler {
    private final UserTokenRepository userTokenRepository;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        try {
            final String authHeader = request.getHeader("Authorization");
            final String jwt;
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                response.setStatus(HttpStatus.BAD_REQUEST.value());
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                response.getWriter().write("{\"message\": \"Authorization header is missing or invalid\"}");
                return;
            }
            jwt = authHeader.substring(7);
            if (!isUserTokenValid(jwt)) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                response.getWriter().write("{\"message\": \"Token is not valid\"}");
            } else {
                response.setStatus(HttpStatus.OK.value());
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                response.getWriter().write("{\"message\": \"Logout successfully\"}");
            }
        } catch (Exception ex) {
            log.error("Error when log out {}", ex.getMessage());
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    private boolean isUserTokenValid(String jwt) {
        var userToken = userTokenRepository.findByToken(jwt)
                .orElse(null);
        if (userToken != null) {
            userToken.setExpired(true);
            userToken.setRevoked(true);
            userTokenRepository.save(userToken);
            return true;
        }
        return false;
    }
}
