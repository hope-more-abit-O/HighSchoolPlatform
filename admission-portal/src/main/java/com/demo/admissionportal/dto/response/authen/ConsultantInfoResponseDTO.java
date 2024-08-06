package com.demo.admissionportal.dto.response.authen;

import com.demo.admissionportal.constants.Gender;
import com.demo.admissionportal.entity.District;
import com.demo.admissionportal.entity.Province;
import com.demo.admissionportal.entity.Ward;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * The type Consultant info response dto.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConsultantInfoResponseDTO implements Serializable {
    private Integer universityId;
    private String firstname;
    private String middleName;
    private String lastName;
    private String phone;
    private String specificAddress;
    private Gender gender;
    private Province province;
    private District district;
    private Ward ward;
    private String avatarUniversity;
    private String fullNameUniversity;
}
