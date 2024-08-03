package com.demo.admissionportal.service;

import com.demo.admissionportal.dto.request.post.PostDeleteRequestDTO;
import com.demo.admissionportal.dto.request.post.PostRequestDTO;
import com.demo.admissionportal.dto.request.post.UpdatePostRequestDTO;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.dto.response.post.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
    ResponseData<PostDetailResponseDTO> createPost(PostRequestDTO requestDTO);

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
    ResponseData<PostDetailResponseDTO> getPostsById(Integer id);


    /**
     * Gets posts newest.
     *
     * @param locationId the location id
     * @return the posts newest
     */
    ResponseData<List<PostResponseDTO>> getPostsNewest(Integer locationId);

    /**
     * Gets posts general.
     *
     * @param locationId the location id
     * @return the posts general
     */
    ResponseData<List<PostResponseDTO>> getPostsGeneral(Integer locationId);

    /**
     * List post by consultant or staff or university response data.
     *
     * @param id the id
     * @return the response data
     */
    ResponseData<List<PostDetailResponseDTO>> listPostByConsultOrStaffOrUniId(Integer id);

    /**
     * List all post consul or staff or uni response data.
     *
     * @param title    the title
     * @param status   the status
     * @param pageable the pageable
     * @return the response data
     */
    ResponseData<Page<PostDetailResponseDTOV2>> listAllPostConsulOrStaff(String title, String status, Pageable pageable);

    /**
     * List post favorite response data.
     *
     * @param locationId the location id
     * @return the response data
     */
    ResponseData<List<PostFavoriteResponseDTO>> listPostFavorite(Integer locationId);

    /**
     * List post random response data.
     *
     * @param locationId the location id
     * @param pageable   the pageable
     * @return the response data
     */
    ResponseData<Page<PostRandomResponseDTO>> listPostRandom(Integer locationId, Pageable pageable);

    /**
     * Gets posts by url.
     *
     * @param url the url
     * @return the posts by url
     */
    ResponseData<PostDetailResponseDTO> getPostsByURL(String url);
}
