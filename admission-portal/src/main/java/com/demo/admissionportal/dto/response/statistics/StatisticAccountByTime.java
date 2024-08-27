package com.demo.admissionportal.dto.response.statistics;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@SuperBuilder
public class StatisticAccountByTime extends StatisticsAdminResponseV3 implements Serializable {
    private Integer totalAccount;

    public StatisticAccountByTime(Date date, Integer totalAccount) {
        super(date, "ACCOUNT");
        this.totalAccount = totalAccount;
    }
}
