package com.demo.admissionportal.dto.request.report.comment_report;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateCommentReportRequest {
    @NotBlank(message = "Bình luận không thể để trống!")
    private Integer commentId;

    @NotBlank(message = "Lý do báo cáo không được để trống!")
    private String content;
}
