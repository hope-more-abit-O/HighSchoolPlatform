package com.demo.admissionportal.service;

import com.demo.admissionportal.dto.request.post.TypePostDeleteRequestDTO;
import com.demo.admissionportal.dto.request.post.TypePostRequestDTO;
import com.demo.admissionportal.dto.request.post.TypePostUpdateRequestDTO;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.entity.Type;

import java.util.List;

/**
 * The interface Type service.
 */
public interface TypeService {
    /**
     * Create type post type.
     *
     * @param typePostRequestDTO the type post request dto
     * @return the type
     */
    ResponseData<Type> createTypePost(TypePostRequestDTO typePostRequestDTO);

    /**
     * Gets list type post.
     *
     * @return the list type post
     */
    ResponseData<List<Type>> getListTypePost();

    /**
     * Gets post by id.
     *
     * @param id the id
     * @return the post by id
     */
    ResponseData<Type> getTypeById(Integer id);

    /**
     * Change status response data.
     *
     * @param id         the id
     * @param requestDTO the request dto
     * @return the response data
     */
    ResponseData<Type> changeStatus(Integer id , TypePostDeleteRequestDTO requestDTO);

    /**
     * Update type response data.
     *
     * @param postId     the post id
     * @param requestDTO the request dto
     * @return the response data
     */
    ResponseData<Type> updateType(Integer postId, TypePostUpdateRequestDTO requestDTO);

}
