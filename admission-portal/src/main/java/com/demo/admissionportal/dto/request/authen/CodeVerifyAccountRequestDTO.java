package com.demo.admissionportal.dto.request.authen;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * The type Code verify account request dto.
 */
@AllArgsConstructor
@Data
@NoArgsConstructor
public class CodeVerifyAccountRequestDTO implements Serializable {
    private String sUID;
}
