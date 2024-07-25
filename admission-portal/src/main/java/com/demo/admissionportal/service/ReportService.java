package com.demo.admissionportal.service;

import com.demo.admissionportal.constants.ReportStatus;
import com.demo.admissionportal.dto.entity.report.ReportPostResponseDTO;
import com.demo.admissionportal.dto.request.report.post_report.CreatePostReportRequest;
import com.demo.admissionportal.dto.request.report.post_report.UpdatePostReportRequest;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.dto.response.report.post_report.FindAllReportsWithPostResponseDTO;
import com.demo.admissionportal.dto.response.report.post_report.UpdatePostReportResponseDTO;
import com.demo.admissionportal.entity.sub_entity.PostReport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

import java.util.Date;

/**
 * The interface Report service.
 */
public interface ReportService {
    /**
     * Create post report response data.
     *
     * @param request        the request
     * @param authentication the authentication
     * @return the response data
     */
    ResponseData<PostReport> createPostReport(CreatePostReportRequest request, Authentication authentication);

    /**
     * Gets post report by id.
     *
     * @param reportId       the report id
     * @param authentication the authentication
     * @return the post report by id
     */
    ResponseData<ReportPostResponseDTO> getPostReportById(Integer reportId, Authentication authentication);

    /**
     * Update post report response data.
     *
     * @param reportId       the report id
     * @param request        the request
     * @param authentication the authentication
     * @return the response data
     */
    ResponseData<UpdatePostReportResponseDTO> updatePostReport(Integer reportId, UpdatePostReportRequest request, Authentication authentication);

    /**
     * Find all post reports response data.
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
    ResponseData<Page<FindAllReportsWithPostResponseDTO>> findAllPostReports(Pageable pageable, Authentication authentication, Integer reportId, String ticketId, Integer createBy, String content, ReportStatus status);
}
