package com.demo.admissionportal.dto.entity.report.post_report;

import com.demo.admissionportal.constants.PostReportActionType;
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
public class FindAllReportsWithPostDTO {
    private Integer reportId;
    private String ticketId;
    private ActionerDTO createBy;
    private Date createTime;
    private String content;
    private ReportStatus status;
    private ReportType reportType;
    private String postUrl;
    private PostReportActionType reportAction;

    public FindAllReportsWithPostDTO(Integer reportId, String ticketId, Integer createById, Date createTime, String content, ReportStatus status, ReportType reportType, String postUrl, PostReportActionType reportAction) {
        this.reportId = reportId;
        this.ticketId = ticketId;
        this.createBy = new ActionerDTO(createById, null, null, null);
        this.createTime = createTime;
        this.content = content;
        this.reportType = reportType;
        this.status = status;
        this.postUrl = postUrl;
        this.reportAction = reportAction;
    }
}
