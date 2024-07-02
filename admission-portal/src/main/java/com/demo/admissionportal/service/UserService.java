package com.demo.admissionportal.service;

import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.dto.response.UserResponseDTO;

/**
 * The interface User service.
 */
public interface UserService {
    /**
     * Gets user.
     *
     * @param id the id
     * @return the user
     */
    ResponseData<UserResponseDTO> getUser(Integer id);
}
