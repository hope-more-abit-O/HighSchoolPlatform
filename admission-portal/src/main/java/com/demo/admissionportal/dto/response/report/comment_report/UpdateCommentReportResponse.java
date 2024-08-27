package com.demo.admissionportal.dto.response.report.comment_report;

import com.demo.admissionportal.dto.entity.ActionerDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCommentReportResponse {
    private Integer id;
    private String ticketId;
    private ActionerDTO actioner;
    private Date createTime;
    private String reportType;
    private String content;
    private Integer commentId;
    private String commentContent;
    private Integer commenterId;
    private Date commentCreateTime;
    private Date updateTime;
    private Integer updateBy;
    private Date completeTime;
    private Integer completeBy;
    private String reportAction;
    private String response;
    private String status;
    private String isBanned;
}
