package com.demo.admissionportal.service;

import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.dto.response.follow.FollowResponseDTO;

/**
 * The interface Follow service.
 */
public interface FollowService {
    /**
     * Create follow response data.
     *
     * @param majorId the major id
     * @return the response data
     */
    ResponseData<FollowResponseDTO> createFollow(Integer majorId);
}
