package com.demo.admissionportal.service;

import com.demo.admissionportal.dto.request.LoginRequestDTO;
import com.demo.admissionportal.dto.request.RegenerateOTPRequestDTO;
import com.demo.admissionportal.dto.request.RegisterStudentRequestDTO;
import com.demo.admissionportal.dto.request.VerifyStudentRequestDTO;
import com.demo.admissionportal.dto.response.LoginResponseDTO;
import com.demo.admissionportal.dto.response.ResponseData;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.coyote.Response;

import java.io.IOException;


/**
 * The interface Authentication student service.
 */
public interface AuthenticationStudentService {
    /**
     * Register authentication response.
     *
     * @param request the request
     * @return the authentication response
     */
    ResponseData<LoginResponseDTO> register(RegisterStudentRequestDTO request);

    /**
     * Authenticate authentication response.
     *
     * @param request the request
     * @return the authentication response
     */
    ResponseData<LoginResponseDTO> login(LoginRequestDTO request);

    /**
     * Refresh token.
     *
     * @param request  the request
     * @param response the response
     * @throws IOException the io exception
     */
    void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException;

    /**
     * Verify account response data.
     *
     * @param verifyStudentRequestDTO the verify student request dto
     * @return the response data
     */
    ResponseData<?> verifyAccount(VerifyStudentRequestDTO verifyStudentRequestDTO);

    /**
     * Regenerate otp response data.
     *
     * @param requestDTO the request dto
     * @return the response data
     */
    ResponseData<?> regenerateOtp(RegenerateOTPRequestDTO requestDTO);
}
