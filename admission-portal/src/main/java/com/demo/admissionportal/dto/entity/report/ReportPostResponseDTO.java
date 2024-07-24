package com.demo.admissionportal.dto.entity.report;

import com.demo.admissionportal.constants.ReportStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class ReportPostResponseDTO {
    private Integer reportId;
    private String ticketId;
    private Integer createBy;
    private Date createTime;
    private String reportType;
    private String content;

    private Integer postId;
    private String title;
    private Integer postCreateBy;
    private Date postCreateTime;
    private ReportStatus status;
}