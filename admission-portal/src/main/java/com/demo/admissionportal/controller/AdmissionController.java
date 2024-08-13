package com.demo.admissionportal.controller;

import com.demo.admissionportal.constants.AdmissionScoreStatus;
import com.demo.admissionportal.constants.AdmissionStatus;
import com.demo.admissionportal.dto.entity.admission.AdmissionSourceDTO;
import com.demo.admissionportal.dto.entity.admission.FullAdmissionDTO;
import com.demo.admissionportal.dto.entity.admission.GetAdmissionScoreResponse;
import com.demo.admissionportal.dto.request.admisison.*;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.dto.response.admission.CreateAdmissionResponse;
import com.demo.admissionportal.exception.exceptions.*;
import com.demo.admissionportal.service.impl.admission.AdmissionServiceImpl;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
    public ResponseEntity advice(@RequestParam(required = false) List<Integer> majorId,
                                 @RequestParam(required = false) Float offset,
                                 @RequestParam(required = false) Float score,
                                 @RequestParam(required = false) List<Integer> subjectGroupId,
                                 @RequestParam(required = false) List<Integer> methodId,
                                 @RequestParam(required = false) List<Integer> provinceId){
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
    public ResponseEntity<ResponseData<List<AdmissionSourceDTO>>> getAdmissionSourceV2(
            Pageable pageable,
            @RequestParam(required = false) String year,
            @RequestParam(required = false) String universityCode
    ) {
        try {
            List<Integer> years = Arrays.stream(year.split(",")).map(Integer::parseInt).toList();
            List<String> universityCodes = Arrays.stream(universityCode.split(",")).toList();
            return ResponseEntity.ok(admissionService.getSourceV2(pageable, years, universityCodes));
        } catch (Exception e) {
            throw e;
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseData<FullAdmissionDTO>> getAdmission(@PathVariable Integer id){
        return ResponseEntity.ok(admissionService.getById(id));
    }

    @PutMapping("/{id}")
    @SecurityRequirement(name = "BearerAuth")
    public ResponseEntity universityAction(@PathVariable Integer id ,@RequestBody @Valid UpdateAdmissionStatusRequest request){
        if (id == null){
            throw new BadRequestException("Id đề án không được để trống.", Map.of("error", "Id is null"));
        } else if (id <= 0)
            throw new BadRequestException("Id đề án không hợp lệ.", Map.of("error", id.toString()));
        return ResponseEntity.ok(admissionService.admissionUpdateStatus(id, request));
    }

    @PutMapping("/score")
    @SecurityRequirement(name = "BearerAuth")
    public ResponseEntity updateAdmissionScore(@RequestBody UpdateAdmissionScoreRequest request){
        return ResponseEntity.ok(admissionService.updateAdmissionScore(request));
    }

    @GetMapping("/score")
    public ResponseEntity<ResponseData<GetAdmissionScoreResponse>> getAdmissionScoreByYearAndUniversityCode(Pageable pageable,
                                                                                                            @RequestParam(required = false) String year,
                                                                                                            @RequestParam(required = false) String universityCode){
        try {
            return ResponseEntity.ok(ResponseData.ok("Lấy điểm xét tuyển thành công.", admissionService.getAdmissionScoreResponse(pageable , Arrays.stream(year.split(",")).toList().stream().map(Integer::parseInt).toList(), Arrays.stream(universityCode.split(",")).toList())) );
        } catch (SQLException e){
            throw new QueryException("Lỗi Query", Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/university/{id}/latest-training-program")
    public ResponseEntity getLatestTrainingProgram(@PathVariable Integer id){
        return ResponseEntity.ok(ResponseData.ok("Lấy thông tin chuyên ngành giảng dạy mới nhất thành công.",admissionService.getLatestTrainingProgramByUniversityId(id)));
    }

    @PutMapping("/auto/update-score-status")
    public ResponseEntity autoUpdateAdmissionScoreStatus(){
        try {
            admissionService.updateAdmissionScoreStatuses();
            return ResponseEntity.ok(ResponseData.ok("Cập nhật thông tin điểm của tất cả đề án thành công."));
        } catch (Exception e){
            throw e;
        }
    }

}
