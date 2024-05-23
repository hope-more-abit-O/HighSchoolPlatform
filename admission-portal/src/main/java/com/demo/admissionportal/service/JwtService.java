package com.demo.admissionportal.service;

import org.springframework.security.core.userdetails.UserDetails;

/**
 * The interface Jwt service.
 */
public interface JwtService {
    /**
     * Extract username string.
     *
     * @param token the token
     * @return the string
     */
    String extractUsername(String token);

    /**
     * Is token valid boolean.
     *
     * @param token       the token
     * @param userDetails the user details
     * @return the boolean
     */
    boolean isTokenValid(String token, UserDetails userDetails);

    /**
     * Generate token string.
     *
     * @param userDetails the user details
     * @return the string
     */
    String generateToken(UserDetails userDetails);

    /**
     * Generate refresh token string.
     *
     * @param userDetails the user details
     * @return the string
     */
    String generateRefreshToken(UserDetails userDetails);
}
