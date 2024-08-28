package com.demo.admissionportal.dto.request;


import com.demo.admissionportal.util.enum_validator.EnumPassword;
import com.demo.admissionportal.util.enum_validator.EnumPhone;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterAdminRequestDTO {
    @NotBlank(message = "Tên đăng nhập không thể để trống !")
    private String username;
    @NotBlank(message = "Tên Email không thể để trống !")
    @Email(message = "Email phải có định dạng hợp lệ!")
    private String email;
//    @EnumPassword(message = "Mật khẩu phải từ 8 đến 16 ký tự, bao gồm ít nhất 1 chữ hoa, 1 chữ thường, 1 chữ số và 1 ký tự đặc biệt!")
    private String password;
    @NotBlank(message = "Họ không thể để trống !")
    private String firstName;
    private String middleName;
    @NotBlank(message = "Tên không thể để trống !")
    private String lastName;
    @NotBlank(message = "Số điện thoại không thể để trống !")
    @EnumPhone(message = "Số điện thoại phải bắt đầu bằng số 0 và chứa 10-11 chữ số!")
    private String phone;
}