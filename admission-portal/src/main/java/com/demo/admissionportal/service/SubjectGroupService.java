package com.demo.admissionportal.service;


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
    ResponseData<?> createSubjectGroup(CreateSubjectGroupRequestDTO request);
    ResponseData<?> updateSubjectGroup(Integer id, UpdateSubjectGroupRequestDTO request);
    ResponseData<?> getSubjectGroupById(Integer id);
    ResponseData<Page<SubjectGroupResponseDTO>> findAll(Pageable pageable);
    ResponseData<?> deleteSubjectGroup(Integer id);
}
