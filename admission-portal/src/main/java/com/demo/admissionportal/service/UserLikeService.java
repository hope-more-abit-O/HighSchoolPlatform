package com.demo.admissionportal.service;

import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.dto.response.like.LikeResponseDTO;
import com.demo.admissionportal.dto.response.like.TotalLikeResponseDTO;

import java.util.List;

/**
 * The interface User like service.
 */
public interface UserLikeService {
    /**
     * Create like response data.
     *
     * @param postID the post id
     * @return the response data
     */
    ResponseData<LikeResponseDTO> createLike(Integer postID);

    /**
     * Gets total like.
     *
     * @param postId the post id
     * @return the total like
     */
    ResponseData<TotalLikeResponseDTO> getTotalLike(Integer postId);

    /**
     * Gets like by university.
     *
     * @param universityID the university id
     * @return the like by university
     */
    ResponseData<List<LikeResponseDTO>> getLikeByUniversity(Integer universityID);
}
