package com.demo.admissionportal.dto.request.holland_test;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * The type Submit question request dto.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SubmitQuestionRequestDTO implements Serializable {
    private Integer question_id;
}
