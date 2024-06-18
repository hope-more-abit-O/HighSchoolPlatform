package com.demo.admissionportal.service;

import com.demo.admissionportal.dto.request.LoginRequestDTO;
import com.demo.admissionportal.dto.response.LoginResponseDTO;
import com.demo.admissionportal.dto.response.ResponseData;

/**
 * The interface Authentication university service.
 */
public interface AuthenticationUniversityService {
    /**
     * Login response data.
     *
     * @param request the request
     * @return the response data
     */
    ResponseData<LoginResponseDTO> login(LoginRequestDTO request);
}
