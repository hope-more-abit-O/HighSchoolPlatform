package com.demo.admissionportal.dto.response.statistics;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class StatisticsAdminResponseV2 {
    private List<StatisticRevenueByTime> getRevenueStatistics;
    private List<StatisticInteractionByTime> getInteractionStatistics;

    public List<StatisticRevenueByTime> getRevenueStatistics() {
        return getRevenueStatistics;
    }

    public List<StatisticInteractionByTime> getInteractionStatistics() {
        return getInteractionStatistics;
    }
}
