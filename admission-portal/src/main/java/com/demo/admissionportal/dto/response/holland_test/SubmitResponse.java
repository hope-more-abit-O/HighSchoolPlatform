package com.demo.admissionportal.dto.response.holland_test;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * The type Submit response.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class SubmitResponse implements Serializable {
    private List<SubmitDetailResponse> submitDetail;
    private List<HighestTypeResponse> highestType;
    private List<SuggestJobResponse> suggestJob;
}
