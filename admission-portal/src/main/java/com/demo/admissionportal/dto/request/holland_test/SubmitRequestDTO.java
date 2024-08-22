package com.demo.admissionportal.dto.request.holland_test;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * The type Submit request dto.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubmitRequestDTO implements Serializable {
    private Integer question_id;
}
