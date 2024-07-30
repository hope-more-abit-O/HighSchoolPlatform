package com.demo.admissionportal.dto.response.report.comment_report;

import com.demo.admissionportal.constants.CommentType;
import com.demo.admissionportal.constants.ReportStatus;
import com.demo.admissionportal.constants.ReportType;
import com.demo.admissionportal.dto.entity.ActionerDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentReportResponse {
    private Integer id;
    private String ticketId;
    private ActionerDTO createBy;
    private Date createTime;
    private String reportContent;
    private String reportType;
    private String status;
    private Integer commentId;
    private String content;
    private Integer commentCreateBy;
    private Date commentCreateTime;
    private String commentType;
}
