package com.demo.admissionportal.service;

import com.demo.admissionportal.dto.request.consultant.UniversityRegisterConsultantRequestDTO;
import com.demo.admissionportal.dto.response.ResponseData;

/**
 * Service interface for managing consultant-related operations.
 *
 * @author hopeless
 * @version 1.0
 * @since 16/06/2024
 */
public interface ConsultantService {

    /**
     * Creates a new consultant and associates them with a university.
     *
     * @param request The request containing consultant information and university ID.
     * @return A response containing the creation result and consultant data.
     */
    ResponseData<?> universityCreateConsultant(UniversityRegisterConsultantRequestDTO request);
}