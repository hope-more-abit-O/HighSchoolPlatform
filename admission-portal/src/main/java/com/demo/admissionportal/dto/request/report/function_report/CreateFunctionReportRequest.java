package com.demo.admissionportal.dto.request.report.function_report;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateFunctionReportRequest {
    @NotNull(message = "Nội dung không thể để trống!")
    private String content;
    @NotNull(message = "Lý do báo cáo không được để trống !")
    private MultipartFile[] proofs;
}