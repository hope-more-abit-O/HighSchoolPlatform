package com.demo.admissionportal.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * The type Change status user response dto.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ChangeStatusUserResponseDTO implements Serializable {
    private String currentStatus;
}
