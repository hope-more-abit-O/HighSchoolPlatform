package com.demo.admissionportal.service;

import com.demo.admissionportal.dto.request.RegisterAdminRequestDTO;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.entity.AdminInfo;

public interface AdminService {
    ResponseData<AdminInfo> registerAdmin(RegisterAdminRequestDTO request);
}