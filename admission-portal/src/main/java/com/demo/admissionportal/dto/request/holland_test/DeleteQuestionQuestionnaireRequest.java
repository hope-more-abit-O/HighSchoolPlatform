package com.demo.admissionportal.dto.request.holland_test;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * The type Delete question questionnaire request.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeleteQuestionQuestionnaireRequest implements Serializable {
    @NotBlank(message = "ID bộ câu hỏi không được trống")
    private Integer questionnaireId;
    @NotBlank(message = "ID câu hỏi không được trống")
    private Integer questionId;
}
