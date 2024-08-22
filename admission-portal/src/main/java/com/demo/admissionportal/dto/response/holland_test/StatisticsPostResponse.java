package com.demo.admissionportal.dto.response.holland_test;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * The type Statistics post response.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class StatisticsPostResponse implements Serializable {
    private Integer totalPost;
    private Integer currentPost;
    private Integer activePost;
    private Integer inactivePost;
}
