package com.demo.admissionportal.controller;

import com.demo.admissionportal.dto.request.LoginRequestDTO;
import com.demo.admissionportal.dto.request.RegisterStudentRequestDTO;
import com.demo.admissionportal.dto.request.VerifyStudentRequestDTO;
import com.demo.admissionportal.dto.response.LoginResponseDTO;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.dto.request.RegenerateOTPRequestDTO;
import com.demo.admissionportal.service.AuthenticationStudentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * The type Authentication controller.
 */
@RestController
@RequestMapping("/api/v1/auth/student")
@RequiredArgsConstructor
public class AuthenticationStudentController {
    private final AuthenticationStudentService authenticationStudentService;


    /**
     * Register response entity.
     *
     * @param request the request
     * @return the response entity
     */
    @PostMapping("/register")
    public ResponseEntity<ResponseData<LoginResponseDTO>> register(@RequestBody @Valid RegisterStudentRequestDTO request) {
        if (request == null) {
            new ResponseEntity<ResponseData<LoginResponseDTO>>(HttpStatus.BAD_REQUEST);
        }
        ResponseData<LoginResponseDTO> registerStudentAccount = authenticationStudentService.register(request);
        if (registerStudentAccount.getData() != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(registerStudentAccount);
        } else {
            return ResponseEntity.status(registerStudentAccount.getStatus()).body(registerStudentAccount);
        }
    }

    /**
     * Authenticate response entity.
     *
     * @param request the request
     * @return the response entity
     */
    @PostMapping("/login")
    public ResponseEntity<ResponseData<LoginResponseDTO>> login(@RequestBody @Valid LoginRequestDTO request) {
        if (request == null) {
            new ResponseEntity<ResponseData<LoginResponseDTO>>(HttpStatus.BAD_REQUEST);
        }
        ResponseData<LoginResponseDTO> loginAccount = authenticationStudentService.login(request);
        if (loginAccount.getData() != null) {
            return ResponseEntity.status(HttpStatus.OK).body(loginAccount);
        } else {
            return ResponseEntity.status(loginAccount.getStatus()).body(loginAccount);
        }
    }

    /**
     * Refresh token.
     *
     * @param request  the request
     * @param response the response
     * @throws IOException the io exception
     */
    @PostMapping("/refresh-token")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        authenticationStudentService.refreshToken(request, response);
    }

    /**
     * Verify account response entity.
     *
     * @param verifyStudentRequestDTO the verify student request dto
     * @return the response entity
     */
    @PostMapping("/verify-account")
    public ResponseEntity<ResponseData<?>> verifyAccount(@RequestBody VerifyStudentRequestDTO verifyStudentRequestDTO) {
        if (verifyStudentRequestDTO == null) {
            new ResponseEntity<ResponseData<?>>(HttpStatus.BAD_REQUEST);
        }
        ResponseData<?> verifyAccount = authenticationStudentService.verifyAccount(verifyStudentRequestDTO);
        if (verifyAccount.getData() != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(verifyAccount);
        } else {
            return ResponseEntity.status(verifyAccount.getStatus()).body(verifyAccount);
        }
    }

    @PostMapping("/regenerate-otp")
    public ResponseEntity<ResponseData<?>> regenerateOtp(@RequestBody RegenerateOTPRequestDTO requestDTO) {
        if (requestDTO == null) {
            new ResponseEntity<ResponseData<?>>(HttpStatus.BAD_REQUEST);
        }
        ResponseData<?> regenerateOtp = authenticationStudentService.regenerateOtp(requestDTO);
        if (regenerateOtp.getData() != null) {
            return ResponseEntity.status(HttpStatus.OK).body(regenerateOtp);
        } else {
            return ResponseEntity.status(regenerateOtp.getStatus()).body(regenerateOtp);
        }
    }
}
