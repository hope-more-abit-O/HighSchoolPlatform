package com.demo.admissionportal.dto.response.report.comment_report;

import com.demo.admissionportal.constants.PostReportActionType;
import com.demo.admissionportal.constants.ReportStatus;
import com.demo.admissionportal.constants.ReportType;
import com.demo.admissionportal.dto.entity.ActionerDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCommentReportResponseDTO {
    private Integer id;
    private String ticketId;
    private ActionerDTO actioner;
    private Date createTime;
    private ReportType reportType;
    private String content;
    private Integer commentId;
    private String commentContent;
    private Integer commenterId;
    private Date commentCreateTime;
    private Date updateTime;
    private Integer updateBy;
    private Date completeTime;
    private Integer completeBy;
    private PostReportActionType reportAction;
    private String response;
    private ReportStatus status;

    public UpdateCommentReportResponseDTO(Integer id, String ticketId, ActionerDTO actioner, Date createTime, ReportType reportType, String content, Integer commentId, String commentContent, Integer commenterId, Date commentCreateTime, Date updateTime, Integer updateBy, Date completeTime, Integer completeBy, String reportAction, String response, ReportStatus status) {
        this.id = id;
        this.ticketId = ticketId;
        this.actioner = actioner;
        this.createTime = createTime;
        this.reportType = reportType;
        this.content = content;
        this.commentId = commentId;
        this.commentContent = commentContent;
        this.commenterId = commenterId;
        this.commentCreateTime = commentCreateTime;
        this.updateTime = updateTime;
        this.updateBy = updateBy;
        this.completeTime = completeTime;
        this.completeBy = completeBy;
        this.reportAction = PostReportActionType.valueOf(reportAction);
        this.response = response;
        this.status = status;
    }
}
