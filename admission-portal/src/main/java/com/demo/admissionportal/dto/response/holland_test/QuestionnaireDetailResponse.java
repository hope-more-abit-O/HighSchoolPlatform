package com.demo.admissionportal.dto.response.holland_test;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * The type Questionnaire response.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class QuestionnaireDetailResponse implements Serializable {
    private String name;
    private String description;
    private String coverImage;
    private List<QuestionResponse> questions;
}
