package com.demo.admissionportal.dto.response.payment;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The type Payment response dto.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateQrResponseDTO {
    private String message;
    private JsonNode info;
    private Integer universityTransactionId;
}
