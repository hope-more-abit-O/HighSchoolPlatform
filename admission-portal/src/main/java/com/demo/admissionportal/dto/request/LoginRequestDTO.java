package com.demo.admissionportal.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The type Login request dto.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequestDTO {
    @NotBlank(message = "Username không thể để trống")
    private String username;
    @NotBlank(message = "Mật khẩu không thể để trống !")
    private String password;
}