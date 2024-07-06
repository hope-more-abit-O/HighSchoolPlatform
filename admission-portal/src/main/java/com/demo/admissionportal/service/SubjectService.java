package com.demo.admissionportal.service;

import com.demo.admissionportal.dto.request.RequestSubjectDTO;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.entity.Subject;

/**
 * The interface Subject service.
 */
public interface SubjectService {
    /**
     * Create subject request subject dto.
     *
     * @param requestSubjectDTO the request subject dto
     * @return the request subject dto
     */
    ResponseData<Subject> createSubject(RequestSubjectDTO requestSubjectDTO);
}
