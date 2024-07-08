package com.demo.admissionportal.dto.request.consultant;

import com.demo.admissionportal.util.enum_validator.EnumPhone;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
public class ConsultantInfoRequest {
    @NotNull
    private String username;

    @NotNull
    private String email;

    @NotNull(message = "Tên không thể để trống!")
    private String firstName;

    private String middleName;

    @NotNull(message = "Họ không thể để trống!")
    private String lastName;

    @NotNull(message = "Ngày sinh không thể để trống!")
    private String birthDate;

    @NotNull(message = "Số điện thoại không thể để trống!")
    @EnumPhone(message = "Số điện thoại phải bắt đầu bằng số 0 và chứa 10-11 chữ số!")
    private String phone;
}
