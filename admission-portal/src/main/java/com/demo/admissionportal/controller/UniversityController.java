package com.demo.admissionportal.controller;

import com.demo.admissionportal.constants.ResponseCode;
import com.demo.admissionportal.dto.request.university.StaffRegisterUniversityRequestDTO;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.service.UniversityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/unversity")
@RequiredArgsConstructor
public class UniversityController {
    private final UniversityService universityService;

    @PostMapping("/staff")
    public ResponseData<?> registerUniversity(@RequestBody @Valid StaffRegisterUniversityRequestDTO request) {
        //if request insert null
        if (request == null) {
            return new ResponseData<>(ResponseCode.C205.getCode(), ResponseCode.C205.getMessage());
        }
        return universityService.staffCreateUniversity(request);
    }
    @PostMapping("/test/create/fail")
    public ResponseData<?> registerFailUniversity(@RequestBody @Valid StaffRegisterUniversityRequestDTO request) {
        //if request insert null
        if (request == null) {
            return new ResponseData<>(ResponseCode.C205.getCode(), ResponseCode.C205.getMessage());
        }
        return universityService.createUniversityFail(request);
    }
}
