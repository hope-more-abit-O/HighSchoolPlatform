package com.demo.admissionportal.service;

import com.demo.admissionportal.exception.DataExistedException;
import com.demo.admissionportal.exception.UnSupportedException;

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

    /**
     * Validates a phone number against a given user object, ensuring it's unique
     * if it's different from the user's current phone number.
     *
     * @param phoneNumber The phone number to validate.
     * @param user       The user object to compare the phone number against.
     * @throws DataExistedException If the phone number is already in use by a different user.
     * @throws UnSupportedException If the provided user type is not supported.
     */
    void validatePhoneNumber(String phoneNumber, Object user) throws DataExistedException, UnSupportedException;

    /**
     * Validates an email against a given user object, ensuring it's unique
     * if it's different from the user's current email.
     *
     * @param email The email to validate.
     * @param user  The user object to compare the email against.
     * @throws DataExistedException If the email is already in use by a different user.
     * @throws UnSupportedException If the provided user type is not supported.
     */
    void validateEmail(String email, Object user) throws DataExistedException, UnSupportedException;

    /**
     * Validates a username against a given user object, ensuring it's unique
     * if it's different from the user's current username.
     *
     * @param username The username to validate.
     * @param user    The user object to compare the username against.
     * @throws DataExistedException If the username is already in use by a different user.
     * @throws UnSupportedException If the provided user type is not supported.
     */
    void validateUsername(String username, Object user) throws DataExistedException, UnSupportedException;
}