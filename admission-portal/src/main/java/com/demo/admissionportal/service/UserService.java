package com.demo.admissionportal.service;

import com.demo.admissionportal.dto.request.ChangeStatusUserRequestDTO;
import com.demo.admissionportal.dto.request.UpdateUserRequestDTO;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.dto.response.UpdateUserResponseDTO;
import com.demo.admissionportal.dto.response.UserProfileResponseDTO;
import com.demo.admissionportal.dto.response.UserResponseDTO;

import java.util.List;

/**
 * The interface User service.
 */
public interface UserService {

    /**
     * Gets user.
     *
     * @param username the username
     * @param email    the email
     * @return the user
     */
    ResponseData<List<UserResponseDTO>> getUser(String username, String email);

    /**
     * Gets user by id.
     *
     * @param id the id
     * @return the user by id
     */
    ResponseData<UserProfileResponseDTO> getUserById(Integer id);

    /**
     * Update user response data.
     *
     * @param id         the id
     * @param requestDTO the request dto
     * @return the response data
     */
    ResponseData<UpdateUserResponseDTO> updateUser(Integer id, UpdateUserRequestDTO requestDTO);

    /**
     * Change status response data.
     *
     * @param id         the id
     * @param requestDTO the request dto
     * @return the response data
     */
    ResponseData<ChangeStatusUserRequestDTO> changeStatus(Integer id, ChangeStatusUserRequestDTO requestDTO);
}
