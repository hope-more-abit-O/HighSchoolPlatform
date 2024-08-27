package com.demo.admissionportal.dto.response.statistics;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;


@AllArgsConstructor
@NoArgsConstructor
@Data
@SuperBuilder
public class StatisticPostByTime extends StatisticsAdminResponseV3 implements Serializable {
    private Integer totalPosts;

}
