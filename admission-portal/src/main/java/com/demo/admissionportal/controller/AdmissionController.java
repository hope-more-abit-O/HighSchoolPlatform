package com.demo.admissionportal.controller;

import com.demo.admissionportal.constants.AdmissionConfirmStatus;
import com.demo.admissionportal.constants.AdmissionScoreStatus;
import com.demo.admissionportal.constants.AdmissionStatus;
import com.demo.admissionportal.dto.entity.admission.FullAdmissionDTO;
import com.demo.admissionportal.dto.entity.admission.GetAdmissionScoreResponse;
import com.demo.admissionportal.dto.entity.admission.SchoolDirectoryRequest;
import com.demo.admissionportal.dto.request.admisison.*;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.dto.response.admission.SearchAdmissionResponse;
import com.demo.admissionportal.exception.exceptions.*;
import com.demo.admissionportal.service.impl.admission.AdmissionServiceImpl;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.webmvc.core.service.RequestService;
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
    private final RequestService requestBuilder;

    @GetMapping("/score-advice")
    public ResponseEntity advice(@RequestParam(required = false) String majorId,
                                 @RequestParam(required = false) Float offset,
                                 @RequestParam(required = false) Float score,
                                 @RequestParam(required = false) String subjectGroupId,
                                 @RequestParam(required = false) String methodId,
                                 @RequestParam(required = false) String provinceId){
        try {
            List<Integer> majorIds = null;
            List<Integer> subjectGroupIds = null;
            List<Integer> methodIds = null;
            List<Integer> provinceIds = null;

            if (majorId != null && !majorId.isEmpty()) {
                majorIds = Arrays.stream(majorId.split(",")).map(Integer::parseInt).toList();
            }
            if (subjectGroupId != null && !subjectGroupId.isEmpty()) {
                subjectGroupIds = Arrays.stream(subjectGroupId.split(",")).map(Integer::parseInt).toList();
            }
            if (methodId != null && !methodId.isEmpty()) {
                methodIds = Arrays.stream(methodId.split(",")).map(Integer::parseInt).toList();
            }
            if (provinceId  != null && !provinceId.isEmpty()) {
                provinceIds = Arrays.stream(provinceId.split(",")).map(Integer::parseInt).toList();
            }
            return ResponseEntity.ok(admissionService.adviceSchool(new SchoolAdviceRequest(majorIds, offset, score, subjectGroupIds, methodIds, provinceIds)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/score-advice/v2")
    public ResponseEntity adviceV2(
            @RequestParam(required = true) Integer pageNumber,
            @RequestParam(required = true) Integer pageSize,
            @RequestParam(required = false) String majorId,
            @RequestParam(required = false) Float fromScore,
            @RequestParam(required = false) Float toScore,
            @RequestParam(required = false) String subjectId,
            @RequestParam(required = false) String methodId,
            @RequestParam(required = false) String region,
            @RequestParam(required = false) String provinceId
    ){
        try {
            List<Integer> majorIds = null;
            List<Integer> subjectIds = null;
            List<Integer> methodIds = null;
            List<Integer> provinceIds = null;
            List<String> regions = null;

            if (majorId != null && !majorId.isEmpty()) {
                majorIds = Arrays.stream(majorId.split(",")).map(Integer::parseInt).toList();
            }
            if (subjectId != null && !subjectId.isEmpty()) {
                subjectIds = Arrays.stream(subjectId.split(",")).map(Integer::parseInt).toList();
            }
            if (methodId != null && !methodId.isEmpty()) {
                methodIds = Arrays.stream(methodId.split(",")).map(Integer::parseInt).toList();
            }
            if (provinceId  != null && !provinceId.isEmpty()) {
                provinceIds = Arrays.stream(provinceId.split(",")).map(Integer::parseInt).toList();
            }
            if (region  != null && !region.isEmpty()) {
                regions = Arrays.stream(region.split(",")).toList();
            }
            return ResponseEntity.ok(admissionService.adviceSchoolV2(new SchoolAdviceRequestV2(majorIds, fromScore, toScore, regions, subjectIds, methodIds, provinceIds, pageNumber, pageSize)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/create")
    @SecurityRequirement(name = "BearerAuth")
    public ResponseEntity createAdmission(@RequestBody @Valid CreateAdmissionRequest request)
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
            @RequestParam(required = false) Integer staffId,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) String source,
            @RequestParam(required = false) Integer universityId,
            @RequestParam(required = false) Date createTime,
            @RequestParam(required = false) Integer createBy,
            @RequestParam(required = false) Integer updateBy,
            @RequestParam(required = false) Date updateTime,
            @RequestParam(required = false) List<AdmissionStatus> status,
            @RequestParam(required = false) List<AdmissionScoreStatus> scoreStatus,
            @RequestParam(required = false) List<AdmissionConfirmStatus> confirmStatus
    ) {
        return ResponseEntity.ok(admissionService.getByV2(
                pageable, id, staffId, year, source, universityId, createTime, createBy, updateBy, updateTime, status, scoreStatus, confirmStatus
        ));
    }

    @GetMapping("/search")
    public ResponseEntity<ResponseData<SearchAdmissionResponse>> searchAdmission(
            Pageable pageable,
            @RequestParam(required = false) String year,
            @RequestParam(required = false) String universityCode,
            @RequestParam(required = false) Integer staffId,
            @RequestParam(required = false) List<AdmissionStatus> status,
            @RequestParam(required = false) List<AdmissionConfirmStatus> confirmStatus,
            @RequestParam(required = false) List<AdmissionScoreStatus> scoreStatus
    ) {
        try {
            List<Integer> years = null;
            List<String> universityCodes = null;
            if (year != null &&  !year.isEmpty()) years = Arrays.stream(year.split(",")).map(Integer::parseInt).toList();
            if (universityCode != null && !universityCode.isEmpty()) universityCodes = Arrays.stream(universityCode.split(",")).toList();;
            return ResponseEntity.ok( ResponseData.ok("Lấy tài liệu thành công.", new SearchAdmissionResponse(admissionService.searchAdmission(pageable, years, universityCodes, staffId, status))));
        } catch (ResourceNotFoundException | QueryException e) {
            throw e;
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseData<FullAdmissionDTO>> getAdmission(@PathVariable Integer id){
        return ResponseEntity.ok(admissionService.getByIdV2(id));
    }

    @PutMapping("/university/{id}")
    @SecurityRequirement(name = "BearerAuth")
    public ResponseEntity universityUpdateAdmissionStatus(@PathVariable Integer id ,@RequestBody @Valid UpdateAdmissionStatusRequest request){
        if (id == null){
            throw new BadRequestException("Id đề án không được để trống.", Map.of("error", "Id is null"));
        } else if (id <= 0)
            throw new BadRequestException("Id đề án không hợp lệ.", Map.of("error", id.toString()));
        try {
            admissionService.universityAndConsultantUpdateAdmissionStatus(id, request);
            return ResponseEntity.ok(ResponseData.ok("Cập nhập trạng thái đề án thành công."));
        }catch (NotAllowedException | BadRequestException e){
            throw e;
        }
        catch (Exception e){
            throw new BadRequestException("Cập nhập trạng thái đề án thất bại.", Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/staff/{id}")
    @SecurityRequirement(name = "BearerAuth")
    public ResponseEntity staffUpdateAdmissionStatus(@PathVariable Integer id ,@RequestBody @Valid UpdateAdmissionConfirmStatusRequest request){
        if (id == null){
            throw new BadRequestException("Id đề án không được để trống.", Map.of("error", "Id is null"));
        } else if (id <= 0)
            throw new BadRequestException("Id đề án không hợp lệ.", Map.of("error", id.toString()));
        try {
            admissionService.staffUpdateAdmissionConfirmStatus(id, request);
            return ResponseEntity.ok(ResponseData.ok("Cập nhập trạng thái đề án thành công."));
        }catch (NotAllowedException | BadRequestException e){
            throw e;
        }
        catch (Exception e){
            throw new BadRequestException("Cập nhập trạng thái đề án thất bại.", Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/test/{id}")
    @SecurityRequirement(name = "BearerAuth")
    public ResponseEntity consultantUpdateAdmission(@PathVariable Integer id){
        if (id == null){
            throw new BadRequestException("Id đề án không được để trống.", Map.of("error", "Id is null"));
        } else if (id <= 0)
            throw new BadRequestException("Id đề án không hợp lệ.", Map.of("error", id.toString()));
        try {
            admissionService.consultantUpdateAdmission(id);
            return ResponseEntity.ok(ResponseData.ok("Cập nhập trạng thái đề án thành công."));
        }catch (NotAllowedException | BadRequestException e){
            throw e;
        }
        catch (Exception e){
            throw new BadRequestException("Cập nhập trạng thái đề án thất bại.", Map.of("error", e.getMessage()));
        }
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
            List<Integer> years = null;
            List<String> universityCodes = null;
            if (year != null &&  !year.isEmpty()) years = Arrays.stream(year.split(",")).map(Integer::parseInt).toList();
            if (universityCode != null && !universityCode.isEmpty()) universityCodes = Arrays.stream(universityCode.split(",")).toList();;
            return ResponseEntity.ok(ResponseData.ok("Lấy điểm xét tuyển thành công.", admissionService.getAdmissionScoreResponse(pageable , years, universityCodes)));
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


    @GetMapping("/schoolDirectory")
    public ResponseEntity schoolDirectory(
            @RequestParam(required = false) String subjectGroupId,
            @RequestParam(required = false) String methodId,
            @RequestParam(required = false) String universityCode,
            @RequestParam(required = false) String provinceId
    ){
        try {
            List<Integer> subjectGroupIds = null;
            List<Integer> methodIds = null;
            List<String> universityCodes = null;
            List<Integer> provinceIds = null;

            if (subjectGroupId != null && !subjectGroupId.isEmpty()) {
                subjectGroupIds = Arrays.stream(subjectGroupId.split(",")).map(Integer::parseInt).toList();
            }
            if (methodId != null && !methodId.isEmpty()) {
                methodIds = Arrays.stream(methodId.split(",")).map(Integer::parseInt).toList();
            }
            if (universityCode != null && !universityCode.isEmpty()) {
                universityCodes = Arrays.stream(universityCode.split(",")).toList();
            }
            if (provinceId  != null && !provinceId.isEmpty()) {
                provinceIds = Arrays.stream(provinceId.split(",")).map(Integer::parseInt).toList();
            }
            return ResponseEntity.ok(admissionService.schoolDirectory(new SchoolDirectoryRequest(subjectGroupIds, methodIds, universityCodes, provinceIds)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
