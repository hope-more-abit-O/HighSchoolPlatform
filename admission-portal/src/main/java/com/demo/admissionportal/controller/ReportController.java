package com.demo.admissionportal.controller;

import com.demo.admissionportal.constants.ReportStatus;
import com.demo.admissionportal.constants.ReportType;
import com.demo.admissionportal.constants.ResponseCode;
import com.demo.admissionportal.dto.response.report.post_report.ReportPostResponse;
import com.demo.admissionportal.dto.request.report.post_report.CreatePostReportRequest;
import com.demo.admissionportal.dto.request.report.post_report.UpdatePostReportRequest;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.dto.response.report.post_report.ListAllPostReportResponse;
import com.demo.admissionportal.dto.response.report.post_report.UpdatePostReportResponseDTO;
import com.demo.admissionportal.entity.sub_entity.PostReport;
import com.demo.admissionportal.service.ReportService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/reports")
@AllArgsConstructor
public class ReportController {
    private final ReportService postReportService;
    private final ReportService reportService;

    @PostMapping
    @PreAuthorize("hasAuthority('USER')")
    @SecurityRequirement(name = "BearerAuth")
    public ResponseEntity<ResponseData<PostReport>> createPostReport(@RequestBody @Valid CreatePostReportRequest request, Authentication authentication) {
        ResponseData<PostReport> createdPostReport = postReportService.createPostReport(request, authentication);
        if (createdPostReport.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.ok(createdPostReport);
        } else if (createdPostReport.getStatus() == ResponseCode.C204.getCode()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(createdPostReport);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(createdPostReport);
    }

    @GetMapping("/{reportId}")
    @PreAuthorize("hasAuthority('STAFF')")
    @SecurityRequirement(name = "BearerAuth")
    public ResponseEntity<ResponseData<ReportPostResponse>> getPostReportById(@PathVariable Integer reportId, Authentication authentication) {
        ResponseData<ReportPostResponse> postReportResponse = reportService.getPostReportById(reportId, authentication);
        if (postReportResponse.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.ok(postReportResponse);
        } else if (postReportResponse.getStatus() == ResponseCode.C203.getCode()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(postReportResponse);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(postReportResponse);
    }

    @PutMapping("/{reportId}")
    @PreAuthorize("hasAuthority('STAFF')")
    @SecurityRequirement(name = "BearerAuth")
    public ResponseEntity<ResponseData<UpdatePostReportResponseDTO>> updatePostReport(
            @PathVariable Integer reportId,
            @RequestBody @Valid UpdatePostReportRequest request,
            Authentication authentication) {
        ResponseData<UpdatePostReportResponseDTO> postReportResponse = reportService.updatePostReport(reportId, request, authentication);
        if (postReportResponse.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.ok(postReportResponse);
        } else if (postReportResponse.getStatus() == ResponseCode.C203.getCode()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(postReportResponse);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(postReportResponse);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('STAFF')")
    @SecurityRequirement(name = "BearerAuth")
    public ResponseEntity<ResponseData<Page<ListAllPostReportResponse>>> findAllPostReports(
            Pageable pageable,
            Authentication authentication,
            @RequestParam(required = false) Integer reportId,
            @RequestParam(required = false) String ticketId,
            @RequestParam(required = false) Integer createBy,
            @RequestParam(required = false) String content,
            @RequestParam(required = false) ReportType reportType,
            @RequestParam(required = false) ReportStatus status) {
        ResponseData<Page<ListAllPostReportResponse>> postReportsResponse = reportService.findAllPostReports(pageable, authentication, reportId, ticketId, createBy, content, reportType, status);
        if (postReportsResponse.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.ok(postReportsResponse);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(postReportsResponse);
    }
}
