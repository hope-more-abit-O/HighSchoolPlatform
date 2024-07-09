package com.demo.admissionportal.service;

import com.demo.admissionportal.dto.request.RegisterAdminRequestDTO;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.entity.AdminInfo;

/**
 * The interface Admin service.
 */
public interface AdminService {
    /**
     * Register admin response data.
     *
     * @param request the request
     * @return the response data
     */
    ResponseData<AdminInfo> registerAdmin(RegisterAdminRequestDTO request);
}