package com.demo.admissionportal.dto.request;

import com.demo.admissionportal.constants.SubjectStatus;
import com.demo.admissionportal.util.EnumValue;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The type Register staff request dto.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Valid
public class RegisterStaffRequestDTO{
    @NotNull(message = "Tên không thể để trống !")
    private String name;
    @NotNull(message = "Tên đăng nhập không thể để trống !")
    private String username;
    @NotNull(message = "Tên Email không thể để trống !")
    private String email;
    @NotNull(message = "Mật khẩu không thể để trống !")
    private String password;
    private String avatar;
    @NotNull (message = "Số điện thoại không thể để trống !")
    private String phone;
    @EnumValue(name = "type", enumClass = SubjectStatus.class, message = "Trạng thái Staff là ACTIVE hoặc INACTIVE")
    @NotNull
    private String Status;
}
