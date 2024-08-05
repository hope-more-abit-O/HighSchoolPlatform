package com.demo.admissionportal.controller;

import com.demo.admissionportal.constants.AdmissionStatus;
import com.demo.admissionportal.dto.entity.admission.FullAdmissionDTO;
import com.demo.admissionportal.dto.request.admisison.*;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.dto.response.admission.CreateAdmissionResponse;
import com.demo.admissionportal.exception.exceptions.DataExistedException;
import com.demo.admissionportal.exception.exceptions.ResourceNotFoundException;
import com.demo.admissionportal.exception.exceptions.StoreDataFailedException;
import com.demo.admissionportal.service.impl.admission.AdmissionServiceImpl;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/api/v1/admission")
@RequiredArgsConstructor
@SecurityRequirement(name = "BearerAuth")
public class AdmissionController {
    private final AdmissionServiceImpl admissionService;

    @PostMapping
    public ResponseEntity<ResponseData<CreateAdmissionResponse>> createAdmission(@RequestBody CreateAdmissionAndMethodsAndMajorsRequest request)
            throws ResourceNotFoundException, DataExistedException, StoreDataFailedException {
        return ResponseEntity.ok(admissionService.createAdmission(request));
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

    @PostMapping("/create")
    public ResponseEntity createAdmission(@RequestBody CreateAdmissionRequest request)
        throws DataExistedException{
        admissionService.createAdmission(request);
        return ResponseEntity.ok("Good");
    }

    @GetMapping()
    public ResponseEntity<ResponseData<Page<FullAdmissionDTO>>> getCreateAdmissionRequests(
            Pageable pageable,
            @RequestParam(required = false) Integer id,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) String source,
            @RequestParam(required = false) Integer universityId,
            @RequestParam(required = false) Date createTime,
            @RequestParam(required = false) Integer createBy,
            @RequestParam(required = false) Integer updateBy,
            @RequestParam(required = false) Date updateTime,
            @RequestParam(required = false) AdmissionStatus status
    ) {
        return ResponseEntity.ok(admissionService.getBy(
                pageable, id, year, source, universityId, createTime, createBy, updateBy, updateTime, status
        ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseData<FullAdmissionDTO>> getAdmission(@PathVariable Integer id){
        return ResponseEntity.ok(admissionService.getById(id));
    }
}
