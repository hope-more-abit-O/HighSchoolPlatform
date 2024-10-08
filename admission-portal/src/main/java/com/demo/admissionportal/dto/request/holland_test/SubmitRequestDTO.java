package com.demo.admissionportal.dto.request.holland_test;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * The type Submit request dto.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubmitRequestDTO implements Serializable {
    private Integer testResponseId;
    private List<SubmitQuestionRequestDTO> question;
}
