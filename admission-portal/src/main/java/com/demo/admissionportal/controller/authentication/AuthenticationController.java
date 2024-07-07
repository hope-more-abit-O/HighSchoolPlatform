package com.demo.admissionportal.controller.authentication;

import com.demo.admissionportal.constants.ResponseCode;
import com.demo.admissionportal.dto.request.LoginRequestDTO;
import com.demo.admissionportal.dto.request.authen.ChangePasswordRequestDTO;
import com.demo.admissionportal.dto.request.authen.EmailRequestDTO;
import com.demo.admissionportal.dto.request.authen.RegisterUserRequestDTO;
import com.demo.admissionportal.dto.request.redis.RegenerateOTPRequestDTO;
import com.demo.admissionportal.dto.request.redis.VerifyAccountRequestDTO;
import com.demo.admissionportal.dto.request.resetPass.request.ConfirmResetPasswordRequest;
import com.demo.admissionportal.dto.request.resetPass.request.ResetPasswordRequest;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.dto.response.authen.LoginResponseDTO;
import com.demo.admissionportal.entity.User;
import com.demo.admissionportal.service.AuthenticationUserService;
import com.demo.admissionportal.service.OTPService;
import com.demo.admissionportal.service.resetPassword.ResetPasswordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

/**
 * The type Authentication controller.
 */
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthenticationController {
    private final AuthenticationUserService authenticationUserService;
    private final OTPService otpService;
    private ResetPasswordService resetPasswordService;

    /**
     * Login response entity.
     *
     * @param request the request
     * @return the response entity
     */
    @PostMapping("/login")
    public ResponseEntity<ResponseData<LoginResponseDTO>> login(@RequestBody @Valid LoginRequestDTO request) {
        if (request == null) {
            new ResponseEntity<ResponseData<LoginResponseDTO>>(HttpStatus.BAD_REQUEST);
        }
        ResponseData<LoginResponseDTO> loginAccount = authenticationUserService.login(request);
        if (loginAccount.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.status(HttpStatus.OK).body(loginAccount);
        } else if (loginAccount.getStatus() == ResponseCode.C203.getCode()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(loginAccount);
        } else if (loginAccount.getStatus() == ResponseCode.C201.getCode()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(loginAccount);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(loginAccount);
    }

    /**
     * Register response entity.
     *
     * @param request the request
     * @return the response entity //
     */
    @PostMapping("/register")
    public ResponseEntity<ResponseData<?>> register(@RequestBody @Valid RegisterUserRequestDTO request) {
        if (request == null) {
            new ResponseEntity<ResponseData<?>>(HttpStatus.BAD_REQUEST);
        }
        ResponseData<?> registerAccount = authenticationUserService.register(request);
        if (registerAccount.getStatus() == ResponseCode.C206.getCode()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(registerAccount);
        } else if (registerAccount.getStatus() == ResponseCode.C204.getCode()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(registerAccount);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(registerAccount);
    }

    /**
     * Verify account response entity.
     *
     * @param verifyAccountRequestDTO the verify account request dto
     * @return the response entity
     */
    @PostMapping("/verify-account/{sUID}")
    public ResponseEntity<ResponseData<?>> verifyAccount(@PathVariable("sUID") String sUID, @RequestBody VerifyAccountRequestDTO verifyAccountRequestDTO) {
        if (verifyAccountRequestDTO == null || sUID == null) {
            new ResponseEntity<ResponseData<?>>(HttpStatus.BAD_REQUEST);
        }
        String sUIDRedis = otpService.getsUID(verifyAccountRequestDTO.getEmail());
        if (!sUID.equals(sUIDRedis)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseData<>(ResponseCode.C205.getCode(), " sUID không đúng"));
        }
        ResponseData<?> verifyAccount = authenticationUserService.verifyAccount(verifyAccountRequestDTO);
        if (verifyAccount.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.status(HttpStatus.OK).body(verifyAccount);
        } else if (verifyAccount.getStatus() == ResponseCode.C201.getCode()) {
            return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).body(verifyAccount);
        } else if (verifyAccount.getStatus() == ResponseCode.C204.getCode()) {
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
        ResponseData<?> regenerateOtp = authenticationUserService.regenerateOtp(requestDTO);
        if (regenerateOtp.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.status(HttpStatus.OK).body(regenerateOtp);
        } else if (regenerateOtp.getStatus() == ResponseCode.C201.getCode()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(regenerateOtp);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(regenerateOtp);
    }

    /**
     * Change password response entity.
     *
     * @param changePasswordRequestDTO the change password request dto
     * @param principal                the principal
     * @return the response entity
     */
    @PutMapping("/change-password")
    @SecurityRequirement(name = "BearerAuth")
    @PreAuthorize("hasAnyAuthority('STAFF','USER','ADMIN','CONSULTANT','UNIVERSITY')")
    public ResponseEntity<ResponseData<?>> changePassword(@RequestBody @Valid ChangePasswordRequestDTO changePasswordRequestDTO, Principal principal) {
        if (changePasswordRequestDTO == null) {
            new ResponseEntity<ResponseData<?>>(HttpStatus.BAD_REQUEST);
        }
        ResponseData<?> changePasswordAccount = authenticationUserService.changePassword(changePasswordRequestDTO, principal);
        if (changePasswordAccount.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.status(HttpStatus.OK).body(changePasswordAccount);
        } else if (changePasswordAccount.getStatus() == ResponseCode.C201.getCode()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(changePasswordAccount);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(changePasswordAccount);
    }

    /**
     * Check email existed response entity.
     *
     * @param requestDTO the request dto
     * @return the response entity
     */
    @PostMapping("/email/check-existed")
    public ResponseEntity<ResponseData<User>> checkEmailExisted(@RequestBody EmailRequestDTO requestDTO) {
        if (requestDTO == null) {
            new ResponseEntity<ResponseData<?>>(HttpStatus.BAD_REQUEST);
        }
        ResponseData<User> account = authenticationUserService.checkEmailExisted(requestDTO);
        if (account.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.status(HttpStatus.OK).body(account);
        } else if (account.getStatus() == ResponseCode.C206.getCode()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(account);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(account);
    }

    /**
     * Handles a request to reset the password by sending a reset token via email.
     *
     * @param request the reset password request
     * @return the response entity
     */
    @PostMapping("/reset/password")
    @Operation(summary = "Yêu cầu tạo lại mật khẩu ( nhận mã token qua email )")
    public ResponseEntity<?> requestResetPassword(@RequestBody ResetPasswordRequest request) {
        log.info("Reset password request for email: {}", request.email());
        ResponseData<?> result = resetPasswordService.resetPasswordRequest(request);
        if (result.getStatus() == ResponseCode.C206.getCode()) {
            log.info("Reset password token sent !");
            return ResponseEntity.ok(result);
        } else if (result.getStatus() == ResponseCode.C203.getCode()) {
            log.warn("User not found !");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        }
        log.error("Failed to reset password for User ");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
    }

    /**
     * Confirms the reset password request using the reset token.
     *
     * @param request    the confirm reset password request
     * @param resetToken the reset token
     * @return the response entity
     */
    @PostMapping("/password/confirm/{resetToken}")
    @Operation(summary = "Xác nhận yêu cầu tạo lại mật khẩu ")
    public ResponseEntity<?> confirmResetPassword(@RequestBody @Valid ConfirmResetPasswordRequest request,
                                                  @PathVariable @Valid String resetToken) {
        log.info("Confirmation for reset password with token: {}", resetToken);
        ResponseData<?> result = resetPasswordService.confirmResetPassword(request, resetToken);
        if (result.getStatus() == ResponseCode.C200.getCode()) {
            log.info("Password reset confirmed ");
            return ResponseEntity.ok(result);
        } else if (result.getStatus() == ResponseCode.C203.getCode()) {
            log.warn("User not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        }
        log.error("Failed to reset password");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
    }
}