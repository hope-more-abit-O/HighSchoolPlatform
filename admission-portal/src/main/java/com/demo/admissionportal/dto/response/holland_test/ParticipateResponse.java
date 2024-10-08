package com.demo.admissionportal.dto.response.holland_test;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * The type Participate response.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ParticipateResponse implements Serializable {
    private Integer testResponseId;
    private List<ParticipateQuestionResponse> question;
}
