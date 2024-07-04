package com.demo.admissionportal.service;

import com.demo.admissionportal.constants.CreateUniversityRequestStatus;
import com.demo.admissionportal.dto.request.CreateUniversityRequestRequest;
import com.demo.admissionportal.dto.response.PostCreateUniversityRequestResponse;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.entity.CreateUniversityRequest;
import com.demo.admissionportal.exception.ResourceNotFoundException;

//TODO: JAVADOC
public interface CreateUniversityService {
    ResponseData<PostCreateUniversityRequestResponse> createCreateUniversityRequest(CreateUniversityRequestRequest request);
    ResponseData adminAction(Integer id, CreateUniversityRequestStatus status);
    CreateUniversityRequest findById(Integer id) throws ResourceNotFoundException;
}
