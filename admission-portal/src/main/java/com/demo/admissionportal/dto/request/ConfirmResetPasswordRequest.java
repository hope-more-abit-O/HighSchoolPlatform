package com.demo.admissionportal.dto.request;

public record ConfirmResetPasswordRequest(String newPassword, String reEnterNewPassword) {
}