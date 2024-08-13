package com.demo.admissionportal.dto.response.holland_test;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * The type Delete question response.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DeleteQuestionResponse implements Serializable {
    private String currentStatus;
}
