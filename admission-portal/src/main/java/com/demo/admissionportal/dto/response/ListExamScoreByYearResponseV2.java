package com.demo.admissionportal.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListExamScoreByYearResponseV2 {
    private Integer id;
    private String title;
    private Integer year;
    private List<HighschoolExamScoreResponse> examScores;
}