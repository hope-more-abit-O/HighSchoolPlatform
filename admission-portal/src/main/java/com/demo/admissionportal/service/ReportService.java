package com.demo.admissionportal.service;

import com.demo.admissionportal.constants.ReportStatus;
import com.demo.admissionportal.constants.ReportType;
import com.demo.admissionportal.dto.entity.report.post_report.ReportPostDTO;
import com.demo.admissionportal.dto.request.report.comment_report.CreateCommentReportRequest;
import com.demo.admissionportal.dto.request.report.comment_report.UpdateCommentReportRequest;
import com.demo.admissionportal.dto.response.report.comment_report.CommentReportResponse;
import com.demo.admissionportal.dto.response.report.comment_report.ListAllCommentReportResponse;
import com.demo.admissionportal.dto.response.report.comment_report.UpdateCommentReportResponse;
import com.demo.admissionportal.dto.response.report.comment_report.UpdateCommentReportResponseDTO;
import com.demo.admissionportal.dto.response.report.post_report.*;
import com.demo.admissionportal.dto.request.report.post_report.CreatePostReportRequest;
import com.demo.admissionportal.dto.request.report.post_report.UpdatePostReportRequest;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.entity.sub_entity.CommentReport;
import com.demo.admissionportal.entity.sub_entity.PostReport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

/**
 * <h1>Report Service Interface</h1>
 * <p>
 * This interface defines the operations related to managing post reports within the Admission Portal.
 * It provides methods for creating, updating, retrieving, and listing post reports. Implementations of this
 * interface are responsible for the business logic associated with report management and interactions with
 * the underlying data layer.
 * </p>
 * <p>
 * The following operations are supported:
 * <ul>
 *     <li>Creating a new post report</li>
 *     <li>Updating an existing post report</li>
 *     <li>Retrieving details of a specific post report by its ID</li>
 *     <li>Listing all post reports with optional filters</li>
 * </ul>
 * </p>
 *
 * @since 1.0
 */
public interface ReportService {
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
    ResponseData<PostReport> createPostReport(CreatePostReportRequest request, Authentication authentication);

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
     * @return A {@link ResponseData} object containing the details of the post report along with {@link ReportPostDTO}.
     * @since 1.0
     */
    ResponseData<ReportPostResponse> getPostReportById(Integer reportId, Authentication authentication);

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
     * @return A {@link ResponseData} object containing the result of the update operation along with the updated {@link UpdatePostReportResponseDTO}.
     * @since 1.0
     */
    ResponseData<UpdatePostReportResponse> updatePostReport(Integer reportId, UpdatePostReportRequest request, Authentication authentication);

    /**
     * Create comment report response data.
     *
     * @param request        the request
     * @param authentication the authentication
     * @return the response data
     */
    ResponseData<CommentReport> createCommentReport(CreateCommentReportRequest request, Authentication authentication);

    /**
     * Gets comment report by id.
     *
     * @param reportId       the report id
     * @param authentication the authentication
     * @return the comment report by id
     */
    ResponseData<CommentReportResponse> getCommentReportById(Integer reportId, Authentication authentication);

    /**
     * Update comment report response data.
     *
     * @param reportId       the report id
     * @param request        the request
     * @param authentication the authentication
     * @return the response data
     */
    ResponseData<UpdateCommentReportResponse> updateCommentReport(Integer reportId, UpdateCommentReportRequest request, Authentication authentication);

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
    ResponseData<Page<ListAllPostReportResponse>> findAllPostReports(Pageable pageable, Authentication authentication, Integer reportId, String ticketId, Integer createBy, String content, ReportStatus status);

    /**
     * Find all completed post reports response data.
     *
     * @param pageable       the pageable
     * @param authentication the authentication
     * @param reportId       the report id
     * @param ticketId       the ticket id
     * @param createBy       the create by
     * @param reportType     the report type
     * @return the response data
     */
    ResponseData<Page<FindAllReportsCompletedResponse>> findAllCompletedPostReports(Pageable pageable, Authentication authentication,
                                                                                    Integer reportId, String ticketId, Integer createBy,
                                                                                    ReportType reportType);

    /**
     * Find all comment reports response data.
     *
     * @param pageable       the pageable
     * @param authentication the authentication
     * @param reportId       the report id
     * @param ticketId       the ticket id
     * @param createBy       the create by
     * @param content        the content
     * @param status         the status
     * @return the response data
     */
    ResponseData<Page<ListAllCommentReportResponse>> findAllCommentReports(Pageable pageable, Authentication authentication,
                                                                           Integer reportId, String ticketId, Integer createBy,
                                                                           String content, ReportStatus status);
}
