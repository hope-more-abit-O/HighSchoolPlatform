package com.demo.admissionportal.controller;

import com.demo.admissionportal.constants.ResponseCode;
import com.demo.admissionportal.dto.request.university.RegisterUniversityRequestDTO;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.entity.University;
import com.demo.admissionportal.service.UniversityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/unversity")
@RequiredArgsConstructor
public class UniversityController {
    private final UniversityService universityService;

    @PostMapping
    public ResponseData<?> registerUniversity(@RequestBody @Valid RegisterUniversityRequestDTO request) {
        //if request insert null
        if (request == null) {
            return new ResponseData<>(ResponseCode.C205.getCode(), ResponseCode.C205.getMessage());
        }
        return universityService.registerUniversity(request);
    }
    @PostMapping("/test/create/fail")
    public ResponseData<?> registerFailUniversity(@RequestBody @Valid RegisterUniversityRequestDTO request) {
        //if request insert null
        if (request == null) {
            return new ResponseData<>(ResponseCode.C205.getCode(), ResponseCode.C205.getMessage());
        }
        return universityService.registerUniversityFail(request);
    }
}
