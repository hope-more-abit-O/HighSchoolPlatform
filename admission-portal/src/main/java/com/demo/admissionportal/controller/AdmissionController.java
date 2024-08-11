package com.demo.admissionportal.controller;

import com.demo.admissionportal.constants.AdmissionScoreStatus;
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
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/v1/admission")
@RequiredArgsConstructor
public class AdmissionController {
    private final AdmissionServiceImpl admissionService;

    @PostMapping
    @SecurityRequirement(name = "BearerAuth")
    public ResponseEntity<ResponseData<CreateAdmissionResponse>> createAdmission(@RequestBody CreateAdmissionAndMethodsAndMajorsRequest request)
            throws ResourceNotFoundException, DataExistedException, StoreDataFailedException {
        return ResponseEntity.ok(admissionService.createAdmission(request));
    }

//    @PostMapping("/training-program")
//    @SecurityRequirement(name = "BearerAuth")
//    public ResponseEntity createAdmissionTrainingProgram(@RequestBody CreateAdmissionTrainingProgramRequest request){
//        return ResponseEntity.ok(admissionService.createAdmissionTrainingProgram(request));
//    }
//
//    @PostMapping("/major")
//    @SecurityRequirement(name = "BearerAuth")
//    public ResponseEntity createAdmissionMajor(@RequestBody CreateAdmissionMethodRequest request){
//        return ResponseEntity.ok(admissionService.createAdmissionMethod(request));
//    }
//
//    @PostMapping("/quota")
//    @SecurityRequirement(name = "BearerAuth")
//    public ResponseEntity createAdmissionQuotas(@RequestBody CreateAdmissionTrainingProgramMethodRequest request){
//        return ResponseEntity.ok(admissionService.createAdmissionTrainingProgramMethodQuota(request));
//    }
//
//    @PostMapping("/training-program/subject-group")
//    @SecurityRequirement(name = "BearerAuth")
//    public ResponseEntity createAdmissionTrainingProgramSubjectGroup(@RequestBody CreateAdmissionTrainingProgramSubjectGroupRequest request){
//        return ResponseEntity.ok(admissionService.createAdmissionTrainingProgramSubjectGroup(request));
//    }
    @GetMapping("/score-advice")
    public ResponseEntity advice(@RequestParam String majorId, @RequestParam Float offset, @RequestParam Float score, @RequestParam Integer subjectGroupId, @RequestParam Integer methodId, @RequestParam Integer provinceId){
        return ResponseEntity.ok(admissionService.adviceSchool(new SchoolAdviceRequest(majorId, offset, score, subjectGroupId, methodId, provinceId)));
    }

    @PostMapping("/create")
    @SecurityRequirement(name = "BearerAuth")
    public ResponseEntity createAdmission(@RequestBody CreateAdmissionRequest request)
        throws DataExistedException{
        try{
            admissionService.createAdmission(request);
            return ResponseEntity.ok(ResponseData.ok("Tạo đề án thành công."));
        }catch (Exception e){
            throw new DataExistedException(e.getMessage());
        }
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
            @RequestParam(required = false) AdmissionStatus status,
            @RequestParam(required = false) AdmissionScoreStatus scoreStatus
    ) {
        return ResponseEntity.ok(admissionService.getBy(
                pageable, id, year, source, universityId, createTime, createBy, updateBy, updateTime, status, scoreStatus
        ));
    }

    @GetMapping("/source")
    public ResponseEntity<ResponseData<List<String>>> getAdmissionSource(
            @RequestParam(required = true) Integer year,
            @RequestParam(required = true) String universityCode
    ) {
        return ResponseEntity.ok(admissionService.getSource(year, universityCode));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseData<FullAdmissionDTO>> getAdmission(@PathVariable Integer id){
        return ResponseEntity.ok(admissionService.getById(id));
    }

    @PatchMapping
    @SecurityRequirement(name = "BearerAuth")
    public ResponseEntity universityAction(@RequestBody @Valid UpdateAdmissionStatusRequest request){
        return ResponseEntity.ok(admissionService.universityUpdateStatus(request));
    }

    @PutMapping("/score")
    @SecurityRequirement(name = "BearerAuth")
    public ResponseEntity updateAdmissionScore(@RequestBody UpdateAdmissionScoreRequest request){
        return ResponseEntity.ok(admissionService.updateAdmissionScore(request));
    }

    @GetMapping("/score")
    public ResponseEntity getAdmissionScoreByYearAndUniversityCode(@RequestParam Integer year, @RequestParam String universityCode){
        return ResponseEntity.ok(admissionService.getAdmissionScore(year, universityCode, 1));
    }

    @GetMapping("/university/{id}/latest-training-program")
    public ResponseEntity getLatestTrainingProgram(@PathVariable Integer id){
        return ResponseEntity.ok(ResponseData.ok("Lấy thông tin chuyên ngành giảng dạy mới nhất thành công.",admissionService.getLatestTrainingProgramByUniversityId(id)));
    }

    @PatchMapping("/auto/update-score-status")
    public ResponseEntity autoUpdateAdmissionScoreStatus(){
        try {
            admissionService.updateAdmissionScoreStatuses();
            return ResponseEntity.ok(ResponseData.ok("Cập nhập thông tin điểm của tất cả đề án thành công."));
        } catch (Exception e){
            throw e;
        }
    }

}
