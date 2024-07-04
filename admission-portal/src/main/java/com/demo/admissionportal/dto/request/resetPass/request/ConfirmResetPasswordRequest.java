package com.demo.admissionportal.dto.request.resetPass.request;

import com.demo.admissionportal.util.enum_validator.EnumPassword;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConfirmResetPasswordRequest {
    @NotNull
    @EnumPassword(message = "Mật khẩu phải từ 8 đến 16 ký tự, bao gồm ít nhất 1 chữ hoa, 1 chữ thường, 1 chữ số và 1 ký tự đặc biệt!")
    private String newPassword;
    @NotNull
    @EnumPassword(message = "Mật khẩu phải từ 8 đến 16 ký tự, bao gồm ít nhất 1 chữ hoa, 1 chữ thường, 1 chữ số và 1 ký tự đặc biệt!")
    private String confirmNewPassword;
}
