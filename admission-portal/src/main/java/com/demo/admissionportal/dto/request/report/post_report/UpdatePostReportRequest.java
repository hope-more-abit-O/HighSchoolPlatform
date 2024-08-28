package com.demo.admissionportal.dto.request.report.post_report;

import com.demo.admissionportal.util.enum_validator.EnumReportAction;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePostReportRequest {
    @NotBlank(message = "Hành động không được để trống !")
    @EnumReportAction
    private String reportAction;
    private String response;
}
