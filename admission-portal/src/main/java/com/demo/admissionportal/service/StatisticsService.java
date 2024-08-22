package com.demo.admissionportal.service;

import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.dto.response.statistics.StatisticsResponse;

/**
 * The interface Statistics service.
 */
public interface StatisticsService {
    /**
     * Gets statistics.
     *
     * @return the statistics
     */
    ResponseData<StatisticsResponse> getStatistics();
}
