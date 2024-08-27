package com.demo.admissionportal.dto.response.statistics;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.Date;


@AllArgsConstructor
@NoArgsConstructor
@Data
@SuperBuilder
public class StatisticInteractionByTime extends StatisticsAdminResponseV3 implements Serializable {
    private Integer interactionCount;

    public StatisticInteractionByTime(Date date, Integer interactionCount, String interactionType) {
        super(date, interactionType);
        this.interactionCount = interactionCount;
    }

}