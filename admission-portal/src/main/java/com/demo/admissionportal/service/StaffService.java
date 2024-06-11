package com.demo.admissionportal.service;

import com.demo.admissionportal.dto.request.RegisterStaffRequestDTO;
import com.demo.admissionportal.dto.response.ResponseData;
import org.springframework.stereotype.Service;

/**
 * The interface Staff service.
 */
@Service
public interface StaffService {
    /**
     * Register staff response data.
     *
     * @param staff the staff
     * @return the response data
     */
    ResponseData<?> registerStaff(RegisterStaffRequestDTO staff);
}
