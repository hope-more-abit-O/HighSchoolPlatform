package com.demo.admissionportal.dto.response.university_campus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * The type Delete campus request dto.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeleteCampusResponseDTO implements Serializable {
    private String currentStatus;
}
