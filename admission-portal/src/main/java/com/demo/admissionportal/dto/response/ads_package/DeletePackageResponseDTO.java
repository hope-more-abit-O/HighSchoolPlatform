package com.demo.admissionportal.dto.response.ads_package;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * The type Delete package response dto.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DeletePackageResponseDTO implements Serializable {
    private String currentStatus;
}
