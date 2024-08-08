package com.demo.admissionportal.dto.request.admisison;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SchoolAdviceRequest implements Serializable {
    private String majorCode;
    private Float offset;
    private Float score;
    private Integer subjectGroupId;
    private Integer methodId;
    private Integer provinceId;
}
