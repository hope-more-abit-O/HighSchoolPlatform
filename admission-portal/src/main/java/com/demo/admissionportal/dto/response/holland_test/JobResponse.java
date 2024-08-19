package com.demo.admissionportal.dto.response.holland_test;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * The type Job response.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobResponse implements Serializable {
    private Integer id;
    private String name;
    private String createBy;
    private Date createTime;
}
