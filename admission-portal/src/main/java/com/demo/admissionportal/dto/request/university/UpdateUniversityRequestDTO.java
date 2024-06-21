package com.demo.admissionportal.dto.request.university;

import com.demo.admissionportal.constants.AccountStatus;
import com.demo.admissionportal.constants.UniversityType;
import com.demo.admissionportal.util.EnumValue;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * The type Update university request dto.
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Valid
public class UpdateUniversityRequestDTO implements Serializable {
    private Integer id;
    /**
     * The code of the university.
     *
     * @NotNull(message = "Mã trường không được để trống")
     */
    @NotNull(message = "Mã trường không được để trống")
    private String code;

    /**
     * The username of the university.
     *
     * @NotNull(message = "Tên người dùng không được để trống")
     */
    @NotNull(message = "Tên người dùng không được để trống")
    private String username;

    /**
     * The name of the university.
     *
     * @NotNull(message = "Tên của tư vấn viên không được để trống")
     */
    @NotNull(message = "Tên của tư vấn viên không được để trống")
    private String name;

    /**
     * The email address of the university.
     *
     * @NotNull(message = "Email không được để trống")
     */
    @NotNull(message = "Email không được để trống")
    private String email;

    /**
     * The description of the university.
     *
     * @NotNull(message = "Mô tả không được để trống")
     */
    @NotNull(message = "Mô tả không được để trống")
    private String description;

    /**
     * The phone number of the university.
     *
     * @NotNull(message = "Số điện thoại không được để trống")
     * @Pattern(regexp = "0\\d{9,10}", message = "Số điện thoại phải bắt đầu bằng số 0 và chứa 10-11 chữ số!")
     */
    @NotNull(message = "Số điện thoại không được để trống")
    @Pattern(regexp = "0\\d{9,10}", message = "Số điện thoại phải bắt đầu bằng số 0 và chứa 10-11 chữ số!")
    private String phone;

    /**
     * The specific address. This field is mandatory.
     */
    @NotNull(message = "Địa chỉ cụ thể không được để trống")
    private String specificAddress;

    /**
     * The ID of the province/city. This field is mandatory.
     */
    @NotNull(message = "Mã tỉnh / thành phố không được để trống")
    private Integer provinceId;

    /**
     * The ID of the district/town. This field is mandatory.
     */
    @NotNull(message = "Mã quận / huyện phố không được để trống")
    private Integer districtId;

    /**
     * The ID of the ward/commune. This field is mandatory.
     */
    @NotNull(message = "Mã phường / xã phố không được để trống")
    private Integer wardId;
    /**
     * The type of the university.
     *
     * @NotNull(message = "Loại trường không được để trống")
     * @EnumValue(name = "type", enumClass = UniversityType.class, message = "Loại trường phải là Công lập hoặc Tư thục")
     */
    @NotNull(message = "Loại trường không được để trống")
    @EnumValue(name = "type", enumClass = UniversityType.class, message = "Loại trường phải là Công lập hoặc Tư thục")
    private String type;

    @NotNull()
    private String avatar;
}
