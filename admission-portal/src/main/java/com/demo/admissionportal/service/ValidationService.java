package com.demo.admissionportal.service;

/**
 * Interface defining validation services for the admission portal.
 *
 * @author hopeless
 * @version 1.0
 * @since 18/06/2024
 */
public interface ValidationService {

    /**
     * Validates if the provided username and email are available across all user types.
     *
     * @param username The username to validate.
     * @param email The email to validate.
     * @return {@code false} if the username or email is already taken.
     * @throws Exception If the username or email is already taken.
     */
    boolean validateUsernameAndEmailAvailable(String username, String email) throws Exception;
}