package com.demo.admissionportal.service;

import com.demo.admissionportal.dto.request.ConfirmResetPasswordRequest;
import com.demo.admissionportal.dto.request.RegisterStaffRequestDTO;
import com.demo.admissionportal.dto.request.ResetPasswordRequest;
import com.demo.admissionportal.dto.request.UpdateStaffRequestDTO;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.dto.response.entity.StaffResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * The interface for the Staff service.
 */
@Service
public interface StaffService {
    /**
     * Registers a new staff.
     *
     * @param staff the staff request data transfer object
     * @return the response data
     */
    ResponseData<?> registerStaff(RegisterStaffRequestDTO staff);

    /**
     * Retrieves a staff by ID.
     *
     * @param id the staff ID
     * @return the response data
     */
    ResponseData<?> getStaffById(int id);

    /**
     * Finds all staff with optional filters.
     *
     * @param username the username filter
     * @param name the name filter
     * @param email the email filter
     * @param phone the phone filter
     * @param pageable the pagination information
     * @return the response data containing a page of staff response DTOs
     */
    ResponseData<Page<StaffResponseDTO>> findAll(String username, String name, String email, String phone, Pageable pageable);

    /**
     * Updates a staff by ID.
     *
     * @param request the update staff request data transfer object
     * @param id the staff ID
     * @return the response data
     */
    ResponseData<StaffResponseDTO> updateStaff(UpdateStaffRequestDTO request, Integer id);

    /**
     * Deletes a staff by ID.
     *
     * @param id the staff ID
     * @return the response data
     */
    ResponseData<?> deleteStaffById(int id);
}
