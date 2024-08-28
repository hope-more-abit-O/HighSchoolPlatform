package com.demo.admissionportal.dto.request.university_campus;

import com.demo.admissionportal.util.enum_validator.EnumPhone;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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
    @NotBlank(message = "Tên campus không thể để trống !")
    private String campusName;

    @NotBlank(message = "Số điện thoại không thể để trống !")
    @EnumPhone(message = "Số điện thoại phải bắt đầu bằng số 0 và chứa 10-11 chữ số!")
    private String phone;

    @Email(message = "Email phải đúng định dạng")
    @NotBlank(message = "Email không được để trống")
    private String email;

    private List<String> picture;

    @NotBlank(message = "Địa chỉ cụ thể không được trống")
    private String specificAddress;

    @NotBlank(message = "Phường không được trống")
    private Integer wardId;

    @NotBlank(message = "Quận không được trống")
    private Integer districtId;

    @NotBlank(message = "Thành phố không được trống")
    private Integer provinceId;
}
