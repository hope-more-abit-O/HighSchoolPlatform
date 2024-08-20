package com.demo.admissionportal.dto.response.holland_test;

import com.demo.admissionportal.constants.QuestionStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class QuestionResponse implements Serializable {
    private Integer questionId;
    private String content;
    private String questionType;
    private String jobName;
    private Date createTime;
    private String status;
}
