package com.demo.admissionportal.dto.response.statistics;

import com.demo.admissionportal.dto.response.holland_test.StatisticsPostResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * The type Statistics response.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class StatisticsResponse implements Serializable {
    private StatisticsTransactionResponse transaction;
    private StatisticsInteractResponse interact;
    private StatisticsAccountResponse account;
    private StatisticsPostResponse post;
    private List<StatisticsTransactionDetailResponse> activityTransaction;
}
