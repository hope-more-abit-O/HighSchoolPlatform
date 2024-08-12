package com.demo.admissionportal.dto.response.campaign;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * The type Campaign status response dto.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CampaignStatusResponseDTO implements Serializable {
    private String currentStatus;
}
