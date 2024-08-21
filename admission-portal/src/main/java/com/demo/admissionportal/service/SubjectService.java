package com.demo.admissionportal.service;

import com.demo.admissionportal.constants.SubjectStatus;
import com.demo.admissionportal.dto.entity.SubjectDTO;
import com.demo.admissionportal.dto.request.RequestSubjectDTO;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.dto.response.sub_entity.SubjectResponseDTO;
import com.demo.admissionportal.entity.Subject;
import com.demo.admissionportal.exception.exceptions.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * The interface Subject service.
 */
public interface SubjectService {
    /**
     * List all subjects with optional filters.
     *
     * @param name     the subject name
     * @param status   the subject status
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
    ResponseData<SubjectResponseDTO> getSubjectById(Integer id);

    /**
     * Create subject request subject dto.
     *
     * @param requestSubjectDTO the request subject dto
     * @return the request subject dto
     */
    ResponseData<Subject> createSubject(RequestSubjectDTO requestSubjectDTO);

    /**
     * Activate subject by id.
     *
     * @param id the subject id
     * @return the response data
     * @throws ResourceNotFoundException the resource not found exception
     */
//TODO: JAVADOC
    Subject findById(Integer id) throws ResourceNotFoundException;

    /**
     * Delete subject response data.
     *
     * @param id the id
     * @return the response data
     */
    ResponseData<?> deleteSubject(Integer id);

    /**
     * Activate subject response data.
     *
     * @param id the id
     * @return the response data
     */
    ResponseData<?> activateSubject(Integer id);

    List<SubjectDTO> getAllActive();
}
