package com.demo.admissionportal.controller;

import com.demo.admissionportal.constants.ReportStatus;
import com.demo.admissionportal.constants.ReportType;
import com.demo.admissionportal.constants.ResponseCode;
import com.demo.admissionportal.dto.request.report.comment_report.CreateCommentReportRequest;
import com.demo.admissionportal.dto.request.report.comment_report.UpdateCommentReportRequest;
import com.demo.admissionportal.dto.request.report.function_report.CreateFunctionReportRequest;
import com.demo.admissionportal.dto.request.report.function_report.UpdateFunctionReportRequest;
import com.demo.admissionportal.dto.response.report.FindAllReportsCompletedResponse;
import com.demo.admissionportal.dto.response.report.FindAllReportsResponse;
import com.demo.admissionportal.dto.response.report.comment_report.CommentReportResponse;
import com.demo.admissionportal.dto.response.report.comment_report.ListAllCommentReportResponse;
import com.demo.admissionportal.dto.response.report.comment_report.UpdateCommentReportResponse;
import com.demo.admissionportal.dto.response.report.function_report.FindAllFunctionReportResponse;
import com.demo.admissionportal.dto.response.report.function_report.FunctionReportResponse;
import com.demo.admissionportal.dto.response.report.function_report.UpdateFunctionReportResponse;
import com.demo.admissionportal.dto.response.report.post_report.*;
import com.demo.admissionportal.dto.request.report.post_report.CreatePostReportRequest;
import com.demo.admissionportal.dto.request.report.post_report.UpdatePostReportRequest;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.entity.sub_entity.CommentReport;
import com.demo.admissionportal.entity.sub_entity.FunctionReport;
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
import org.springframework.web.multipart.MultipartFile;

/**
 * <h1>Report Controller</h1>
 * <p>
 * This controller handles HTTP requests related to report operations within the Admission Portal.
 * It provides endpoints for creating, updating, retrieving, and listing various types of reports.
 * </p>
 *
 * @since 1.0
 */
@RestController
@RequestMapping("/api/v1/reports")
@AllArgsConstructor
@SecurityRequirement(name = "BearerAuth")
public class ReportController {

    private final ReportService postReportService;
    private final ReportService reportService;

