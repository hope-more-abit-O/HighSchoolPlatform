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
    @EnumPassword(message = "Mật khẩu phải từ 8 đến 16 ký tự, bao gồm ít nhất 1 chữ hoa, 1 chữ thường, 1 chữ số và 1 ký tự đặc biệt!")
    private String password;
}
