package com.demo.admissionportal.config.authentication.filter;

import com.demo.admissionportal.repository.*;
import com.demo.admissionportal.service.JwtService;
import io.micrometer.common.lang.NonNull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


/**
 * The type Jwt authentication student filter.
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final StudentTokenRepository studentTokenRepository;
    private final StaffTokenRepository staffTokenRepository;
    private final AdminTokenRepository adminTokenRepository;
    private final ConsultantTokenRepository consultantTokenRepository;
    private final UniversityTokenRepository universityTokenRepository;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        jwt = authHeader.substring(7);
        userEmail = jwtService.extractUsername(jwt);
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
            boolean checkToken = isTokenValid(jwt);
            if (jwtService.isTokenValid(jwt, userDetails) && checkToken) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }

    public boolean isTokenValid(String jwt) {
        return isStudentTokenValid(jwt) ||
                isStaffTokenValid(jwt) ||
                isAdminTokenValid(jwt) ||
                isConsultantTokenValid(jwt) ||
                isUniversityTokenValid(jwt);
    }

    private boolean isStudentTokenValid(String jwt) {
        return studentTokenRepository.findByTokenStudent(jwt)
                .map(t -> !t.isExpired() && !t.isRevoked())
                .orElse(false);
    }

    private boolean isStaffTokenValid(String jwt) {
        return staffTokenRepository.findByStaffToken(jwt)
                .map(t -> !t.isExpired() && !t.isRevoked())
                .orElse(false);
    }

    private boolean isAdminTokenValid(String jwt) {
        return adminTokenRepository.findByAdminToken(jwt)
                .map(t -> !t.isExpired() && !t.isRevoked())
                .orElse(false);
    }

    private boolean isConsultantTokenValid(String jwt) {
        return consultantTokenRepository.findByConsultantToken(jwt)
                .map(t -> !t.isExpired() && !t.isRevoked())
                .orElse(false);
    }

    private boolean isUniversityTokenValid(String jwt) {
        return universityTokenRepository.findByUniversityToken(jwt)
                .map(t -> !t.isExpired() && !t.isRevoked())
                .orElse(false);
    }
}
