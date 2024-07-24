package com.demo.admissionportal.service;


import com.demo.admissionportal.constants.SubjectStatus;
import com.demo.admissionportal.dto.request.UpdateSubjectGroupRequestDTO;
import com.demo.admissionportal.dto.request.CreateSubjectGroupRequestDTO;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.dto.response.SubjectGroupResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * The interface Subject group service.
 */
public interface SubjectGroupService {
    /**
     * Create subject group response data.
     *
     * @param request the request
     * @return the response data
     */
    ResponseData<?> createSubjectGroup(CreateSubjectGroupRequestDTO request);

    /**
     * Update subject group response data.
     *
     * @param id      the id
     * @param request the request
     * @return the response data
     */
    ResponseData<?> updateSubjectGroup(Integer id, UpdateSubjectGroupRequestDTO request);

    /**
     * Gets subject group by id.
     *
     * @param id the id
     * @return the subject group by id
     */
    ResponseData<?> getSubjectGroupById(Integer id);

    /**
     * Find all response data.
     *
     * @param groupName   the group name
     * @param subjectName the subject name
     * @param status      the status
     * @param pageable    the pageable
     * @return the response data
     */
    ResponseData<Page<SubjectGroupResponseDTO>> findAll(String groupName, String subjectName, SubjectStatus status, Pageable pageable);

    /**
     * Delete subject group response data.
     *
     * @param id the id
     * @return the response data
     */
    ResponseData<?> deleteSubjectGroup(Integer id);

    /**
     * Activate subject group response data.
     *
     * @param id the id
     * @return the response data
     */
    ResponseData<?> activateSubjectGroup(Integer id);
}
