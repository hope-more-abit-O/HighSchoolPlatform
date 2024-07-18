package com.demo.admissionportal.dto.response;

import com.demo.admissionportal.constants.AccountStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
    @Enumerated(EnumType.STRING)
    private AccountStatus currentStatus;
}
