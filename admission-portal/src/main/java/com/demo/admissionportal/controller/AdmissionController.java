package com.demo.admissionportal.controller;

import com.demo.admissionportal.dto.request.admisison.*;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.dto.response.admission.CreateAdmissionResponse;
import com.demo.admissionportal.exception.DataExistedException;
import com.demo.admissionportal.exception.ResourceNotFoundException;
import com.demo.admissionportal.exception.StoreDataFailedException;
import com.demo.admissionportal.service.impl.admission.AdmissionServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admission")
@RequiredArgsConstructor
public class AdmissionController {
    private final AdmissionServiceImpl admissionService;

    @PostMapping
    public ResponseEntity<ResponseData<CreateAdmissionResponse>> createAdmission(@RequestBody CreateAdmissionRequest createAdmissionRequest)
            throws ResourceNotFoundException, DataExistedException, StoreDataFailedException {
        return ResponseEntity.ok(admissionService.createAdmission(createAdmissionRequest));
    }

    @PostMapping("/training-program")
    public ResponseEntity createAdmissionTrainingProgram(@RequestBody CreateAdmissionTrainingProgramRequest request){
        return ResponseEntity.ok(admissionService.createAdmissionTrainingProgram(request));
    }

    @PostMapping("/major")
    public ResponseEntity createAdmissionMajor(@RequestBody CreateAdmissionMethodRequest request){
        return ResponseEntity.ok(admissionService.createAdmissionMethod(request));
    }

    @PostMapping("/quota")
    public ResponseEntity createAdmissionQuotas(@RequestBody CreateAdmissionTrainingProgramMethodRequest request){
        return ResponseEntity.ok(admissionService.createAdmissionTrainingProgramMethodQuota(request));
    }

    @PostMapping("/training-program/subject-group")
    public ResponseEntity createAdmissionTrainingProgramSubjectGroup(@RequestBody CreateAdmissionTrainingProgramSubjectGroupRequest request){
        return ResponseEntity.ok(admissionService.createAdmissionTrainingProgramSubjectGroup(request));
    }
}
