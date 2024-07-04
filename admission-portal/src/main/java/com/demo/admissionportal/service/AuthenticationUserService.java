package com.demo.admissionportal.service;

import com.demo.admissionportal.dto.request.LoginRequestDTO;
import com.demo.admissionportal.dto.request.authen.ChangePasswordRequestDTO;
import com.demo.admissionportal.dto.request.authen.EmailRequestDTO;
import com.demo.admissionportal.dto.request.authen.RegisterUserRequestDTO;
import com.demo.admissionportal.dto.request.redis.RegenerateOTPRequestDTO;
import com.demo.admissionportal.dto.request.redis.VerifyAccountRequestDTO;
import com.demo.admissionportal.dto.response.LoginResponseDTO;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.entity.User;

import java.security.Principal;

/**
 * The interface Authentication user service.
 */
public interface AuthenticationUserService {
    /**
     * Login response data.
     *
     * @param request the request
     * @return the response data
     */
    ResponseData<LoginResponseDTO> login(LoginRequestDTO request);

    /**
     * Register response data.
     *
     * @param request the request
     * @return the response data
     */
    ResponseData<?> register(RegisterUserRequestDTO request);

    /**
     * Verify account response data.
     *
     * @param verifyAccountRequestDTO the verify account request dto
     * @return the response data
     */
    ResponseData<?> verifyAccount(VerifyAccountRequestDTO verifyAccountRequestDTO);

    /**
     * Regenerate otp response data.
     *
     * @param requestDTO the request dto
     * @return the response data
     */
    ResponseData<?> regenerateOtp(RegenerateOTPRequestDTO requestDTO);

    /**
     * Change password response data.
     *
     * @param changePasswordRequestDTO the change password request dto
     * @param principal                the principal
     * @return the response data
     */
    ResponseData<?> changePassword(ChangePasswordRequestDTO changePasswordRequestDTO, Principal principal);

    /**
     * Check email existed response data.
     *
     * @param requestDTO the request dto
     * @return the response data
     */
    ResponseData<User> checkEmailExisted(EmailRequestDTO requestDTO);
}
