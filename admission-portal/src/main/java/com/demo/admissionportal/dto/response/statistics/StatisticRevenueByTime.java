package com.demo.admissionportal.dto.response.statistics;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class StatisticRevenueByTime extends StatisticsAdminResponseV3 implements Serializable{
    private Integer revenue;
}
