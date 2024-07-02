package com.demo.admissionportal.service;

import com.demo.admissionportal.dto.request.RegisterStaffRequestDTO;
import com.demo.admissionportal.dto.response.RegisterStaffResponse;
import com.demo.admissionportal.dto.response.ResponseData;

public interface StaffService {
    ResponseData<RegisterStaffResponse> registerStaff(RegisterStaffRequestDTO request);
}
