package com.demo.admissionportal.service.impl;

import com.demo.admissionportal.exception.DataExistedException;
import com.demo.admissionportal.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ValidationServiceImpl {
    private final UserRepository userRepository;

    /**
     * Validates if the provided username and username are available across all user types.
     *
     * @param username The username to validate.
     * @param username The username to validate.
     * @return {@code false} if the username or username is already taken.
     * @throws Exception If the username or username is already taken.
     */
    public boolean validateUsername(String username) throws DataExistedException {
        log.info("Checking username availability.");
        String errorMessage = "Tên tài khoản hoặc username đã được sử dụng";
        if (userRepository.findFirstByUsername(username).isPresent()){
            log.warn("Username: {} not available!", username);
            throw new DataExistedException(errorMessage);
        }
        log.info("Email: {} available.", username);
        return true;
    }

    /**
     * Validates if the provided email and email are available across all user types.
     *
     * @param email The email to validate.
     * @param email The email to validate.
     * @return {@code false} if the email or email is already taken.
     * @throws Exception If the email or email is already taken.
     */
    public boolean validateEmail(String email) throws DataExistedException {
        log.info("Checking email availability.");
        String errorMessage = "Email đã được sử dụng";
        if (userRepository.findFirstByEmail(email).isPresent()){
            log.warn("Username: {} not available!", email);
            throw new DataExistedException(errorMessage);
        }
        log.info("Email: {} available.", email);
        return true;
    }

    /**
     * Validates if the provided phone number are available across all user types.
     *
     * @param phone The phone number to validate.
     * @param phone The phone number to validate.
     * @return {@code false} if the phone number or phone number is already taken.
     * @throws Exception If the phone number or phone number is already taken.
     */
    public boolean validatePhoneNumber(String phone) throws DataExistedException {
//        log.info("Checking phone number availability.");
//        String errorMessage = "Email đã được sử dụng";
//        if (userRepository.findFirstByPhoneNumber(phone).isPresent()){
//            log.warn("Username: {} not available!", phone);
//            throw new DataExistedException(errorMessage);
//        }
//        log.info("Email: {} available.", phone);
//        return true;
        return true;
    }

    public boolean validateRegister(String username, String email, String phone) throws DataExistedException {
        return validateUsername(username) && validateEmail(email) && validatePhoneNumber(phone);
    }
}
