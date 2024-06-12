package com.demo.admissionportal.config;

import com.demo.admissionportal.repository.StudentTokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;


/**
 * The type Logout student config.
 */
@Service
@RequiredArgsConstructor
public class LogoutStudentConfig implements LogoutHandler {
    private final StudentTokenRepository studentTokenRepository;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }
        jwt = authHeader.substring(7);
        var storedToken = studentTokenRepository.findByTokenStudent(jwt)
                .orElse(null);
        if (storedToken != null) {
            storedToken.setExpired(true);
            storedToken.setRevoked(true);
            studentTokenRepository.save(storedToken);
        }
    }
}
