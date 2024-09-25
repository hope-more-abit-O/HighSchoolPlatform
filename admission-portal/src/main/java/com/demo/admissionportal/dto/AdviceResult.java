package com.demo.admissionportal.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AdviceResult {
    private String universityName;
    private String majorName;
    private int quota2023;
    private int quota2024;
    private String subjectGroupName;
    private float avgScoreForSubjectGroup2023;
    private float avgScoreForSubjectGroup2024;
    private float admissionScore2023;
    private float userScore2024;
    private String advice;
    private String avgDiffMessage;
    private String quotaDiffMessage;
}
