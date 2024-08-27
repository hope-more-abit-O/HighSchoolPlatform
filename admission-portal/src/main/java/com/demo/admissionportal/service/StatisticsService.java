package com.demo.admissionportal.service;

import com.demo.admissionportal.dto.response.ResponseData;

import java.util.Date;

/**
 * The interface Statistics service.
 */
public interface StatisticsService {
    /**
     * Gets statistics.
     *
     * @return the statistics
     */
    ResponseData<?> getStatistics();

    ResponseData<?> getStatisticsV2(Date startDay, Date endDay, String type, String role, String status, String period);
}
