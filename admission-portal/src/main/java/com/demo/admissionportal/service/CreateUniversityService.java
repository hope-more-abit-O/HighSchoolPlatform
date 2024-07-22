package com.demo.admissionportal.service;

import com.demo.admissionportal.constants.CreateUniversityRequestStatus;
import com.demo.admissionportal.dto.entity.create_university_request.CreateUniversityRequestDTO;
import com.demo.admissionportal.dto.request.create_univeristy_request.CreateUniversityRequestRequest;
import com.demo.admissionportal.dto.response.PostCreateUniversityRequestResponse;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.entity.CreateUniversityRequest;
import com.demo.admissionportal.exception.ResourceNotFoundException;
import com.demo.admissionportal.exception.StoreDataFailedException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

//TODO: JAVADOC
public interface CreateUniversityService {
    ResponseData<PostCreateUniversityRequestResponse> createCreateUniversityRequest(CreateUniversityRequestRequest request);
    ResponseData adminAction(Integer id, CreateUniversityRequestStatus status, String note) throws ResourceNotFoundException, StoreDataFailedException;
    CreateUniversityRequestDTO getById(Integer id) throws ResourceNotFoundException;

    ResponseData<Page<CreateUniversityRequestDTO>> getBy(Pageable pageable,
                                                                Integer id,
                                                                String universityName,
                                                                String universityCode,
                                                                String universityEmail,
                                                                String universityUsername,
                                                                CreateUniversityRequestStatus status,
                                                                Integer createBy,
                                                                Integer confirmBy);

    ResponseData<Page<CreateUniversityRequestDTO>> getByStaff(Pageable pageable);
}
