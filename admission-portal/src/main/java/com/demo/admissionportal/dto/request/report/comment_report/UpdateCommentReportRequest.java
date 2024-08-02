package com.demo.admissionportal.dto.request.report.comment_report;

import com.demo.admissionportal.util.enum_validator.EnumReportAction;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCommentReportRequest {
    @NotNull(message = "Hành động không thể để trống!")
    @EnumReportAction
    private String reportAction;
    private String response;
    private Boolean isBanned;
}

