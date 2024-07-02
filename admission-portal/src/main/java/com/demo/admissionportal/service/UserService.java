package com.demo.admissionportal.service;

import com.demo.admissionportal.dto.response.ResponseData;
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
     * @return the user
     */
    ResponseData<List<UserResponseDTO>> getUser();

    /**
     * Gets user by id.
     *
     * @param id the id
     * @return the user by id
     */
    ResponseData<UserProfileResponseDTO> getUserById(Integer id);
}
