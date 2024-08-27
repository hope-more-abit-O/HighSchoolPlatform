package com.demo.admissionportal.dto.entity.report.comment_report;

import com.demo.admissionportal.constants.PostReportActionType;
import com.demo.admissionportal.constants.ReportStatus;
import com.demo.admissionportal.constants.ReportType;
import com.demo.admissionportal.constants.isBannedType;
import com.demo.admissionportal.dto.entity.ActionerDTO;
import lombok.*;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FindAllCommentReportsDTO {
    private Integer reportId;
    private String ticketId;
    private ActionerDTO createBy;
    private String postTitle;
    private Date createTime;
    private ReportType reportType;
    private String commentContent;
    private ReportStatus status;
    private PostReportActionType reportAction;
    private isBannedType isBanned;

    public FindAllCommentReportsDTO(Integer reportId, String ticketId, Integer createById, String postTitle, Date createTime, ReportStatus status, PostReportActionType reportAction, ReportType reportType, String commentContent, isBannedType isBanned) {
        this.reportId = reportId;
        this.ticketId = ticketId;
        this.createBy = new ActionerDTO(createById, null, null, null);
        this.postTitle = postTitle;
        this.createTime = createTime;
        this.reportType = reportType;
        this.status = status;
        this.reportAction = reportAction;
        this.commentContent = commentContent;
        this.isBanned = isBanned;
    }
}
