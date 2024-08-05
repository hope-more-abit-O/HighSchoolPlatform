package com.demo.admissionportal.service;

import com.demo.admissionportal.dto.entity.university_campus.UniversityCampusDTO;
import com.demo.admissionportal.dto.entity.university_campus.UniversityCampusProperties;
import com.demo.admissionportal.dto.request.university_campus.CreateCampusRequestDTO;
import com.demo.admissionportal.dto.request.university_campus.UpdateCampusRequestDTO;
import com.demo.admissionportal.dto.response.university_campus.DeleteCampusResponseDTO;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.entity.UniversityCampus;

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

    /**
     * Create university campus response data.
     *
     * @param requestDTO the request dto
     * @return the response data
     */
    ResponseData<UniversityCampus> createUniversityCampus(CreateCampusRequestDTO requestDTO);

    /**
     * Delete university campus response data.
     *
     * @param id the id
     * @return the response data
     */
    ResponseData<DeleteCampusResponseDTO> deleteUniversityCampus(Integer id);

    /**
     * Update university campus response data.
     *
     * @param campusId   the campus id
     * @param requestDTO the request dto
     * @return the response data
     */
    ResponseData<UniversityCampusProperties> updateUniversityCampus(Integer campusId, UpdateCampusRequestDTO requestDTO);

    /**
     * Change type university campus response data.
     *
     * @param campusID the campus id
     * @return the response data
     */
    ResponseData<String> changeTypeUniversityCampus(Integer campusID);
}
