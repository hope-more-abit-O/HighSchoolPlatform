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
    private final StudentTokenRepository studentTokenRepository;
    private final AdminTokenRepository adminTokenRepository;
    private final StaffTokenRepository staffTokenRepository;
    private final ConsultantTokenRepository consultantTokenRepository;
    private final UniversityTokenRepository universityTokenRepository;

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
            boolean checkAdminTokenExisted = isAdminTokenValid(jwt);
            boolean checkStaffTokenExisted = isStaffTokenValid(jwt);
            boolean checkUniversityTokenExisted = isUniversityTokenValid(jwt);
            boolean checkConsultantTokenExisted = isConsultantTokenValid(jwt);
            boolean checkStudentTokenExisted = isStudentTokenValid(jwt);
            if (!checkStaffTokenExisted && !checkAdminTokenExisted && !checkUniversityTokenExisted
                    && !checkConsultantTokenExisted && !checkStudentTokenExisted) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                response.getWriter().write("{\"message\": \"Token is not valid\"}");
            } else {
                response.setStatus(HttpStatus.OK.value());
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                response.getWriter().write("{\"message\": \"Logout successful\"}");
            }
        } catch (Exception ex) {
            log.error("Error when log out {}", ex.getMessage());
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    private boolean isStudentTokenValid(String jwt) {
        var studentToken = studentTokenRepository.findByTokenStudent(jwt)
                .orElse(null);
        if (studentToken != null) {
            studentToken.setExpired(true);
            studentToken.setRevoked(true);
            studentTokenRepository.save(studentToken);
            return true;
        }
        return false;
    }

    private boolean isStaffTokenValid(String jwt) {
        var staffToken = staffTokenRepository.findByStaffToken(jwt)
                .orElse(null);
        if (staffToken != null) {
            staffToken.setExpired(true);
            staffToken.setRevoked(true);
            staffTokenRepository.save(staffToken);
            return true;
        }
        return false;
    }

    private boolean isAdminTokenValid(String jwt) {
        var adminToken = adminTokenRepository.findByAdminToken(jwt)
                .orElse(null);
        if (adminToken != null) {
            adminToken.setExpired(true);
            adminToken.setRevoked(true);
            adminTokenRepository.save(adminToken);
            return true;
        }
        return false;
    }

    private boolean isConsultantTokenValid(String jwt) {
        var consultantToken = consultantTokenRepository.findByConsultantToken(jwt)
                .orElse(null);
        if (consultantToken != null) {
            consultantToken.setExpired(true);
            consultantToken.setRevoked(true);
            consultantTokenRepository.save(consultantToken);
            return true;
        }
        return false;
    }

    private boolean isUniversityTokenValid(String jwt) {
        var universityToken = universityTokenRepository.findByUniversityToken(jwt)
                .orElse(null);
        if (universityToken != null) {
            universityToken.setExpired(true);
            universityToken.setRevoked(true);
            universityTokenRepository.save(universityToken);
            return true;
        }
        return false;
    }
}