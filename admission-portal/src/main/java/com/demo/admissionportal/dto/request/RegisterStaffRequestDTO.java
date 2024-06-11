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
    @NotNull(message = "Ten khong duoc trong")
    private String name;
    @NotNull(message = "Ten dang nhap khong duoc trong")
    private String username;
    @NotNull(message = "Email khong duoc trong")
    private String email;
    @NotNull(message = "Password khong duoc trong")
    private String password;
    private String avatar;
    @NotNull (message = "So dien thoai khong duoc trong")
    private String phone;
    @EnumValue(name = "type", enumClass = SubjectStatus.class, message = "Trạng thái Staff là ACTIVE hoặc INACTIVE")
    @NotNull
    private String Status;
}
