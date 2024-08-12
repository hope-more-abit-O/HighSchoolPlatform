package com.demo.admissionportal.dto.request;

import com.demo.admissionportal.constants.EducationLevel;
import com.demo.admissionportal.constants.Gender;
import com.demo.admissionportal.util.enum_validator.EnumName;
import com.demo.admissionportal.util.enum_validator.EnumPhone;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.Valid;
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
    @EnumName(message = "Họ tên chỉ chứa chữ")
    private String firstName;
    @EnumName(message = "Họ tên chỉ chứa chữ")
    private String middleName;
    @EnumName(message = "Họ tên chỉ chứa chữ")
    private String lastName;
    @PastOrPresent(message = "Ngày sinh không được vượt ngày hiện tại")
    private Date birthday;
    @EnumPhone(message = "Số điện thoại từ 10 - 11")
    private String phone;
    private Gender gender;
    @Enumerated(EnumType.STRING)
    private EducationLevel education_level;
    private String specific_address;
    private Integer province;
    private Integer district;
    private Integer ward;
    private String avatar;

}
