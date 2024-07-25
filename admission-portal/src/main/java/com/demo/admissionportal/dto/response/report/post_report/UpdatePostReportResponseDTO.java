package com.demo.admissionportal.dto.response.report.post_report;

import com.demo.admissionportal.constants.ReportStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePostReportResponseDTO {
    private Integer reportId;
    private String ticketId;
    private Integer createBy;
    private Date createTime;
    private String reportType;
    private String content;
    private Integer postId;
    private String postTitle;
    private Integer postCreateBy;
    private Date postCreateTime;
    private Date updateTime;
    private Integer updateBy;
    private Date completeTime;
    private Integer completeBy;
    private String reportAction;
    private String response;
    private ReportStatus status;
}