package com.demo.admissionportal.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * The type Verify update university request dto.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VerifyUpdateUniversityRequestDTO implements Serializable {
    private Integer universityId;
    private String email;
    private String otpFromEmail;
}
