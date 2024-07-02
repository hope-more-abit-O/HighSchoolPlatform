package com.demo.admissionportal.service;

import com.demo.admissionportal.dto.request.RegisterStaffRequestDTO;
import com.demo.admissionportal.dto.response.RegisterStaffResponse;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.dto.response.StaffResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StaffService {
    ResponseData<RegisterStaffResponse> registerStaff(RegisterStaffRequestDTO request);
    ResponseData<Page<StaffResponseDTO>> findAll(String username, String name, String email, String phone, Pageable pageable);
}
