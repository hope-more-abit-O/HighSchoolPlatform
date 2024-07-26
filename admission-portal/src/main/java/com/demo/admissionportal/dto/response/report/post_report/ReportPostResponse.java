package com.demo.admissionportal.dto.response.report.post_report;

import com.demo.admissionportal.dto.entity.ActionerDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReportPostResponse {
    private Integer reportId;
    private String ticketId;
    private ActionerDTO createBy;
    private Date createTime;
    private String reportType;
    private String content;

    private Integer postId;
    private String title;
    private Integer postCreateBy;
    private Date postCreateTime;
    private String status;
}