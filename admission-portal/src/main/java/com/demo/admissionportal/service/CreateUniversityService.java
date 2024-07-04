package com.demo.admissionportal.service;

import com.demo.admissionportal.constants.CreateUniversityRequestStatus;
import com.demo.admissionportal.dto.request.CreateUniversityRequestRequest;
import com.demo.admissionportal.dto.response.PostCreateUniversityRequestResponse;
import com.demo.admissionportal.dto.response.ResponseData;

public interface CreateUniversityService {
    public ResponseData<PostCreateUniversityRequestResponse> createCreateUniversityRequest(CreateUniversityRequestRequest request);
    public ResponseData adminAction(Integer id, CreateUniversityRequestStatus status);
}
