package com.demo.admissionportal.dto.response.report.function_report;

import com.demo.admissionportal.constants.PostReportActionType;
import com.demo.admissionportal.constants.ReportStatus;
import com.demo.admissionportal.dto.entity.ActionerDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateFunctionReportResponseDTO {
    private Integer id;
    private String ticketId;
    private ActionerDTO createBy;
    private Date createTime;
    private String reportContent;
    private String reportType;
    private String proofs;
    private ReportStatus status;
    private Date updateTime;
    private Integer updateBy;
    private Date completeTime;
    private Integer completeBy;
    private String response;
    private PostReportActionType reportAction;
}
