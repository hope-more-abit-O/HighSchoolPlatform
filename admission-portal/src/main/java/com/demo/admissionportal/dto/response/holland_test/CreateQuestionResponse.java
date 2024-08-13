package com.demo.admissionportal.dto.response.holland_test;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateQuestionResponse {
    private String content;
    private Integer createBy;
    private Date createTime;
    private String type;
    private List<String> jobNames;
    private String status;
}
