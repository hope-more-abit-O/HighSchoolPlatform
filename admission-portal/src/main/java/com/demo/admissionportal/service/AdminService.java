package com.demo.admissionportal.service;

import com.demo.admissionportal.dto.request.RegisterAdminRequestDTO;
import com.demo.admissionportal.dto.response.ResponseData;

/**
 * The interface Admin service.
 */
public interface AdminService {
    /**
     * Create admin response data.
     *
     * @param request the request
     * @return the response data
     */
    ResponseData<?> registerAdmin(RegisterAdminRequestDTO request);
}
