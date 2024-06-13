package com.demo.admissionportal.service;

import com.demo.admissionportal.dto.request.university.RegisterUniversityRequestDTO;
import com.demo.admissionportal.dto.response.ResponseData;

public interface UniversityService {

    public ResponseData<?> registerUniversity(RegisterUniversityRequestDTO request);
    public ResponseData<?> registerUniversityFail(RegisterUniversityRequestDTO request);
}
