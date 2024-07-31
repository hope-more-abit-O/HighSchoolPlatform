package com.demo.admissionportal.dto.entity.report.comment_report;

import com.demo.admissionportal.constants.ReportStatus;
import com.demo.admissionportal.constants.ReportType;
import com.demo.admissionportal.dto.entity.ActionerDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class FindAllCommentReportsByStatusDTO {
    private Integer reportId;
    private String ticketId;
    private ActionerDTO createBy;
    private Date createTime;
    private ReportType reportType;
    private ReportStatus status;

    public FindAllCommentReportsByStatusDTO(Integer reportId, String ticketId, Integer createById, Date createTime, ReportType reportType, ReportStatus status) {
        this.reportId = reportId;
        this.ticketId = ticketId;
        this.createBy = new ActionerDTO(createById, null, null, null);
        this.createTime = createTime;
        this.reportType = reportType;
        this.status = status;
    }
}
