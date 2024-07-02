package com.demo.admissionportal.dto.request;

import com.demo.admissionportal.util.EnumPassword;
import jakarta.validation.constraints.NotNull;
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
    @NotNull(message = "Username không thể để trống")
    private String username;
    @NotNull(message = "Mật khẩu không thể để trống !")
    private String password;
}
