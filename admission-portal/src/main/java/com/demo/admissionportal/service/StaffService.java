package com.demo.admissionportal.service;

import com.demo.admissionportal.dto.request.RegisterStaffRequestDTO;
import com.demo.admissionportal.dto.request.UpdateStaffRequestDTO;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.dto.response.entity.StaffResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    ResponseData<?> getStaffById(int id);

    ResponseData<Page<StaffResponseDTO>> findAll(String username, String name, String email, String phone, Pageable pageable);

    ResponseData<StaffResponseDTO> updateStaff(UpdateStaffRequestDTO request, Integer id);

    ResponseData<?> deleteStaffById(int id);

}