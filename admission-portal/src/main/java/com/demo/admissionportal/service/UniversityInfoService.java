package com.demo.admissionportal.service;

import com.demo.admissionportal.entity.UniversityInfo;
import com.demo.admissionportal.exception.exceptions.ResourceNotFoundException;

import java.util.Collection;
import java.util.List;

public interface UniversityInfoService {
    public UniversityInfo findById(Integer id)throws ResourceNotFoundException;

    List<UniversityInfo> findByIds(List<Integer> universityIds);

}