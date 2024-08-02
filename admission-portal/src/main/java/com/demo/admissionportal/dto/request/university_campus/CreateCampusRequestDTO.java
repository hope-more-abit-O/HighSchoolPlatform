package com.demo.admissionportal.dto.request.university_campus;

import com.demo.admissionportal.util.enum_validator.EnumName;
import com.demo.admissionportal.util.enum_validator.EnumPhone;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CreateCampusRequestDTO implements Serializable {
    @NotNull(message = "Tên campus không thể để trống !")
    @EnumName(message = "Tên campus chỉ chứa chữ")
    private String campusName;

    @NotNull(message = "Số điện thoại không thể để trống !")
    @EnumPhone(message = "Số điện thoại phải bắt đầu bằng số 0 và chứa 10-11 chữ số!")
    private String phone;

    @Email(message = "Email phải đúng định dạng")
    @NotNull(message = "Email không được để trống")
    private String email;
}
