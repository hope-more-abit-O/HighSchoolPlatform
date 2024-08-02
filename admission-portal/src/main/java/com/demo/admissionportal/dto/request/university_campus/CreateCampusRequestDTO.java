package com.demo.admissionportal.dto.request.university_campus;

import com.demo.admissionportal.constants.CampusType;
import com.demo.admissionportal.util.enum_validator.EnumName;
import com.demo.admissionportal.util.enum_validator.EnumPhone;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * The type Create campus request dto.
 */
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

    private List<String> picture;

    @NotNull(message = "Địa chỉ cụ thể không được trống")
    private String specificAddress;

    @NotNull(message = "Phường không được trống")
    private Integer wardId;

    @NotNull(message = "Quận không được trống")
    private Integer districtId;

    @NotNull(message = "Thành phố không được trống")
    private Integer provinceId;

    @NotNull(message = "Chọn cơ sở không được trống")
    @Enumerated(EnumType.STRING)
    private CampusType type;
}
