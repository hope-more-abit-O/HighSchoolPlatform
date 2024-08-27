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
    private StatisticsAccountUserResponse user;
    private StatisticsAccountStaffResponse staff;
    private StatisticsAccountConsultantResponse consultant;
    private StatisticsAccountUniversityResponse university;

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class StatisticsAccountUserResponse implements Serializable {
        private Integer total;
        private Integer current;
        private Integer active;
        private Integer inactive;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class StatisticsAccountStaffResponse implements Serializable {
        private Integer total;
        private Integer current;
        private Integer active;
        private Integer inactive;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class StatisticsAccountConsultantResponse implements Serializable {
        private Integer total;
        private Integer current;
        private Integer active;
        private Integer inactive;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class StatisticsAccountUniversityResponse implements Serializable {
        private Integer total;
        private Integer current;
        private Integer active;
        private Integer inactive;
    }
}
