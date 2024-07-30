package com.demo.admissionportal.dto.request.report.comment_report;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCommentReportRequest {
    @NotNull(message = "Hành động báo cáo không thể để trống!")
    private String reportAction;

    @NotNull(message = "Phản hồi không thể để trống!")
    private String response;
}

