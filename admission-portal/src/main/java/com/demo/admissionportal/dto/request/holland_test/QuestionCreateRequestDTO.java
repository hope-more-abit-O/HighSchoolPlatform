package com.demo.admissionportal.dto.request.holland_test;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * The type Question create request dto.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class QuestionCreateRequestDTO implements Serializable {
    private Integer questionId;
    private String questionType;
}
