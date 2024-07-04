package com.demo.admissionportal.dto.request.redis;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * The type Verify account request dto.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VerifyAccountRequestDTO implements Serializable {
    private String email;
    private String otpFromEmail;
}