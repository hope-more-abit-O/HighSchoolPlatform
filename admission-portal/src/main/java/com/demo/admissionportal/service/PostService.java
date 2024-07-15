package com.demo.admissionportal.service;

import com.demo.admissionportal.dto.request.post.PostDeleteRequestDTO;
import com.demo.admissionportal.dto.request.post.PostRequestDTO;
import com.demo.admissionportal.dto.request.post.UpdatePostRequestDTO;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.dto.response.post.PostResponseDTO;

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
    ResponseData<PostResponseDTO> createPost(PostRequestDTO requestDTO);

    /**
     * Change status response data.
     *
     * @param requestDTO the request dto
     * @return the response data
     */
    ResponseData<String> changeStatus(PostDeleteRequestDTO requestDTO);

    /**
     * Update post response data.
     *
     * @param requestDTO the request dto
     * @return the response data
     */
    ResponseData<String> updatePost(UpdatePostRequestDTO requestDTO);

    /**
     * Gets posts.
     *
     * @return the posts
     */
    ResponseData<List<PostResponseDTO>> getPosts();

    /**
     * Gets posts by id.
     *
     * @param id the id
     * @return the posts by id
     */
    ResponseData<PostResponseDTO> getPostsById(Integer id);


    /**
     * Gets posts newest.
     *
     * @return the posts newest
     */
    ResponseData<List<PostResponseDTO>> getPostsNewest();

    /**
     * Gets posts general.
     *
     * @return the posts general
     */
    ResponseData<List<PostResponseDTO>> getPostsGeneral();
}
