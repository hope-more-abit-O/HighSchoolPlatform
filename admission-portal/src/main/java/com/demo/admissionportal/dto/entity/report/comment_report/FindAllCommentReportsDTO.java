package com.demo.admissionportal.dto.entity.report.comment_report;

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
public class FindAllCommentReportsDTO {
    private Integer reportId;
    private String ticketId;
    private String postTitle;
    private Date createTime;
    private ReportType reportType;
    private String commentContent;
    private ReportStatus status;


    public FindAllCommentReportsDTO(Integer reportId, String ticketId, String postTitle, Date createTime, ReportStatus status, ReportType reportType, String commentContent) {
        this.reportId = reportId;
        this.ticketId = ticketId;
        this.postTitle = postTitle;
        this.createTime = createTime;
        this.reportType = reportType;
        this.status = status;
        this.commentContent = commentContent;
    }
}
