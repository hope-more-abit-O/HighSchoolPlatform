package com.demo.admissionportal.controller;

import com.demo.admissionportal.constants.ResponseCode;
import com.demo.admissionportal.dto.AdviceResult;
import com.demo.admissionportal.dto.Aspiration;
import com.demo.admissionportal.dto.ExamYearData;
import com.demo.admissionportal.dto.YearlyExamScoreResponse;
import com.demo.admissionportal.dto.request.AdmissionAnalysisRequest;
import com.demo.admissionportal.dto.request.AspirationsRequest;
import com.demo.admissionportal.dto.request.CreateHighschoolExamScoreRequest;
import com.demo.admissionportal.dto.request.UpdateHighschoolExamScoreRequest;
import com.demo.admissionportal.dto.response.*;
import com.demo.admissionportal.entity.UserIdentificationNumberRegister;
import com.demo.admissionportal.service.HighschoolExamScoreService;
import com.demo.admissionportal.service.impl.HighschoolExamScoreServiceImpl;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/exam-scores")
public class HighschoolExamScoreController {

    @Autowired
    private HighschoolExamScoreService highschoolExamScoreService;
    @Autowired
    private HighschoolExamScoreServiceImpl highschoolExamScoreServiceImpl;

    @GetMapping
    public ResponseEntity<ResponseData<List<HighschoolExamScoreResponse>>> getAllExamScores(
            @RequestParam(required = true) String identificationNumber,
            @RequestParam(required = true) Integer year
    ) {
        ResponseData<List<HighschoolExamScoreResponse>> response = highschoolExamScoreService.findAllWithFilter(identificationNumber, year);
        if (response.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.ok(response);
        } else if (response.getStatus() == ResponseCode.C204.getCode()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        } else if (response.getStatus() == ResponseCode.C203.getCode()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
    @GetMapping("/top-100")
    public ResponseEntity<ResponseData<List<HighschoolExamScoreResponse>>> getAllExamScores(
            @RequestParam(required = false) String subjectName,
            @RequestParam(required = false) String local
    ) {
        ResponseData<List<HighschoolExamScoreResponse>> response = highschoolExamScoreService.getAllTop100HighestScoreBySubject(subjectName, local);
        if (response.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.ok(response);
        } else if (response.getStatus() == ResponseCode.C204.getCode()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        } else if (response.getStatus() == ResponseCode.C203.getCode()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @GetMapping("/list-of-exam-score-data")
    @SecurityRequirement(name = "BearerAuth")
    @PreAuthorize("hasAuthority('STAFF')")
    public ResponseEntity<ResponseData<Page<ListExamScoreByYearResponse>>> listOfExamScoreData(Pageable pageable){
        ResponseData<Page<ListExamScoreByYearResponse>> response = highschoolExamScoreService.getAllListExamScoresByYear(pageable);
        if (response.getStatus() == ResponseCode.C200.getCode()){
            return ResponseEntity.ok(response);
        } else if (response.getStatus() == ResponseCode.C203.getCode()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @PostMapping
    @SecurityRequirement(name = "BearerAuth")
    @PreAuthorize("hasAuthority('STAFF')")
    public ResponseEntity<ResponseData<List<YearlyExamScoreResponse>>> createExamScores(@RequestBody List<ExamYearData> request) {
        ResponseData<List<YearlyExamScoreResponse>> response = highschoolExamScoreService.createExamScores(request);
        if (response.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @PutMapping("/{listExamScoreByYearId}")
    @SecurityRequirement(name = "BearerAuth")
    @PreAuthorize("hasAuthority('STAFF')")
    public ResponseEntity<ResponseData<List<YearlyExamScoreResponse>>> updateExamScores(
            @PathVariable Integer listExamScoreByYearId,
            @RequestBody List<ExamYearData>requests) {
        ResponseData<List<YearlyExamScoreResponse>> response = highschoolExamScoreService.updateExamScores(listExamScoreByYearId, requests);
        if (response.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    //Subject local Distribution
    @GetMapping("/distribution-by-local")
    public ResponseEntity<ResponseData<Map<String, Map<String, Float>>>> getScoreDistributionByLocal(
            @RequestParam(required = false) String subjectName) {
        ResponseData<Map<String, Map<String, Float>>> response = highschoolExamScoreService.getScoreDistributionByLocal(subjectName);
        if (response.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.ok(response);
        } else if (response.getStatus() == ResponseCode.C204.getCode()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        } else if (response.getStatus() == ResponseCode.C203.getCode()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    //Subject Distribution
    @GetMapping("/distribution-by-subject")
    public ResponseEntity<ResponseData<Map<String, Map<Float, Integer>>>> getScoreDistributionBySubject(
            @RequestParam(required = false) String local,
            @RequestParam(required = false) String subjectName) {
        ResponseData<Map<String, Map<Float, Integer>>> response = highschoolExamScoreService.getScoreDistributionBySubject(local, subjectName);
        if (response.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.ok(response);
        } else if (response.getStatus() == ResponseCode.C204.getCode()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        } else if (response.getStatus() == ResponseCode.C203.getCode()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
    //Subject Group distribution
    @GetMapping("/distribution")
    public ResponseEntity<ResponseData<?>> getScoreDistributionBySubjectGroup(
            @RequestParam(required = false) String local,
            @RequestParam(required = false) String subjectGroup,
            @RequestParam(required = false) String identificationNumber) {

        if (identificationNumber != null) {
            ResponseData<String> response = highschoolExamScoreServiceImpl.getRankingBySubjectGroupAndLocal(identificationNumber, subjectGroup, local);
            if (response.getStatus() == ResponseCode.C200.getCode()) {
                return ResponseEntity.ok(response);
            } else if (response.getStatus() == ResponseCode.C204.getCode()) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
            } else if (response.getStatus() == ResponseCode.C203.getCode()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        } else {
            ResponseData<Map<String, Map<Float, Integer>>> response = highschoolExamScoreService.getScoreDistributionBySubjectGroup(local, subjectGroup);
            if (response.getStatus() == ResponseCode.C200.getCode()) {
                return ResponseEntity.ok(response);
            } else if (response.getStatus() == ResponseCode.C204.getCode()) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
            } else if (response.getStatus() == ResponseCode.C203.getCode()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    @PostMapping("/publish/{listId}")
    @SecurityRequirement(name = "BearerAuth")
    @PreAuthorize("hasAuthority('STAFF')")
    public ResponseEntity<ResponseData<String>> publishExamScores(@PathVariable Integer listId) {
        ResponseData<String> response = highschoolExamScoreService.publishExamScores(listId);
        if (response.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.ok(response);
        } else if (response.getStatus() == ResponseCode.C204.getCode()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        } else if (response.getStatus() == ResponseCode.C203.getCode()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
    @GetMapping("/list-exam-score/{listId}")
    @SecurityRequirement(name = "BearerAuth")
    @PreAuthorize("hasAuthority('STAFF')")
    public ResponseEntity<ResponseData<ListExamScoreByYearResponseV2>> getExamScoresListById(
            @PathVariable Integer listId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        ResponseData<ListExamScoreByYearResponseV2> response = highschoolExamScoreService.getListExamScoreById(listId, page, size);
        if (response.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.ok(response);
        } else if (response.getStatus() == ResponseCode.C204.getCode()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        } else if (response.getStatus() == ResponseCode.C203.getCode()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @GetMapping("/list-all-registered-identification-number")
    @SecurityRequirement(name = "BearerAuth")
    @PreAuthorize("hasAuthority('STAFF')")
    public ResponseEntity<ResponseData<List<UserIdentificationResponseDTO>>> getAllRegisteredIdentificationNumbers(
            @RequestParam(required = false) Integer userId,
            @RequestParam(required = false) String identificationNumber
    ) {
        ResponseData<List<UserIdentificationResponseDTO>> response = highschoolExamScoreService.getAllRegisteredIdentificationNumbers(userId, identificationNumber);
        if (response.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.ok(response);
        } else if (response.getStatus() == ResponseCode.C204.getCode()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        } else if (response.getStatus() == ResponseCode.C203.getCode()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }


    @PostMapping("/forecast")
    public ResponseEntity<ResponseData<?>> forecastScore2024(@RequestBody Aspiration request) {
        List<Object> successList = new ArrayList<>();
        List<Object> errorList = new ArrayList<>();
        for (AdmissionAnalysisRequest requests : request.getAspirations()) {
            try {
                ResponseData<?> responseData = highschoolExamScoreServiceImpl.forecastScore2024(requests);

                if (responseData.getStatus() == ResponseCode.C200.getCode()) {
                    successList.add(responseData.getData());
                } else {
                    Map<String, Object> errorInfo = new HashMap<>();
                    errorInfo.put("errorMessage", responseData.getMessage());
                    errorList.add(errorInfo);
                }
            } catch (Exception e) {
                Map<String, Object> errorInfo = new HashMap<>();

                errorInfo.put("errorMessage", e.getMessage());
                errorList.add(errorInfo);
            }
        }
        if (!successList.isEmpty() && errorList.isEmpty()) {
            Map<String, Object> result = new HashMap<>();
            result.put("success", successList);
            result.put("errors", errorList);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseData<>(ResponseCode.C200.getCode(), "Tất cả nguyện vọng được dự đoán thành công.", result));
        } else if (!successList.isEmpty() && !errorList.isEmpty()) {
            Map<String, Object> result = new HashMap<>();
            result.put("success", successList);
            result.put("errors", errorList);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseData<>(ResponseCode.C200.getCode(), "Các nguyện vọng đã được dự đoán nhưng có một số nguyện vọng không thể dự đoán được", result));
        } else {
            Map<String, Object> result = new HashMap<>();
            result.put("success", successList);
            result.put("errors", errorList);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseData<>(ResponseCode.C210.getCode(), "Tất cả nguyện vọng dự đoán thất bại", result));
        }
    }

    @GetMapping("/universities")
    public ResponseEntity<ResponseData<?>> getDistinctUniversityIds() {
        ResponseData<?> universityIds = highschoolExamScoreServiceImpl.getDistinctUniversityIds();
        if (universityIds.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.ok(universityIds);
        } else if (universityIds.getStatus() == ResponseCode.C204.getCode()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(universityIds);
        } else if (universityIds.getStatus() == ResponseCode.C203.getCode()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(universityIds);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(universityIds);
        }
    }

    @GetMapping("/major/{universityId}")
    public ResponseEntity<ResponseData<?>> getMajorfromUniversityId(
            @PathVariable (required = true) Integer universityId
    ){
        ResponseData<?> majorIds = highschoolExamScoreServiceImpl.getDistinctMajorByUniversityId(universityId);
        if (majorIds.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.ok(majorIds);
        } else if (majorIds.getStatus() == ResponseCode.C204.getCode()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(majorIds);
        } else if (majorIds.getStatus() == ResponseCode.C203.getCode()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(majorIds);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(majorIds);
        }
    }

    @GetMapping("/{universityId}/{majorId}")
    public ResponseEntity<ResponseData<?>> getSubjectGroupByUniversityIdAndMajorId(
            @PathVariable (required = true) Integer universityId,
            @PathVariable (required = true) Integer majorId
    ) {
        ResponseData<?> response = highschoolExamScoreServiceImpl.getSubjectGroupByUniversityIdAndMajorId(universityId, majorId);
        if (response.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.ok(response);
        } else if (response.getStatus() == ResponseCode.C204.getCode()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        } else if (response.getStatus() == ResponseCode.C203.getCode()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}