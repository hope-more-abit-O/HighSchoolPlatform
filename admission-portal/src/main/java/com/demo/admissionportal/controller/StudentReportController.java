package com.demo.admissionportal.controller;

import com.demo.admissionportal.constants.ResponseCode;
import com.demo.admissionportal.dto.request.student_report.CreateStudentReportRequest;
import com.demo.admissionportal.dto.request.student_report.UpdateStudentReportRequest;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.dto.response.student_report.CreateStudentReportResponseDTO;
import com.demo.admissionportal.dto.response.student_report.ListStudentReportResponse;
import com.demo.admissionportal.dto.response.student_report.StudentReportResponseDTO;
import com.demo.admissionportal.dto.response.student_report.UpdateStudentReportResponseDTO;
import com.demo.admissionportal.service.StudentReportService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * The type Student report controller.
 */
@RestController
@RequestMapping("/api/v1/student-report")
@AllArgsConstructor
public class StudentReportController {
    private final StudentReportService studentReportService;

    /**
     * Create student report response entity.
     *
     * @param createStudentReportRequest the create student report request
     * @param authentication             the authentication
     * @return the response entity
     */
    @PostMapping
    @PreAuthorize("hasAuthority('USER')")
    @SecurityRequirement(name = "BearerAuth")
    public ResponseEntity<ResponseData<CreateStudentReportResponseDTO>> createStudentReport(@RequestBody @Valid CreateStudentReportRequest createStudentReportRequest, Authentication authentication) {
        ResponseData<CreateStudentReportResponseDTO> createdStudentReport = studentReportService.createStudentReport(createStudentReportRequest, authentication);
        if (createdStudentReport.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.ok(createdStudentReport);
        } else if (createdStudentReport.getStatus() == ResponseCode.C204.getCode()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(createdStudentReport);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(createdStudentReport);
    }

    /**
     * Find student report by id response entity.
     *
     * @param studentReportId the student report id
     * @param authentication  the authentication
     * @return the response entity
     */
    @GetMapping("/{studentReportId}")
    @PreAuthorize("hasAuthority('USER')")
    @SecurityRequirement(name = "BearerAuth")
    public ResponseEntity<ResponseData<StudentReportResponseDTO>> findStudentReportById(@PathVariable Integer studentReportId, Authentication authentication) {
        ResponseData<StudentReportResponseDTO> response = studentReportService.findStudentReportById(studentReportId, authentication);
        if (response.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.ok(response);
        } else if (response.getStatus() == ResponseCode.C204.getCode()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    /**
     * Find student reports by user response entity.
     *
     * @param name           the name
     * @param pageable       the pageable
     * @param authentication the authentication
     * @return the response entity
     */
    @GetMapping
    @PreAuthorize("hasAuthority('USER')")
    @SecurityRequirement(name = "BearerAuth")
    public ResponseEntity<?> findAllStudentReports(
            @RequestParam(required = false) String name,
            @PageableDefault(size = 10) Pageable pageable,
            Authentication authentication) {
        ResponseData<?> response = studentReportService.findAllStudentReports(name, pageable, authentication);
        if (response.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.ok(response);
        } else if (response.getStatus() == ResponseCode.C204.getCode()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
    /**
     * Update student report response entity.
     *
     * @param studentReportId the student report id
     * @param request         the request
     * @param authentication  the authentication
     * @return the response entity
     */
    @PutMapping("/update/{studentReportId}")
    @PreAuthorize("hasAuthority('USER')")
    @SecurityRequirement(name = "BearerAuth")
    public ResponseEntity<ResponseData<UpdateStudentReportResponseDTO>> updateStudentReport(@PathVariable Integer studentReportId, @RequestBody UpdateStudentReportRequest request, Authentication authentication) {
        ResponseData<UpdateStudentReportResponseDTO> response = studentReportService.updateStudentReportById(studentReportId, request, authentication);
        if (response.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.ok(response);
        } else if (response.getStatus() == ResponseCode.C204.getCode()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
    @DeleteMapping("/{studentReportId}")
    @PreAuthorize("hasAuthority('USER')")
    @SecurityRequirement(name = "BearerAuth")
    public ResponseEntity<?> deleteStudentReport(@PathVariable Integer studentReportId, Authentication authentication) {
        ResponseData<Void> response = studentReportService.deleteStudentReportById(studentReportId, authentication);
        if (response.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.ok(response);
        } else if (response.getStatus() == ResponseCode.C204.getCode()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
