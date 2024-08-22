package com.demo.admissionportal.dto.response.holland_test;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * The type Suggest major response.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class SuggestJobResponse implements Serializable {
    private String jobName;
    private String image;
}
