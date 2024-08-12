package com.demo.admissionportal.dto.request;

import com.demo.admissionportal.constants.EducationLevel;
import com.demo.admissionportal.constants.Gender;
import com.demo.admissionportal.util.enum_validator.EnumPhone;
import com.demo.admissionportal.util.enum_validator.EnumValue;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
    private String firstName;
    private String middleName;
    private String lastName;
    private Date birthday;
    private String phone;
    private Gender gender;
    private String education_level;
    private String specific_address;
    private Integer province;
    private Integer district;
    private Integer ward;
    private String avatar;

}
