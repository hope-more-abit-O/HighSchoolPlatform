package com.demo.admissionportal.dto.response.report.comment_report;

import com.demo.admissionportal.dto.entity.ActionerDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListAllCommentReportResponse {
    private Integer reportId;
    private String ticketId;
    private String postTitle;
    private Date createTime;
    private String commentContent;
    private String reportType;
    private String status;
}
