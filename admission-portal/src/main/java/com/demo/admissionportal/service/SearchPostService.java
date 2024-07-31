package com.demo.admissionportal.service;

import com.demo.admissionportal.dto.entity.search_engine.PostSearchDTO;
import com.demo.admissionportal.dto.response.ResponseData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * The interface Search post service.
 */
public interface SearchPostService {

    /**
     * Search post response data.
     *
     * @param title      the title
     * @param tag        the tag
     * @param schoolName the school name
     * @param code       the code
     * @param pageable   the pageable
     * @return the response data
     */
    ResponseData<Page<PostSearchDTO>> searchPost(String title, String tag, String schoolName, String code, Pageable pageable);
}
