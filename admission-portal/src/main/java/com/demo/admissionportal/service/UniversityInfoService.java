package com.demo.admissionportal.service;

import com.demo.admissionportal.entity.UniversityInfo;
import com.demo.admissionportal.exception.exceptions.ResourceNotFoundException;

public interface UniversityInfoService {
    public UniversityInfo findById(Integer id)throws ResourceNotFoundException;
}
