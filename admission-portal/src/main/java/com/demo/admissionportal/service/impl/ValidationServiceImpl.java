package com.demo.admissionportal.service.impl;

import com.demo.admissionportal.entity.*;
import com.demo.admissionportal.exception.DataExistedException;
import com.demo.admissionportal.exception.UnSupportedException;
import com.demo.admissionportal.repository.*;
import com.demo.admissionportal.service.ValidationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Implementation of the Validation Service.
 *
 * @author hopeless
 * @version 1.0
 * @since 18/06/2024
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ValidationServiceImpl implements ValidationService {
    private final StudentRepository studentRepository;
    private final ConsultantRepository consultantRepository;
    private final UniversityRepository universityRepository;
    private final AdminRepository adminRepository;
    private final StaffRepository staffRepository;

    /**
     * Validates if the provided username and username are available across all user types.
     *
     * @param username The username to validate.
     * @param username The username to validate.
     * @return {@code false} if the username or username is already taken.
     * @throws Exception If the username or username is already taken.
     */
    public boolean validateUsernameAndEmailAvailable(String username, String email) throws Exception {
        log.info("Checking username and username availability.");
        String errorMessage = "Tên tài khoản hoặc username đã được sử dụng";
        if ((adminRepository.findFirstByUsernameOrEmail(username, email).isPresent()) ||
                (staffRepository.findFirstByUsernameOrEmail(username, email).isPresent()) ||
                (universityRepository.findFirstByUsernameOrEmail(username,email).isPresent()) ||
                (consultantRepository.findByEmailOrUsername(username, email).isPresent()) ||
                (studentRepository.findFirstByUsernameOrEmail(username, email).isPresent())
        ){
            log.warn("Username: {} or email: {} not available!", username, email);
            throw new Exception(errorMessage);
        }
        log.info("Email: {} and username: {} available.", username, email);
        return true;
    }

    /**
     * Validates if a phone number is unique across all user types.
     *
     * @param phoneNumber The phone number to validate.
     * @return {@code true} if the phone number is available, {@code false} otherwise.
     * @throws DataExistedException If the phone number is already in use.
     */
    public boolean validatePhoneNumber(String phoneNumber) throws DataExistedException {
        log.info("Checking phone number availability.");

        if ((adminRepository.findFirstByPhone(phoneNumber).isPresent()) ||
                (staffRepository.findFirstByPhone(phoneNumber).isPresent())||
                (universityRepository.findFirstByPhone(phoneNumber).isPresent()) ||
                (consultantRepository.findFirstByPhone(phoneNumber).isPresent()) ||
                (studentRepository.findFirstByPhone(phoneNumber).isPresent())
        ){
            log.warn("Phone number: {} not available!", phoneNumber);
            throw new DataExistedException("Số điện thoại đã được sử dụng");
        }
        log.info("Phone number: {} available.", phoneNumber);
        return true;
    }

    /**
     * Validates if an email is unique across all user types.
     *
     * @param email The email to validate.
     * @return {@code true} if the email is available, {@code false} otherwise.
     * @throws DataExistedException If the email is already in use.
     */
    public boolean validateEmail(String email) throws DataExistedException {
        log.info("Checking email availability.");
        if ((adminRepository.findFirstByEmail(email).isPresent()) ||
                (staffRepository.findFirstByEmail(email).isPresent())||
                (universityRepository.findFirstByEmail(email).isPresent()) ||
                (consultantRepository.findFirstByEmail(email).isPresent()) ||
                (studentRepository.findFirstByEmail(email).isPresent())
        ){
            log.warn("Email: {} not available!", email);
            throw new DataExistedException("Email đã được sử dụng.");
        }
        log.info("Email: {} available.", email);
        return true;
    }

    /**
     * Validates if a username is unique across all user types.
     *
     * @param username The username to validate.
     * @return {@code true} if the username is available, {@code false} otherwise.
     * @throws DataExistedException If the username is already in use.
     */
    public boolean validateUsername(String username) throws DataExistedException {
        log.info("Checking username availability.");
        if ((adminRepository.findFirstByUsername(username).isPresent()) ||
                (staffRepository.findFirstByUsername(username).isPresent())||
                (universityRepository.findFirstByUsername(username).isPresent()) ||
                (consultantRepository.findFirstByUsername(username).isPresent()) ||
                (studentRepository.findFirstByUsername(username).isPresent())
        ){
            log.warn("Username: {} not available!", username);
            throw new DataExistedException("Username đã được sử dụng.");
        }
        log.info("Username: {} available.", username);
        return true;
    }

    /**
     * Validates a phone number against a given user object, ensuring it's unique
     * if it's different from the user's current phone number.
     *
     * @param phoneNumber The phone number to validate.
     * @param user       The user object to compare the phone number against.
     * @throws DataExistedException If the phone number is already in use by a different user.
     * @throws UnSupportedException If the provided user type is not supported.
     */
    @Override
    public void validatePhoneNumber(String phoneNumber, Object user) throws DataExistedException, UnSupportedException {
        log.info("Checking phone number availability.");

        log.info("Checking object type.");
        List<Class<?>> supportedTypes = List.of(Student.class, Consultant.class, University.class, Staff.class, Admin.class);
        if (!supportedTypes.contains(user.getClass())) {
            log.warn("Unsupported user type: {}", user.getClass());
            throw new UnSupportedException("User type " + user.getClass() + " is not supported.");
        }
        log.info("Object type: {} valid", user.getClass());

        try {
            Method getPhoneMethod = user.getClass().getMethod("getPhone");
            String userPhoneNumber = (String) getPhoneMethod.invoke(user);

            if (userPhoneNumber == null || !userPhoneNumber.equals(phoneNumber)) {
                validatePhoneNumber(phoneNumber);
            }
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e){
            log.error("Error accessing 'getEmail' method:", e);
            throw new UnSupportedException(e.getMessage());
        }
    }

    /**
     * Validates an email against a given user object, ensuring it's unique
     * if it's different from the user's current email.
     *
     * @param email The email to validate.
     * @param user  The user object to compare the email against.
     * @throws DataExistedException If the email is already in use by a different user.
     * @throws UnSupportedException If the provided user type is not supported.
     */
    @Override
    public void validateEmail(String email, Object user) throws DataExistedException, UnSupportedException {
        log.info("Checking email availability.");

        log.info("Checking object type.");
        List<Class<?>> supportedTypes = List.of(Student.class, Consultant.class, University.class, Staff.class, Admin.class);
        if (!supportedTypes.contains(user.getClass())) {
            log.warn("Unsupported user type: {}", user.getClass());
            throw new UnSupportedException("User type " + user.getClass() + " is not supported.");
        }
        log.info("Object type: {} valid", user.getClass());

        try {
            Method getEmailMethod = user.getClass().getMethod("getEmail");
            String userEmail = (String) getEmailMethod.invoke(user);

            if (userEmail == null || !userEmail.equals(email)) {
                validateEmail(email);
            }
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            log.error("Error accessing 'getEmail' method:", e);
            throw new UnSupportedException(e.getMessage());
        }
    }

    /**
     * Validates a username against a given user object, ensuring it's unique
     * if it's different from the user's current username.
     *
     * @param username The username to validate.
     * @param user    The user object to compare the username against.
     * @throws DataExistedException If the username is already in use by a different user.
     * @throws UnSupportedException If the provided user type is not supported.
     */
    @Override
    public void validateUsername(String username, Object user) throws DataExistedException, UnSupportedException {
        log.info("Checking username availability.");

        log.info("Checking object type.");
        List<Class<?>> supportedTypes = List.of(Student.class, Consultant.class, University.class, Staff.class, Admin.class);
        if (!supportedTypes.contains(user.getClass())) {
            log.warn("Unsupported user type: {}", user.getClass());
            throw new UnSupportedException("User type " + user.getClass() + " is not supported.");
        }
        log.info("Object type: {} valid", user.getClass());

        try {
            Method getUsernameMethod = user.getClass().getMethod("getUsername");
            String userEmail = (String) getUsernameMethod.invoke(user);

            if (userEmail == null || !userEmail.equals(username)) {
                validateUsername(username);
            }
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            log.error("Error accessing 'getUsername' method:", e);
            throw new UnSupportedException(e.getMessage());
        }
    }
}