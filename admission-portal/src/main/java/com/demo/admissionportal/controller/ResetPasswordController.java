package com.demo.admissionportal.controller;

import com.demo.admissionportal.constants.ResponseCode;
import com.demo.admissionportal.dto.request.ConfirmResetPasswordRequest;
import com.demo.admissionportal.dto.request.ResetPasswordRequest;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.service.ResetPasswordService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for handling password reset requests.
 */
@RestController
@RequestMapping("/account")
@CrossOrigin
@Slf4j
public class ResetPasswordController {
    @Autowired
    private ResetPasswordService resetPasswordService;

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
            log.warn("Entity not found !");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        }
        log.error("Failed to reset password for Entity ");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
    }

    /**
     * Confirms the reset password request using the reset token.
     *
     * @param request the confirm reset password request
     * @param resetToken the reset token
     * @return the response entity
     */
    @PostMapping("/password/confirm/{resetToken}")
    @Operation(summary = "Xác nhận yêu cầu tạo lại mật khẩu ")
    public ResponseEntity<?> confirmResetPassword(@RequestBody ConfirmResetPasswordRequest request,
                                                  @PathVariable @Valid String resetToken) {
        log.info("Confirmation for reset password with token: {}", resetToken);
        ResponseData<?> result = resetPasswordService.confirmResetPassword(request, resetToken);
        if (result.getStatus() == ResponseCode.C200.getCode()) {
            log.info("Password reset confirmed ");
            return ResponseEntity.ok(result);
        } else if (result.getStatus() == ResponseCode.C203.getCode()) {
            log.warn("Entity not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        }
        log.error("Failed to reset password");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
    }
}
