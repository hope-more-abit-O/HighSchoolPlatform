package com.demo.admissionportal.dto.request.post_report;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreatePostReportRequest {
    private Integer report_id;
    @NotNull
    private Integer post_id;
    @NotNull
    private String content;
    private Integer createBy;
    private Date createTime;
}
