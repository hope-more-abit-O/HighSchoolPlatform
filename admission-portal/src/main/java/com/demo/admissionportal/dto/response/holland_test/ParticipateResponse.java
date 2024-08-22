package com.demo.admissionportal.dto.response.holland_test;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * The type Participate response.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ParticipateResponse implements Serializable {
    private Integer question_id;
    private String content;
}
