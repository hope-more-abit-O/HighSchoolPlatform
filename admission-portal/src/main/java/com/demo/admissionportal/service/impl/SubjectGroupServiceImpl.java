package com.demo.admissionportal.service.impl;


import com.demo.admissionportal.dto.request.SubjectGroupRequestDTO;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.repository.SubjectGroupRepository;
import com.demo.admissionportal.service.SubjectGroupService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

/**
 * The type Subject group service.
 */
@Service
@AllArgsConstructor
@Slf4j
public class SubjectGroupServiceImpl implements SubjectGroupService {
    private final SubjectGroupRepository subjectGroupRepository;
    private final ModelMapper modelMapper;


    @Override
    public ResponseData<?> createSubjectGroup(SubjectGroupRequestDTO request) {
        return null;
    }

    @Override
    public ResponseData<?> updateSubjectGroup(Integer id, SubjectGroupRequestDTO request) {
        return null;
    }

    @Override
    public ResponseData<?> getSubjectGroupById(Integer id) {
        return null;
    }

    @Override
    public ResponseData<?> getAllSubjectGroups() {
        return null;
    }

    @Override
    public ResponseData<?> deleteSubjectGroup(Integer id) {
        return null;
    }
}