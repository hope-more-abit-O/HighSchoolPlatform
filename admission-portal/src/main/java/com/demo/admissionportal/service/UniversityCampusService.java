package com.demo.admissionportal.service;

import com.demo.admissionportal.dto.entity.university_campus.UniversityCampusDTO;
import com.demo.admissionportal.dto.response.ResponseData;

/**
 * The interface University campus service.
 */
public interface UniversityCampusService {
    /**
     * Gets university campus.
     *
     * @return the university campus
     */
    ResponseData<UniversityCampusDTO> getUniversityCampus();
}
