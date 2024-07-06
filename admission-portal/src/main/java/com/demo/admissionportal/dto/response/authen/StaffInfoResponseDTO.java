package com.demo.admissionportal.dto.response.authen;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * The type Staff info response dto.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StaffInfoResponseDTO implements Serializable {
    private Integer adminId;
    private String firstName;
    private String middleName;
    private String lastName;
    private String phone;
}
