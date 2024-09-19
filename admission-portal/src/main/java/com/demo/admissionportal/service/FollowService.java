package com.demo.admissionportal.service;

import com.demo.admissionportal.dto.request.follow.UpdateFollowUniRequestDTO;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.dto.response.follow.*;

import java.util.List;

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
    ResponseData<FollowResponseDTO> createFollowMajor(Integer majorId);

    /**
     * Gets follow.
     *
     * @param majorId the major id
     * @return the follow
     */
    ResponseData<FollowResponseDTO> getFollowMajor(Integer majorId);

    /**
     * Gets list follow.
     *
     * @return the list follow
     */
    ResponseData<List<UserFollowMajorResponseDTO>> getListFollowMajor();

    /**
     * Create follow uni major response data.
     *
     * @param universityMajorId the university major id
     * @return the response data
     */
    ResponseData<FollowUniMajorResponseDTO> createFollowUniMajor(Integer universityMajorId);

    /**
     * Gets follow uni major.
     *
     * @param universityMajorId the university major id
     * @return the follow uni major
     */
    ResponseData<FollowUniMajorResponseDTO> getFollowUniMajor(Integer universityMajorId);

    /**
     * Gets list follow uni major.
     *
     * @param year the year
     * @return the list follow uni major
     */
    ResponseData<List<UserFollowUniversityMajorResponseDTO>> getListFollowUniMajor(Integer year);

    /**
     * Gets list user follow major.
     *
     * @return the list user follow major
     */
    ResponseData<List<UsersFollowMajorResponseDTO>> getListUserFollowMajor();

    /**
     * Update index follow response data.
     *
     * @param updateFollowUniRequestDTOS the update follow uni request dtos
     * @return the response data
     */
    ResponseData<String> updateIndexFollow(List<UpdateFollowUniRequestDTO> updateFollowUniRequestDTOS);

    /**
     * Gets list user follow major v 2.
     *
     * @param universityId the university id
     * @return the list user follow major v 2
     */
    ResponseData<List<UsersFollowMajorResponseDTO>> getListUserFollowMajorV2(Integer universityId);
}
