package com.demo.admissionportal.dto.response.statistics;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * The type Statistics account response.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class StatisticsAccountResponse implements Serializable {
    private Integer totalAccount;
    private Integer currentAccount;
    private Integer accountActive;
    private Integer accountInactive;
}
