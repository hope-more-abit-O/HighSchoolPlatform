package com.demo.admissionportal.dto.response.holland_test;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * The type Question response.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuestionResponse implements Serializable {
    private Integer questionId;
    private String content;
    private String questionType;
    private String jobName;
    private Date createTime;
    private String status;
}
