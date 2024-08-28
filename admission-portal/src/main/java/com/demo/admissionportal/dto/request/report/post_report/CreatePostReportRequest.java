package com.demo.admissionportal.dto.request.report.post_report;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreatePostReportRequest {
    @NotNull(message = "Bài viết không thể để trống !")
    private Integer postId;
    @NotNull(message = "Lý do báo cáo không được để trống !")
    private String content;
}
