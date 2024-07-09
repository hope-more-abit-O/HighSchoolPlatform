package com.demo.admissionportal.service;

import com.demo.admissionportal.dto.request.post.TypePostRequestDTO;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.entity.Type;

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
}
