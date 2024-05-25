package com.demo.admissionportal.service;

import com.demo.admissionportal.dto.request.ChangePasswordRequest;

import java.security.Principal;

/**
 * The interface User service.
 */
public interface UserService {
    /**
     * Change password.
     *
     * @param request the request
     */
    void changePassword(ChangePasswordRequest request, Principal connectedUser);
}
