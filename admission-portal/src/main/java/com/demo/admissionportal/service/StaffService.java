package com.demo.admissionportal.service;

import com.demo.admissionportal.constants.AccountStatus;
import com.demo.admissionportal.dto.request.ActiveStaffRequest;
import com.demo.admissionportal.dto.request.DeleteStaffRequest;
import com.demo.admissionportal.dto.request.RegisterStaffRequestDTO;
import com.demo.admissionportal.dto.request.UpdateStaffRequestDTO;
import com.demo.admissionportal.dto.response.RegisterStaffResponse;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.dto.response.StaffResponseDTO;
import com.demo.admissionportal.entity.StaffInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StaffService {
    ResponseData<RegisterStaffResponse> registerStaff(RegisterStaffRequestDTO request);
    ResponseData<Page<StaffResponseDTO>> findAll(String username, String firstName, String middleName, String lastName, String email, String phone, AccountStatus status, Pageable pageable);
    ResponseData<?> getStaffById(int id);
    ResponseData<StaffResponseDTO> updateStaff(UpdateStaffRequestDTO request, Integer id);
    ResponseData<?> deleteStaffById(int id, DeleteStaffRequest request);
    ResponseData<?> activateStaffById(int id, ActiveStaffRequest request);
}