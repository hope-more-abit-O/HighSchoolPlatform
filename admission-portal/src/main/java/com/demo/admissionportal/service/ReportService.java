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

public interface ReportService {
    ResponseData<PostReport> createPostReport(CreatePostReportRequest request, Authentication authentication);
    ResponseData<ReportPostResponseDTO> getPostReportById(Integer reportId, Authentication authentication);
    ResponseData<UpdatePostReportResponseDTO> updatePostReport(Integer reportId, UpdatePostReportRequest request, Authentication authentication);
    ResponseData<Page<FindAllReportsWithPostResponseDTO>> findAllPostReports(Pageable pageable, Authentication authentication);
}
