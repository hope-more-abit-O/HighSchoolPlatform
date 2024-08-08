package com.demo.admissionportal.service;

import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.dto.response.like.LikeResponseDTO;

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
     * Gets like.
     *
     * @param universityID the university id
     * @return the like
     */
    ResponseData<List<LikeResponseDTO>> getLike(Integer universityID);
}
