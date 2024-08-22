package com.demo.admissionportal.dto.response.statistics;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * The type Statistics transaction response.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class StatisticsTransactionResponse implements Serializable {
    private Integer totalTransaction;
    private Integer currentTransaction;
}
