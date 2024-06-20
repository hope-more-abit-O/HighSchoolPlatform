package com.demo.admissionportal.dto.request;

public record ConfirmResetPasswordRequest(String email, String newPassword) {
}