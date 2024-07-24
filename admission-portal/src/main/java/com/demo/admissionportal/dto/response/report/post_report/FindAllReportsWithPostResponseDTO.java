package com.demo.admissionportal.dto.response.report.post_report;

import com.demo.admissionportal.constants.ReportStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FindAllReportsWithPostResponseDTO {
    private Integer reportId;
    private String ticketId;
    private Integer createBy;
    private Date createTime;
    private String content;
    private ReportStatus status;
    private String postUrl;
}
