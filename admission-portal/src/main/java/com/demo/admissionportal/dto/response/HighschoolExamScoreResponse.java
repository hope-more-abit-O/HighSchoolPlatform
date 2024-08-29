package com.demo.admissionportal.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HighschoolExamScoreResponse {
    private String identificationNumber;
    private String local;
    private Integer year;
    private List<SubjectScoreDTO> subjectScores;
}
