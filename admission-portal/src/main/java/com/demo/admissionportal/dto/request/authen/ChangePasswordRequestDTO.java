package com.demo.admissionportal.dto.request.authen;

import com.demo.admissionportal.util.enum_validator.EnumPassword;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * The type Change password request dto.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChangePasswordRequestDTO implements Serializable {
    @NotBlank(message = "Mật khẩu cũ không được bỏ trống")
    private String currentPassword;

    @NotBlank(message = "Mật khẩu mới không được bỏ trống")
    @EnumPassword(message = "Mật khẩu phải từ 8 đến 16 ký tự, bao gồm ít nhất 1 chữ hoa, 1 chữ thường, 1 chữ số và 1 ký tự đặc biệt!")
    private String newPassword;

    @NotBlank(message = "Nhập lại mật khẩu mới không được bỏ trống")
    private String confirmPassword;
}
