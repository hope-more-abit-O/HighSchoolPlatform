package com.demo.admissionportal.dto.request;

import com.demo.admissionportal.util.enum_validator.EnumName;
import com.demo.admissionportal.util.enum_validator.EnumPhone;
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
    @NotNull(message = "Tên Email không thể để trống !")
    @Email(message = "Email phải có định dạng hợp lệ!")
    private String email;
    @NotNull(message = "Họ không thể để trống !")
    @EnumName(message = "Họ chỉ chứa chữ")
    private String firstName;
    @EnumName(message = "Tên đệm chỉ chứa chữ")
    private String middleName;
    @NotNull(message = "Tên không thể để trống !")
    @EnumName(message = "Tên chỉ chứa chữ")
    private String lastName;
    @NotNull(message = "Số điện thoại không thể để trống !")
    @EnumPhone(message = "Số điện thoại phải bắt đầu bằng số 0 và chứa 10-11 chữ số!")
    private String phone;
    @NotNull(message = "Vui lòng chọn địa chỉ hợp lệ ")
    private Integer provinceId;

    public void trim() {
        firstName = firstName.trim();
        middleName = middleName.trim();
        lastName = lastName.trim();
        email = email.trim();
        phone = phone.trim();
    }
}