package com.demo.admissionportal.dto.request.redis;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * The type Regenerate otp request dto.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RegenerateOTPRequestDTO implements Serializable {
    private String email;
}
