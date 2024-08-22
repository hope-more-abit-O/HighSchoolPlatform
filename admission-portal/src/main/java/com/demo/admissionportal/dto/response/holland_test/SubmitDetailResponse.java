package com.demo.admissionportal.dto.response.holland_test;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * The type Submit detail response.
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class SubmitDetailResponse implements Serializable {
    private String typeQuestions;
    private Integer numberOfSubmit;
}
