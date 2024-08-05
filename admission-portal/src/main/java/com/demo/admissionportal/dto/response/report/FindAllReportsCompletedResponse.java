package com.demo.admissionportal.dto.response.report;

import com.demo.admissionportal.dto.entity.ActionerDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FindAllReportsCompletedResponse {
    private Integer reportId;
    private String ticketId;
    private ActionerDTO createBy;
    private Date createTime;
    private String reportType;
    private String status;
}
