package com.demo.admissionportal.dto.request;

import com.demo.admissionportal.util.EnumPassword;
import com.demo.admissionportal.util.EnumPhone;
import com.demo.admissionportal.util.EnumStaffUsernameValidator;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterStaffRequestDTO {
    @NotNull(message = "Tên đăng nhập không thể để trống !")
    @EnumStaffUsernameValidator
    private String username;
    @NotNull(message = "Tên Email không thể để trống !")
    @Email(message = "Email phải có định dạng hợp lệ!")
    private String email;
    @EnumPassword(message = "Mật khẩu phải từ 8 đến 16 ký tự, bao gồm ít nhất 1 chữ hoa, 1 chữ thường, 1 chữ số và 1 ký tự đặc biệt!")
    private String password;
    @NotNull(message = "Họ không thể để trống !")
    private String firstName;
    private String middleName;
    @NotNull(message = "Tên không thể để trống !")
    private String lastName;
    @NotNull(message = "Số điện thoại không thể để trống !")
    @EnumPhone(message = "Số điện thoại phải bắt đầu bằng số 0 và chứa 10-11 chữ số!")
    private String phone;
}
