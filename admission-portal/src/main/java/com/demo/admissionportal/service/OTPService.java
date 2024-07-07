package com.demo.admissionportal.service;

import com.demo.admissionportal.dto.request.authen.CodeVerifyAccountRequestDTO;
import com.demo.admissionportal.entity.User;
import com.demo.admissionportal.entity.UserInfo;

import java.time.LocalDateTime;

/**
 * The interface Otp service.
 */
public interface OTPService {
    /**
     * Save otp.
     *
     * @param email        the email
     * @param otp          the otp
     * @param timeGenerate the time generate
     */
    void saveOTP(String email, String otp, LocalDateTime timeGenerate);

    /**
     * Save user.
     *
     * @param email    the email
     * @param user     the user
     * @param userInfo the user info
     */
    void saveUser(String email, User user, UserInfo userInfo);

    /**
     * Gets otp.
     *
     * @param email the email
     * @return the otp
     */
    String getOTP(String email);

    /**
     * Gets otp date time.
     *
     * @param email the email
     * @return the otp date time
     */
    LocalDateTime getOTPDateTime(String email);

    /**
     * Gets user.
     *
     * @param email the email
     * @return the user
     */
    User getUser(String email);

    /**
     * Gets user info.
     *
     * @param email the email
     * @return the user info
     */
    UserInfo getUserInfo(String email);

    /**
     * Delete otp.
     *
     * @param email the email
     */
    void deleteOTP(String email);

    /**
     * S uid string.
     *
     * @param email                       the email
     * @param codeVerifyAccountRequestDTO the code verify account request dto
     * @return the string
     */
    void savesUID(String email, CodeVerifyAccountRequestDTO codeVerifyAccountRequestDTO);

    /**
     * Gets uid.
     *
     * @param email the email
     * @return the uid
     */
    String getsUID(String email);
}