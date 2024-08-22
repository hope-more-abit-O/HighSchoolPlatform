package com.demo.admissionportal.dto.request.admisison;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SchoolAdviceRequestV2 {
    private List<Integer> majorId;
    private Float offset;
    private Float score;
    private List<String> region;
    private List<Integer> subjectGroupId;
    private List<Integer> subjectId;
    private List<Integer> methodId;
    private List<Integer> provinceId;
}
