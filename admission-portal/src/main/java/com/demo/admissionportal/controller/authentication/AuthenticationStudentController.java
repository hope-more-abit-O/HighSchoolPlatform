package com.demo.admissionportal.controller.authentication;

import com.demo.admissionportal.constants.ResponseCode;
import com.demo.admissionportal.dto.request.LoginRequestDTO;
import com.demo.admissionportal.dto.request.RegisterStudentRequestDTO;
import com.demo.admissionportal.dto.request.VerifyAccountRequestDTO;
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
        if (registerStudentAccount.getStatus() == ResponseCode.C206.getCode()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(registerStudentAccount);
        } else if (registerStudentAccount.getStatus() == ResponseCode.C204.getCode()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(registerStudentAccount);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(registerStudentAccount);
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
        if (loginAccount.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(loginAccount);
        } else if (loginAccount.getStatus() == ResponseCode.C203.getCode()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(loginAccount);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(loginAccount);
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
     * @param verifyAccountRequestDTO the verify student request dto
     * @return the response entity
     */
    @PostMapping("/verify-account")
    public ResponseEntity<ResponseData<?>> verifyAccount(@RequestBody VerifyAccountRequestDTO verifyAccountRequestDTO) {
        if (verifyAccountRequestDTO == null) {
            new ResponseEntity<ResponseData<?>>(HttpStatus.BAD_REQUEST);
        }
        ResponseData<?> verifyAccount = authenticationStudentService.verifyAccount(verifyAccountRequestDTO);
        if (verifyAccount.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(verifyAccount);
        } else if (verifyAccount.getStatus() == ResponseCode.C201.getCode()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(verifyAccount);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(verifyAccount);
    }

    /**
     * Regenerate otp response entity.
     *
     * @param requestDTO the request dto
     * @return the response entity
     */
    @PostMapping("/regenerate-otp")
    public ResponseEntity<ResponseData<?>> regenerateOtp(@RequestBody RegenerateOTPRequestDTO requestDTO) {
        if (requestDTO == null) {
            new ResponseEntity<ResponseData<?>>(HttpStatus.BAD_REQUEST);
        }
        ResponseData<?> regenerateOtp = authenticationStudentService.regenerateOtp(requestDTO);
        if (regenerateOtp.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(regenerateOtp);
        } else if (regenerateOtp.getStatus() == ResponseCode.C201.getCode()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(regenerateOtp);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(regenerateOtp);
    }
}
