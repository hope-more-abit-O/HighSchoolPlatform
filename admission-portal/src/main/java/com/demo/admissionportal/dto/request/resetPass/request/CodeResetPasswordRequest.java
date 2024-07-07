package com.demo.admissionportal.dto.request.resetPass.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

/**
 * The type Code verify account request dto.
 */
@AllArgsConstructor
@Data
@NoArgsConstructor
public class CodeResetPasswordRequest implements Serializable {
    private UUID sUID;
}
