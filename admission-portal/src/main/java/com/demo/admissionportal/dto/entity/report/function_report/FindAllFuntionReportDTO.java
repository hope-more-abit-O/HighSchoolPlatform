package com.demo.admissionportal.dto.entity.report.function_report;

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
public class FindAllFuntionReportDTO {
    private Integer reportId;
    private String ticketId;
    private ActionerDTO createBy;
    private ReportType reportType;
    private Date createTime;
    private ReportStatus status;

    public FindAllFuntionReportDTO(Integer reportId, ReportStatus status, Date createTime, ReportType reportType, Integer createById, String ticketId) {
        this.reportId = reportId;
        this.status = status;
        this.createTime = createTime;
        this.reportType = reportType;
        this.createBy = new ActionerDTO(createById, null, null, null);
        this.ticketId = ticketId;
    }
}
