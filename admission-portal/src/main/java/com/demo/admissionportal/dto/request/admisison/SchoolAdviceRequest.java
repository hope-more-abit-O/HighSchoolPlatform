package com.demo.admissionportal.dto.request.admisison;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SchoolAdviceRequest implements Serializable {
    private List<Integer> majorId;
    private Float offset;
    private Float score;
    private List<Integer> subjectGroupId;
    private List<Integer> methodId;
    private List<Integer> provinceId;
    private Integer year;
}