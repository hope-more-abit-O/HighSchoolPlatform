package com.demo.admissionportal.dto.response;

import com.demo.admissionportal.dto.entity.ActionerDTO;
import com.demo.admissionportal.dto.response.report.comment_report.CommentReportResponse;
import com.demo.admissionportal.dto.response.report.function_report.FunctionReportResponse;
import com.demo.admissionportal.dto.response.report.post_report.ReportPostResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportResponse {
    private Integer id;
    private String ticketId;
    private ActionerDTO createBy;
    private Date createTime;
    private String reportContent;
    private String reportType;
    private String status;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private FunctionReportResponse function;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private CommentReportResponse comment;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ReportPostResponse post;
}
