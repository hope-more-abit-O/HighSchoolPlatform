package com.demo.admissionportal.service;

import com.demo.admissionportal.dto.request.post.TypePostRequestDTO;
import com.demo.admissionportal.dto.request.post.TypePostUpdateRequestDTO;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.dto.response.type.TypeListResponseDTO;
import com.demo.admissionportal.entity.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
     * @param typeName the type name
     * @param status   the status
     * @param pageable the pageable
     * @return the list type post
     */
    ResponseData<Page<TypeListResponseDTO>> getListTypePost(String typeName, String status, Pageable pageable);

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
     * @param id the id
     * @return the response data
     */
    ResponseData<Type> changeStatus(Integer id);

    /**
     * Update type response data.
     *
     * @param postId     the post id
     * @param requestDTO the request dto
     * @return the response data
     */
    ResponseData<Type> updateType(Integer postId, TypePostUpdateRequestDTO requestDTO);

}
