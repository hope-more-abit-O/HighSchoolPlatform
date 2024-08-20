package com.demo.admissionportal.dto.request.holland_test;

import jakarta.validation.constraints.NotNull;
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
    @NotNull(message = "ID bộ câu hỏi không được trống")
    private Integer questionnaireId;
    @NotNull(message = "ID câu hỏi không được trống")
    private Integer questionId;
}
