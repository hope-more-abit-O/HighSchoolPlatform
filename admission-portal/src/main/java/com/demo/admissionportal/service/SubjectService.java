package com.demo.admissionportal.service;

import com.demo.admissionportal.constants.SubjectStatus;
import com.demo.admissionportal.dto.request.RequestSubjectDTO;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.dto.response.sub_entity.SubjectResponseDTO;
import com.demo.admissionportal.entity.Subject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * The interface Subject service.
 */
public interface SubjectService {
    /**
     * List all subjects with optional filters.
     *
     * @param name the subject name
     * @param status the subject status
     * @param pageable the pageable
     * @return the response data
     */
    ResponseData<Page<SubjectResponseDTO>> findAll(String name, SubjectStatus status, Pageable pageable);
    /**
     * Get subject by id.
     *
     * @param id the subject id
     * @return the response data
     */
    ResponseData<Subject> getSubjectById(Integer id);
    /**
     * Create subject request subject dto.
     *
     * @param requestSubjectDTO the request subject dto
     * @return the request subject dto
     */
    ResponseData<Subject> createSubject(RequestSubjectDTO requestSubjectDTO);
    /**
     * Delete subject by id.
     *
     * @param id the subject id
     * @return the response data
     */
    ResponseData<?> deleteSubject(Integer id);
    /**
     * Activate subject by id.
     *
     * @param id the subject id
     * @return the response data
     */
    ResponseData<?> activateSubject(Integer id);
}
