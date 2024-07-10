package com.demo.admissionportal.service;

import com.demo.admissionportal.dto.request.post.PostDeleteRequestDTO;
import com.demo.admissionportal.dto.request.post.PostRequestDTO;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.entity.sub_entity.PostType;

import java.util.List;

/**
 * The interface Post service.
 */
public interface PostService {
    /**
     * Create a post.
     *
     * @param requestDTO the request dto
     * @return the post
     */
    ResponseData<List<PostType>> createPost(PostRequestDTO requestDTO);

    /**
     * Delete post response data.
     *
     * @param requestDTO the request dto
     * @return the response data
     */
    ResponseData<String> deletePost(PostDeleteRequestDTO requestDTO);
}
