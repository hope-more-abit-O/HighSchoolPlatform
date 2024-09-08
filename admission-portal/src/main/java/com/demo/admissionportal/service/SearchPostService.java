package com.demo.admissionportal.service;

import com.demo.admissionportal.dto.entity.search_engine.PostSearchDTO;
import com.demo.admissionportal.dto.response.ResponseData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * The interface Search post service.
 */
public interface SearchPostService {

    /**
     * Search post response data.
     *
     * @param content  the content
     * @param pageable the pageable
     * @return the response data
     */
    ResponseData<Page<PostSearchDTO.PostSearch>> searchPost(String content, Pageable pageable);

    /**
     * Search filter post response data.
     *
     * @param content    the content
     * @param typeId     the type id
     * @param locationId the location id
     * @param startDate  the start date
     * @param endDate    the end date
     * @param authorId   the author id
     * @param pageable   the pageable
     * @return the response data
     */
    ResponseData<Map<String, Object>> searchFilterPostV2(String content, List<Integer> typeId, List<Integer> locationId, LocalDate startDate, LocalDate endDate, List<Integer> authorId, Pageable pageable);
}
