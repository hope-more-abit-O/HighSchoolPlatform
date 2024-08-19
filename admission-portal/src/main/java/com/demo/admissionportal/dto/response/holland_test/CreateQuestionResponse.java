package com.demo.admissionportal.dto.response.holland_test;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * The type Create question response.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateQuestionResponse {
    private String content;
    private String type;
    private List<String> jobNames;
}
