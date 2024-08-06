package com.demo.admissionportal.dto.request.university_campus;

import com.demo.admissionportal.constants.CampusType;
import com.demo.admissionportal.util.enum_validator.*;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateCampusRequestDTO implements Serializable {
    @EnumNameV2(message = "Tên campus chỉ chứa chữ")
    private String campusName;

    @EnumPhoneV2(message = "Số điện thoại phải bắt đầu bằng số 0 và chứa 10-11 chữ số!")
    private String phone;

    @EnumEmailV2(message = "Email phải đúng định dạng")
    private String email;

    @Nullable
    private List<String> picture;

    @Nullable
    private String specificAddress;

    @Nullable
    private Integer wardId;

    @Nullable
    private Integer districtId;

    @Nullable
    private Integer provinceId;

}
