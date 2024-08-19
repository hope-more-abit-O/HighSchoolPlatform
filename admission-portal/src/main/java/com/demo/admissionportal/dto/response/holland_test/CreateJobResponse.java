package com.demo.admissionportal.dto.response.holland_test;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * The type Create job response.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class  CreateJobResponse implements Serializable {
    private Integer id;
    private String name;
    private String createBy;
    private Date createTime;
}
