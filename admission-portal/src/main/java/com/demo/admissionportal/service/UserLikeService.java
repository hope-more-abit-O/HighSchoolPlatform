package com.demo.admissionportal.service;

import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.dto.response.like.LikeResponseDTO;
import com.demo.admissionportal.dto.response.like.TotalLikeResponseDTO;

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
     * Gets like.
     *
     * @param postId the post id
     * @return the like
     */
    ResponseData<LikeResponseDTO> getLike(Integer postId);

    /**
     * Gets total like.
     *
     * @param postId the post id
     * @return the total like
     */
    ResponseData<TotalLikeResponseDTO> getTotalLike(Integer postId);
}
