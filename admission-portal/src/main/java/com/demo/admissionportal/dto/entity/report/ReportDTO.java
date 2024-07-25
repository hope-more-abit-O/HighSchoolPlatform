package com.demo.admissionportal.dto.entity.report;

import com.demo.admissionportal.constants.PostReportActionType;
import com.demo.admissionportal.constants.ReportType;

import java.util.Date;

public class ReportDTO {
    private Integer id;
    private Integer ticket_id;
    private Integer create_by;
    private Date create_time;
    private ReportType report_type;
}
