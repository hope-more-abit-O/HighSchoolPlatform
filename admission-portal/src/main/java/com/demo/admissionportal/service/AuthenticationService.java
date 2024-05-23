package com.demo.admissionportal.service;

import com.demo.admissionportal.dto.request.AuthenticationRequest;
import com.demo.admissionportal.dto.request.RegisterRequest;
import com.demo.admissionportal.dto.response.AuthenticationResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * The interface Authentication service.
 */
public interface AuthenticationService {
    /**
     * Register authentication response.
     *
     * @param request the request
     * @return the authentication response
     */
    AuthenticationResponse register(RegisterRequest request);

    /**
     * Authenticate authentication response.
     *
     * @param request the request
     * @return the authentication response
     */
    AuthenticationResponse authenticate(AuthenticationRequest request);

    /**
     * Refresh token.
     *
     * @param request  the request
     * @param response the response
     */
    void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException;
}
