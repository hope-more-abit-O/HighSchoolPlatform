package com.demo.admissionportal.dto.request.consultant;

import com.demo.admissionportal.constants.Gender;
import com.demo.admissionportal.util.enum_validator.EnumName;
import com.demo.admissionportal.util.enum_validator.EnumPhone;
import com.demo.admissionportal.util.enum_validator.EnumSpecificAddress;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Date;

@Data
public class ConsultantInfoRequest {
    @NotNull
    private String username;

    @NotNull(message = "Số điện thoại không thể để trống!")
    @EnumPhone(message = "Số điện thoại phải bắt đầu bằng số 0 và chứa 10-11 chữ số!")
    private String phone;

    @NotNull(message = "Email không thể để trống!")
    @Email(message = "Email phải có định dạng hợp lệ.")
    private String email;

    @NotNull(message = "Họ thể để trống!")
    @EnumName(message = "Hãy nhập đúng cú pháp cho họ.")
    private String firstName;

    @EnumName(message = "Hãy nhập đúng cú pháp cho tên đệm.")
    private String middleName;

    @NotNull(message = "Tên không thể để trống!")
    @EnumName(message = "Hãy nhập đúng cú pháp cho họ.")
    private String lastName;

    @NotNull(message = "Địa chỉ cụ thể không thể để trống!")
    @EnumSpecificAddress(message = "Địa chỉ cụ thể không đúng định dạng!")
    private String specificAddress;

    @NotNull(message = "Địa chỉ cấp 1 không được để trống!")
    private Integer provinceId;

    @NotNull(message = "Địa chỉ cấp 2 không được để trống!")
    private Integer districtId;

    @NotNull(message = "Địa chỉ cấp 3 không được để trống!")
    private Integer wardId;

    @NotNull(message = "Ngày sinh không được để trống!")
    private Date birthday;

    @NotNull(message = "Giới tính không được để trống!")
    private Gender gender;
}
