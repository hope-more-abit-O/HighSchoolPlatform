package com.demo.admissionportal.dto.response.holland_test;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * The type Highest type response.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class HighestTypeResponse implements Serializable {
    @JsonIgnore
    private Integer typeQuestionId;
    private String typeQuestion;
    private String content;
    private String image;
}
