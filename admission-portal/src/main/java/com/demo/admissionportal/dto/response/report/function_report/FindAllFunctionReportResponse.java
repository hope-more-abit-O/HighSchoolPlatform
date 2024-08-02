package com.demo.admissionportal.dto.response.report.function_report;

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
public class FindAllFunctionReportResponse {
    private Integer reportId;
    private String ticketId;
    private ActionerDTO createBy;
    private String reportType;
    private Date createTime;
    private String status;
}