    /**
     * <h2>Create Post Report</h2>
     * <p>
     * Creates a new post report based on the provided request data. The request must contain valid report
     * details including the post ID and content. The authenticated user information is used to track who created
     * the report.
     * </p>
     *
     * @param request        The {@link CreatePostReportRequest} containing the details of the new post report.
     * @param authentication The {@link Authentication} object representing the authenticated user.
     * @return A {@link ResponseData} object containing the result of the creation operation along with the created {@link PostReport}.
     * @since 1.0
     */
    @PostMapping("/post/create")
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
     * <h2>Get Post Report By ID</h2>
     * <p>
     * Retrieves the details of a specific post report identified by the provided ID. The response includes all the
     * report details along with the information about who created the report. The authenticated user information is
     * used to verify permissions.
     * </p>
     *
     * @param reportId       The ID of the post report to be retrieved.
     * @param authentication The {@link Authentication} object representing the authenticated user.
     * @return A {@link ResponseData} object containing the details of the post report along with {@link ReportPostResponse}.
     * @since 1.0
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
     * <h2>Update Post Report</h2>
     * <p>
     * Updates the details of an existing post report identified by the provided ID. The request contains the updated
     * report details. The authenticated user information is used to track who performed the update.
     * </p>
     *
     * @param reportId       The ID of the post report to be updated.
     * @param request        The {@link UpdatePostReportRequest} containing the updated details of the post report.
     * @param authentication The {@link Authentication} object representing the authenticated user.
     * @return A {@link ResponseData} object containing the result of the update operation along with the updated {@link UpdatePostReportResponse}.
     * @since 1.0
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
     * <h2>Find All Post Reports</h2>
     * <p>
     * Retrieves a paginated list of all post reports, optionally filtered by report ID, ticket ID, creator, content, and status.
     * The response includes summary details of each post report. The authenticated user information is used to verify permissions.
     * </p>
     *
     * @param pageable       Pagination details.
     * @param authentication The {@link Authentication} object representing the authenticated user.
     * @param reportId       Optional filter for the report ID.
     * @param ticketId       Optional filter for the ticket ID.
     * @param createBy       Optional filter for the creator ID.
     * @param content        Optional filter for the content.
     * @param status         Optional filter for the report status.
     * @return A {@link ResponseData} object containing a paginated list of post reports.
     * @since 1.0
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
            @RequestParam(required = false) ReportStatus status) {
        ResponseData<Page<ListAllPostReportResponse>> postReportsResponse = reportService.findAllPostReports(pageable, authentication, reportId, ticketId, createBy, content, status);
        if (postReportsResponse.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.ok(postReportsResponse);
        } else if (postReportsResponse.getStatus() == ResponseCode.C203.getCode()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(postReportsResponse);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(postReportsResponse);
    }

    /**
     * Find all reports response entity.
     *
     * @param pageable       the pageable
     * @param authentication the authentication
     * @param reportId       the report id
     * @param ticketId       the ticket id
     * @param createBy       the create by
     * @param content        the content
     * @param status         the status
     * @return the response entity
     */
    @GetMapping
    @PreAuthorize("hasAuthority('STAFF')")
    public ResponseEntity<ResponseData<Page<FindAllReportsResponse>>> findAllReports(Pageable pageable, Authentication authentication,
                                                                                     @RequestParam(required = false) Integer reportId,
                                                                                     @RequestParam(required = false) String ticketId,
                                                                                     @RequestParam(required = false) Integer createBy,
                                                                                     @RequestParam(required = false) String content,
                                                                                     @RequestParam(required = false) ReportStatus status) {
        ResponseData<Page<FindAllReportsResponse>> reportsResponse = reportService.findAllReports(pageable, authentication, reportId, ticketId, createBy, content, status);
        if (reportsResponse.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.ok(reportsResponse);
        } else if (reportsResponse.getStatus() == ResponseCode.C203.getCode()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(reportsResponse);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(reportsResponse);
    }


    /**
     * <h2>Find All Completed Post Reports</h2>
     * <p>
     * Retrieves a paginated list of all completed post reports, optionally filtered by report ID, ticket ID, creator, and report type.
     * The response includes summary details of each completed post report. The authenticated user information is used to verify permissions.
     * </p>
     *
     * @param pageable       Pagination details.
     * @param authentication The {@link Authentication} object representing the authenticated user.
     * @param reportId       Optional filter for the report ID.
     * @param ticketId       Optional filter for the ticket ID.
     * @param createBy       Optional filter for the creator ID.
     * @param reportType     Optional filter for the report type.
     * @return A {@link ResponseData} object containing a paginated list of completed post reports.
     * @since 1.0
     */
    @GetMapping("/completed")
    @PreAuthorize("hasAuthority('STAFF')")
    public ResponseEntity<ResponseData<Page<FindAllReportsCompletedResponse>>> findAllCompleteReports(
            Pageable pageable,
            Authentication authentication,
            @RequestParam(required = false) Integer reportId,
            @RequestParam(required = false) String ticketId,
            @RequestParam(required = false) Integer createBy,
            @RequestParam(required = false) ReportType reportType) {
        ResponseData<Page<FindAllReportsCompletedResponse>> postReportsResponse = reportService.findAllCompletedReports(pageable, authentication, reportId, ticketId, createBy, reportType);
        if (postReportsResponse.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.ok(postReportsResponse);
        } else if (postReportsResponse.getStatus() == ResponseCode.C203.getCode()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(postReportsResponse);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(postReportsResponse);
    }

    /**
     * <h2>Create Comment Report</h2>
     * <p>
     * Creates a new comment report based on the provided request data. The request must contain valid report details
     * including the comment ID and content. The authenticated user information is used to track who created the report.
     * </p>
     *
     * @param request        The {@link CreateCommentReportRequest} containing the details of the new comment report.
     * @param authentication The {@link Authentication} object representing the authenticated user.
     * @return A {@link ResponseData} object containing the result of the creation operation along with the created {@link CommentReport}.
     * @since 1.0
     */
    @PostMapping("/comment/create")
    @PreAuthorize("hasAuthority('USER')")
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
     * <h2>Get Comment Report By ID</h2>
     * <p>
     * Retrieves the details of a specific comment report identified by the provided ID. The response includes all the
     * report details along with the information about who created the report. The authenticated user information is
     * used to verify permissions.
     * </p>
     *
     * @param reportId       The ID of the comment report to be retrieved.
     * @param authentication The {@link Authentication} object representing the authenticated user.
     * @return A {@link ResponseData} object containing the details of the comment report along with {@link CommentReportResponse}.
     * @since 1.0
     */
    @GetMapping("/comment/{reportId}")
    @PreAuthorize("hasAuthority('STAFF')")
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
     * <h2>Find All Comment Reports</h2>
     * <p>
     * Retrieves a paginated list of all comment reports, optionally filtered by report ID, ticket ID, creator, content, and status.
     * The response includes summary details of each comment report. The authenticated user information is used to verify permissions.
     * </p>
     *
     * @param pageable       Pagination details.
     * @param authentication The {@link Authentication} object representing the authenticated user.
     * @param reportId       Optional filter for the report ID.
     * @param ticketId       Optional filter for the ticket ID.
     * @param createBy       Optional filter for the creator ID.
     * @param content        Optional filter for the content.
     * @param status         Optional filter for the report status.
     * @return A {@link ResponseData} object containing a paginated list of comment reports.
     * @since 1.0
     */
    @GetMapping("/comments")
    @PreAuthorize("hasAuthority('STAFF')")
    public ResponseEntity<ResponseData<Page<ListAllCommentReportResponse>>> findAllCommentReports(Pageable pageable,
                                                                                                  Authentication authentication,
                                                                                                  @RequestParam(required = false) Integer reportId,
                                                                                                  @RequestParam(required = false) String ticketId,
                                                                                                  @RequestParam(required = false) Integer createBy,
                                                                                                  @RequestParam(required = false) String content,
                                                                                                  @RequestParam(required = false) ReportStatus status) {
        ResponseData<Page<ListAllCommentReportResponse>> commentReportsResponse = reportService.findAllCommentReports(pageable, authentication, reportId, ticketId, createBy, content, status);
        if (commentReportsResponse.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.ok(commentReportsResponse);
        } else if (commentReportsResponse.getStatus() == ResponseCode.C203.getCode()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(commentReportsResponse);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(commentReportsResponse);
    }

    /**
     * <h2>Update Comment Report</h2>
     * <p>
     * Updates the details of an existing comment report identified by the provided ID. The request contains the updated
     * report details. The authenticated user information is used to track who performed the update.
     * </p>
     *
     * @param reportId       The ID of the comment report to be updated.
     * @param request        The {@link UpdateCommentReportRequest} containing the updated details of the comment report.
     * @param authentication The {@link Authentication} object representing the authenticated user.
     * @return A {@link ResponseData} object containing the result of the update operation along with the updated {@link UpdateCommentReportResponse}.
     * @since 1.0
     */
    @PutMapping("/comment/{reportId}")
    @PreAuthorize("hasAuthority('STAFF')")
    public ResponseEntity<ResponseData<UpdateCommentReportResponse>> updateCommentReport(
            @PathVariable Integer reportId,
            @RequestBody @Valid UpdateCommentReportRequest request,
            Authentication authentication) {
        ResponseData<UpdateCommentReportResponse> commentReportResponse = reportService.updateCommentReport(reportId, request, authentication);
        if (commentReportResponse.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.ok(commentReportResponse);
        } else if (commentReportResponse.getStatus() == ResponseCode.C203.getCode()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(commentReportResponse);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(commentReportResponse);
    }

    /**
     * <h2>Create Function Report</h2>
     * <p>
     * Creates a new function report based on the provided request data. The request must contain valid report details
     * including the content and proofs. The authenticated user information is used to track who created the report.
     * </p>
     *
     * @param content        The content of the function report.
     * @param proofs         The proofs associated with the function report.
     * @param authentication The {@link Authentication} object representing the authenticated user.
     * @return A {@link ResponseData} object containing the result of the creation operation along with the created {@link FunctionReport}.
     * @since 1.0
     */
    @PostMapping("/function/create")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<ResponseData<FunctionReport>> createFunctionReport(
            @RequestParam("content") String content,
            @RequestParam("proofs") MultipartFile[] proofs,
            Authentication authentication) {

        CreateFunctionReportRequest request = new CreateFunctionReportRequest();
        request.setContent(content);
        request.setProofs(proofs);

        ResponseData<FunctionReport> response = reportService.createFunctionReport(request, authentication);

        if (response.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.ok(response);
        } else if (response.getStatus() == ResponseCode.C203.getCode()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    /**
     * <h2>Get Function Report By ID</h2>
     * <p>
     * Retrieves the details of a specific function report identified by the provided ID. The response includes all the
     * report details along with the information about who created the report. The authenticated user information is
     * used to verify permissions.
     * </p>
     *
     * @param reportId       The ID of the function report to be retrieved.
     * @param authentication The {@link Authentication} object representing the authenticated user.
     * @return A {@link ResponseData} object containing the details of the function report along with {@link FunctionReportResponse}.
     * @since 1.0
     */
    @GetMapping("/function/{reportId}")
    @PreAuthorize("hasAuthority('STAFF')")
    public ResponseEntity<ResponseData<FunctionReportResponse>> getFunctionReportById(@PathVariable Integer reportId, Authentication authentication) {
        ResponseData<FunctionReportResponse> functionReportResponse = reportService.getFunctionReportById(reportId, authentication);
        if (functionReportResponse.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.ok(functionReportResponse);
        } else if (functionReportResponse.getStatus() == ResponseCode.C203.getCode()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(functionReportResponse);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(functionReportResponse);
    }

    /**
     * <h2>Update Function Report</h2>
     * <p>
     * Updates the details of an existing function report identified by the provided ID. The request contains the updated
     * report details. The authenticated user information is used to track who performed the update.
     * </p>
     *
     * @param reportId       The ID of the function report to be updated.
     * @param request        The {@link UpdateFunctionReportRequest} containing the updated details of the function report.
     * @param authentication The {@link Authentication} object representing the authenticated user.
     * @return A {@link ResponseData} object containing the result of the update operation along with the updated {@link UpdateFunctionReportResponse}.
     * @since 1.0
     */
    @PutMapping("/function/{reportId}")
    @PreAuthorize("hasAuthority('STAFF')")
    public ResponseEntity<ResponseData<UpdateFunctionReportResponse>> updateFunctionReport(
            @PathVariable Integer reportId,
            @RequestBody @Valid UpdateFunctionReportRequest request,
            Authentication authentication) {
        ResponseData<UpdateFunctionReportResponse> functionReportResponse = reportService.updateFunctionReport(reportId, request, authentication);
        if (functionReportResponse.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.ok(functionReportResponse);
        } else if (functionReportResponse.getStatus() == ResponseCode.C203.getCode()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(functionReportResponse);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(functionReportResponse);
    }

    /**
     * <h2>Find All Function Reports</h2>
     * <p>
     * Retrieves a paginated list of all function reports, optionally filtered by report ID, ticket ID, creator, and status.
     * The response includes summary details of each function report. The authenticated user information is used to verify permissions.
     * </p>
     *
     * @param pageable       Pagination details.
     * @param authentication The {@link Authentication} object representing the authenticated user.
     * @param reportId       Optional filter for the report ID.
     * @param ticketId       Optional filter for the ticket ID.
     * @param createBy       Optional filter for the creator ID.
     * @param status         Optional filter for the report status.
     * @return A {@link ResponseData} object containing a paginated list of function reports.
     * @since 1.0
     */
    @GetMapping("/functions")
    @PreAuthorize("hasAuthority('STAFF')")
    public ResponseEntity<ResponseData<Page<FindAllFunctionReportResponse>>> findAllFunctionReports(
            Pageable pageable,
            Authentication authentication,
            @RequestParam(required = false) Integer reportId,
            @RequestParam(required = false) String ticketId,
            @RequestParam(required = false) Integer createBy,
            @RequestParam(required = false) ReportStatus status) {
        ResponseData<Page<FindAllFunctionReportResponse>> functionReportsResponse = reportService.findAllFunctionReports(pageable, authentication, reportId, ticketId, createBy, status);
        if (functionReportsResponse.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.ok(functionReportsResponse);
        } else if (functionReportsResponse.getStatus() == ResponseCode.C203.getCode()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(functionReportsResponse);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(functionReportsResponse);
    }

}
