package com.demo.admissionportal.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The type Regenerate otp request dto.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RegenerateOTPRequestDTO {
    private String email;
}
