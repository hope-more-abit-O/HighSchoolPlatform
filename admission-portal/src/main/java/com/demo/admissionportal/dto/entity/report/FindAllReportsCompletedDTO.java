package com.demo.admissionportal.dto.entity.report;

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
public class FindAllReportsCompletedDTO {
    private Integer reportId;
    private String ticketId;
    private ActionerDTO createBy;
    private Date createTime;
    private ReportStatus status;
    private ReportType reportType;

    public FindAllReportsCompletedDTO(Integer reportId, String ticketId, Integer createById, Date createTime, ReportStatus status, ReportType reportType) {
        this.reportId = reportId;
        this.ticketId = ticketId;
        this.createBy = new ActionerDTO(createById, null, null, null);
        this.createTime = createTime;
        this.reportType = reportType;
        this.status = status;
    }
}
