package com.demo.admissionportal.service;


import com.demo.admissionportal.dto.request.SubjectGroupRequestDTO;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.entity.SubjectGroup;

import java.util.List;

/**
 * The interface Subject group service.
 */
public interface SubjectGroupService {
    ResponseData<?> createSubjectGroup(SubjectGroupRequestDTO request);
    ResponseData<?> updateSubjectGroup(Integer id, SubjectGroupRequestDTO request);
    ResponseData<?> getSubjectGroupById(Integer id);
    ResponseData<?> getAllSubjectGroups();
    ResponseData<?> deleteSubjectGroup(Integer id);
}
