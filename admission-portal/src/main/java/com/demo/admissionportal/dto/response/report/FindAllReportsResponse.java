package com.demo.admissionportal.dto.response.report;


import com.demo.admissionportal.dto.entity.ActionerDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FindAllReportsResponse {
    private Integer reportId;
    private String ticketId;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String postTitle;
    private ActionerDTO createBy;
    private Date createTime;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String postUrl;
    private String status;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String commentContent;
    private String reportType;
}
