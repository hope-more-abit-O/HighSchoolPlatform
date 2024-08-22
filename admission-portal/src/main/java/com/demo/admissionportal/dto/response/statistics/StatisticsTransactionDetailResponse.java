package com.demo.admissionportal.dto.response.statistics;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * The type Statistics transaction detail response.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class StatisticsTransactionDetailResponse implements Serializable {
    private String createBy;
    private Integer price;
}
