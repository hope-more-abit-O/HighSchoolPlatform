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

/**
 * The interface Staff service.
 */
public interface StaffService {
    /**
     * Register staff response data.
     *
     * @param request the request
     * @return the response data
     */
    ResponseData registerStaff(RegisterStaffRequestDTO request);

    /**
     * Find all response data.
     *
     * @param username   the username
     * @param firstName  the first name
     * @param middleName the middle name
     * @param lastName   the last name
     * @param email      the email
     * @param phone      the phone
     * @param status     the status
     * @param pageable   the pageable
     * @return the response data
     */
    ResponseData<Page<StaffResponseDTO>> findAll(String username, String firstName, String middleName, String lastName, String email, String phone, AccountStatus status, Pageable pageable);

    /**
     * Gets staff by id.
     *
     * @param id the id
     * @return the staff by id
     */
    ResponseData<?> getStaffById(int id);

    /**
     * Update staff response data.
     *
     * @param request the request
     * @param id      the id
     * @return the response data
     */
    ResponseData<StaffResponseDTO> updateStaff(UpdateStaffRequestDTO request, Integer id);

    /**
     * Delete staff by id response data.
     *
     * @param id      the id
     * @param request the request
     * @return the response data
     */
    ResponseData<?> deleteStaffById(int id, DeleteStaffRequest request);

    /**
     * Activate staff by id response data.
     *
     * @param id      the id
     * @param request the request
     * @return the response data
     */
    ResponseData<?> activateStaffById(int id, ActiveStaffRequest request);
}