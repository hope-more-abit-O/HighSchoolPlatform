package com.demo.admissionportal.dto.request;

import com.demo.admissionportal.constants.EducationLevel;
import com.demo.admissionportal.constants.Gender;
import com.demo.admissionportal.util.enum_validator.EnumPhone;
import com.demo.admissionportal.util.enum_validator.EnumValue;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * The type Update user request dto.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Valid
public class UpdateUserRequestDTO implements Serializable {
    @NotNull(message = "Họ tên không được để trống")
    private String firstName;

    @NotNull(message = "Tên đệm không được để trống")
    private String middleName;

    @NotNull(message = "Tên không được để trống")
    private String lastName;

    @PastOrPresent(message = "Ngày sinh không được vượt ngày hiện tại")
    @Temporal(TemporalType.DATE)
    @NotNull(message = "Ngày sinh không được để trống")
    private Date birthday;

    @NotNull(message = "Số điện thoại không được để trống")
    @EnumPhone(message = "Số điện thoại từ 10 - 11")
    private String phone;

    @NotNull(message = "Giới tính không thể để trống")
    @EnumValue(name = "type", enumClass = Gender.class, message = "Giới tính phải đúng format")
    private String gender;

    @NotNull(message = "Trình độ học vấn không được trống")
    @EnumValue(name = "type", enumClass = EducationLevel.class, message = "Trình độ học vấn phải đúng format")
    private String education_level;

    @NotNull(message = "Số nhà, tên đường không được để trống")
    private String specific_address;

    private Integer province;
    private Integer district;
    private Integer ward;

    @NotNull(message = "Avatar không được để trống")
    private String avatar;
}
