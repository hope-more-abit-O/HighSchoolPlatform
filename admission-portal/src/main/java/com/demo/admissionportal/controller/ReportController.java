package com.demo.admissionportal.controller;

import com.demo.admissionportal.constants.ReportStatus;
import com.demo.admissionportal.constants.ReportType;
import com.demo.admissionportal.constants.ResponseCode;
import com.demo.admissionportal.dto.request.report.comment_report.CreateCommentReportRequest;
import com.demo.admissionportal.dto.response.report.comment_report.CommentReportResponse;
import com.demo.admissionportal.dto.response.report.comment_report.ListAllCommentReportResponse;
import com.demo.admissionportal.dto.response.report.post_report.*;
import com.demo.admissionportal.dto.request.report.post_report.CreatePostReportRequest;
import com.demo.admissionportal.dto.request.report.post_report.UpdatePostReportRequest;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.entity.sub_entity.CommentReport;
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

/**
 * The type Report controller.
 */
@RestController
@RequestMapping("/api/v1/reports")
@AllArgsConstructor
@SecurityRequirement(name = "BearerAuth")
public class ReportController {
    private final ReportService postReportService;
    private final ReportService reportService;

    /**
     * Create post report response entity.
     *
     * @param request        the request
     * @param authentication the authentication
     * @return the response entity
     */
    @PostMapping("/post")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<ResponseData<PostReport>> createPostReport(@RequestBody @Valid CreatePostReportRequest request, Authentication authentication) {
        ResponseData<PostReport> createdPostReport = postReportService.createPostReport(request, authentication);
        if (createdPostReport.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.ok(createdPostReport);
        } else if (createdPostReport.getStatus() == ResponseCode.C204.getCode()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(createdPostReport);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(createdPostReport);
    }

    /**
     * Gets post report by id.
     *
     * @param reportId       the report id
     * @param authentication the authentication
     * @return the post report by id
     */
    @GetMapping("/post/{reportId}")
    @PreAuthorize("hasAuthority('STAFF')")
    public ResponseEntity<ResponseData<ReportPostResponse>> getPostReportById(@PathVariable Integer reportId, Authentication authentication) {
        ResponseData<ReportPostResponse> postReportResponse = reportService.getPostReportById(reportId, authentication);
        if (postReportResponse.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.ok(postReportResponse);
        } else if (postReportResponse.getStatus() == ResponseCode.C203.getCode()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(postReportResponse);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(postReportResponse);
    }

    /**
     * Update post report response entity.
     *
     * @param reportId       the report id
     * @param request        the request
     * @param authentication the authentication
     * @return the response entity
     */
    @PutMapping("/post/{reportId}")
    @PreAuthorize("hasAuthority('STAFF')")
    public ResponseEntity<ResponseData<UpdatePostReportResponse>> updatePostReport(
            @PathVariable Integer reportId,
            @RequestBody @Valid UpdatePostReportRequest request,
            Authentication authentication) {
        ResponseData<UpdatePostReportResponse> postReportResponse = reportService.updatePostReport(reportId, request, authentication);
        if (postReportResponse.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.ok(postReportResponse);
        } else if (postReportResponse.getStatus() == ResponseCode.C203.getCode()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(postReportResponse);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(postReportResponse);
    }

    /**
     * Find all post reports response entity.
     *
     * @param pageable       the pageable
     * @param authentication the authentication
     * @param reportId       the report id
     * @param ticketId       the ticket id
     * @param createBy       the create by
     * @param content        the content
     * @param reportType     the report type
     * @param status         the status
     * @return the response entity
     */
    @GetMapping("/posts")
    @PreAuthorize("hasAuthority('STAFF')")
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
        } else if (postReportsResponse.getStatus() == ResponseCode.C203.getCode()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(postReportsResponse);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(postReportsResponse);
    }

    /**
     * Find all complete post reports response entity.
     *
     * @param pageable       the pageable
     * @param authentication the authentication
     * @param reportId       the report id
     * @param ticketId       the ticket id
     * @param createBy       the create by
     * @param reportType     the report type
     * @return the response entity
     */
    @GetMapping("/completed")
    @PreAuthorize("hasAuthority('STAFF')")
    public ResponseEntity<ResponseData<Page<FindAllReportsCompletedResponse>>> findAllCompletePostReports(
            Pageable pageable,
            Authentication authentication,
            @RequestParam(required = false) Integer reportId,
            @RequestParam(required = false) String ticketId,
            @RequestParam(required = false) Integer createBy,
            @RequestParam(required = false) ReportType reportType
    ) {
        ResponseData<Page<FindAllReportsCompletedResponse>> postReportsResponse = reportService.findAllCompletedPostReports(pageable, authentication, reportId, ticketId, createBy, reportType);
        if (postReportsResponse.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.ok(postReportsResponse);
        } else if (postReportsResponse.getStatus() == ResponseCode.C203.getCode()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(postReportsResponse);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(postReportsResponse);
    }

    /**
     * Create comment report response data.
     *
     * @param request        the request
     * @param authentication the authentication
     * @return the response data
     */
    @PostMapping("/comment/create")
    public ResponseEntity<ResponseData<CommentReport>> createCommentReport(@RequestBody @Valid CreateCommentReportRequest request, Authentication authentication) {
        ResponseData<CommentReport> createCommentReport = reportService.createCommentReport(request, authentication);
        if (createCommentReport.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.ok(createCommentReport);
        } else if (createCommentReport.getStatus() == ResponseCode.C204.getCode()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(createCommentReport);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(createCommentReport);
    }

    /**
     * Gets comment report by id.
     *
     * @param reportId       the report id
     * @param authentication the authentication
     * @return the comment report by id
     */
    @GetMapping("/comment/{reportId}")
    public ResponseEntity<ResponseData<CommentReportResponse>> getCommentReportById(@PathVariable Integer reportId, Authentication authentication) {
        ResponseData<CommentReportResponse> commentReportsResponse = reportService.getCommentReportById(reportId, authentication);
        if (commentReportsResponse.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.ok(commentReportsResponse);
        } else if (commentReportsResponse.getStatus() == ResponseCode.C203.getCode()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(commentReportsResponse);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(commentReportsResponse);
    }

    /**
     * Find all comment reports response entity.
     *
     * @param pageable       the pageable
     * @param authentication the authentication
     * @param reportId       the report id
     * @param ticketId       the ticket id
     * @param createBy       the create by
     * @param content        the content
     * @param reportType     the report type
     * @param status         the status
     * @return the response entity
     */
    @GetMapping("/comments")
    @PreAuthorize("hasAuthority('STAFF')")
    public ResponseEntity<ResponseData<Page<ListAllCommentReportResponse>>> findAllCommentReports(Pageable pageable,
                                                                                                  Authentication authentication,
                                                                                                  @RequestParam(required = false) Integer reportId,
                                                                                                  @RequestParam(required = false) String ticketId,
                                                                                                  @RequestParam(required = false) Integer createBy,
                                                                                                  @RequestParam(required = false) String content,
                                                                                                  @RequestParam(required = false) ReportType reportType,
                                                                                                  @RequestParam(required = false) ReportStatus status) {
        ResponseData<Page<ListAllCommentReportResponse>> commentReportsResponse = reportService.findAllCommentReports(pageable, authentication, reportId, ticketId, createBy, content, reportType, status);
        if (commentReportsResponse.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.ok(commentReportsResponse);
        } else if (commentReportsResponse.getStatus() == ResponseCode.C203.getCode()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(commentReportsResponse);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(commentReportsResponse);
    }
}
