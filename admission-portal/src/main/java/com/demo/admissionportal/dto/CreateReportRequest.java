package com.demo.admissionportal.dto;

import com.demo.admissionportal.dto.request.report.comment_report.CreateCommentReportRequest;
import com.demo.admissionportal.dto.request.report.post_report.CreatePostReportRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateReportRequest {
    private CreatePostReportRequest postReport;
    private CreateCommentReportRequest commentReport;
}
