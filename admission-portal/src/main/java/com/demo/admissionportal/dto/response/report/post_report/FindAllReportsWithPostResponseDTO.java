package com.demo.admissionportal.dto.response.report.post_report;

import com.demo.admissionportal.constants.ReportStatus;
import com.demo.admissionportal.dto.entity.ActionerDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FindAllReportsWithPostResponseDTO {
    private Integer reportId;
    private String ticketId;
    private ActionerDTO createBy;
    private Date createTime;
    private String content;
    private ReportStatus status;
    private String postUrl;

    public FindAllReportsWithPostResponseDTO(Integer reportId, String ticketId, Integer createById, Date createTime, String content, ReportStatus status, String postUrl) {
        this.reportId = reportId;
        this.ticketId = ticketId;
        this.createBy = new ActionerDTO(createById, null, null, null);
        this.createTime = createTime;
        this.content = content;
        this.status = status;
        this.postUrl = postUrl;
    }
}
