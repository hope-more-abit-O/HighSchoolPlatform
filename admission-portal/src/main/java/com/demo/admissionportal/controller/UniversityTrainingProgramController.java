package com.demo.admissionportal.controller;

import com.demo.admissionportal.constants.Role;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.dto.response.university_training_program.GetFullUniversityTrainingProgramResponse;
import com.demo.admissionportal.dto.response.university_training_program.GetInfoUniversityTrainingProgramResponse;
import com.demo.admissionportal.entity.User;
import com.demo.admissionportal.exception.exceptions.BadRequestException;
import com.demo.admissionportal.exception.exceptions.NotAllowedException;
import com.demo.admissionportal.service.UniversityTrainingProgramService;
import com.demo.admissionportal.util.impl.ServiceUtils;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/university-training-program")
@RequiredArgsConstructor
public class UniversityTrainingProgramController {
    private final UniversityTrainingProgramService universityTrainingProgramService;


    @GetMapping("/full/{university-id}")
    @SecurityRequirement(name = "BearerAuth")
    public ResponseEntity<ResponseData<GetFullUniversityTrainingProgramResponse>> universityGetUniversityTrainingPrograms(@PathVariable("university-id") Integer universityId) {
        return ResponseEntity.ok(ResponseData.ok("Lấy thông tin chương trình đào tạo thành công", universityTrainingProgramService.getFullUniversityTrainingPrograms(universityId)));
    }

    @GetMapping("/info/{university-id}")
    public ResponseEntity<ResponseData<GetInfoUniversityTrainingProgramResponse>> universityGetInfoUniversityTrainingPrograms(@PathVariable("university-id") Integer universityId) {
        return ResponseEntity.ok(ResponseData.ok("Lấy thông tin chương trình đào tạo thành công", universityTrainingProgramService.getInfoUniversityTrainingPrograms(universityId)));
    }
}
