package com.demo.admissionportal.dto.response;

import com.demo.admissionportal.entity.District;
import com.demo.admissionportal.entity.Province;
import com.demo.admissionportal.entity.Ward;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * The type User profile response dto.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileResponseDTO implements Serializable {
    private String firstname;
    private String lastname;
    private String middle_name;
    private String gender;
    private String email;
    private String username;
    private String birthday;
    private String phone;
    private String education_level;
    private String specificAddress;
    private Province province;
    private District district;
    private Ward ward;
    private String avatar;
}
