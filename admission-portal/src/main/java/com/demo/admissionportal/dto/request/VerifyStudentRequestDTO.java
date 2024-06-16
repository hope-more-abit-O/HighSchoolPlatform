package com.demo.admissionportal.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * The type Verify student request dto.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VerifyStudentRequestDTO implements Serializable {
    private String email;
    private String otpFromEmail;
}
