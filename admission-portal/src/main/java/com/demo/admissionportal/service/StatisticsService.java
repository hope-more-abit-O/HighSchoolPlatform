package com.demo.admissionportal.service;

import com.demo.admissionportal.dto.response.ResponseData;

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
}
