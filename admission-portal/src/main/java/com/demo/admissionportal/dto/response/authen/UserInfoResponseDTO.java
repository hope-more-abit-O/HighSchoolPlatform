package com.demo.admissionportal.dto.response.authen;

import com.demo.admissionportal.entity.District;
import com.demo.admissionportal.entity.Province;
import com.demo.admissionportal.entity.Ward;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * The type User info response dto.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoResponseDTO implements Serializable {
    private String firstName;
    private String middleName;
    private String lastName;
    private String phone;
    private String gender;
    private String specificAddress;
    private String educationLevel;
    private Province province;
    private District district;
    private Ward ward;
    private Date birthday;

}
