package com.demo.admissionportal.service;

import com.demo.admissionportal.dto.request.post_report.CreatePostReportRequest;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.entity.sub_entity.PostReport;
import org.springframework.security.core.Authentication;

public interface PostReportService {
    ResponseData<PostReport> createPostReport(CreatePostReportRequest request, Authentication authentication);
}
