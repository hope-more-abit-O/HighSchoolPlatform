package com.demo.admissionportal.dto.response.holland_test;

import com.demo.admissionportal.constants.QuestionStatus;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * The type Questionnaire response.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class QuestionnaireResponse implements Serializable {
    private Integer id;
    private String code;
    private String name;
    private Integer numberOfQuestions;
    private String createBy;
    private Date createTime;
    private String status;
}
