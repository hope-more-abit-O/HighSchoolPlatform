package com.demo.admissionportal.dto.request.report.function_report;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateFunctionReportRequest {
    @NotBlank(message = "Nội dung không thể để trống!")
    private String content;
    @NotBlank(message = "Lý do báo cáo không được để trống !")
    private MultipartFile[] proofs;
}