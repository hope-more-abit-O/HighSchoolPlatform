package com.demo.admissionportal.dto.response.authen;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * The type Admin info response dto.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminInfoResponseDTO implements Serializable {
    private String firstName;
    private String middleName;
    private String lastName;
    private String phone;
}
