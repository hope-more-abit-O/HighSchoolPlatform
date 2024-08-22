package com.demo.admissionportal.dto.response.statistics;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * The type Statistics interact response.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class StatisticsInteractResponse implements Serializable {
    private Integer totalInteraction;
    private Integer currentInteraction;
}
