package com.demo.admissionportal.service;

import com.demo.admissionportal.dto.request.post.TagRequestDTO;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.entity.Tag;

/**
 * The interface Post service.
 */
public interface TagService {
    /**
     * Create tag respones data.
     *
     * @param requestDTO the request dto
     * @return the respones data
     */
    ResponseData<Tag> createTag(TagRequestDTO requestDTO);

//    ResponseData<List<Tag>> getAllTag(String name);
}
