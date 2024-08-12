package com.demo.admissionportal.dto;

import com.demo.admissionportal.dto.response.HighschoolExamScoreResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class YearlyExamScoreResponse {
    private String title;
    private int year;
    private List<HighschoolExamScoreResponse> examScores;
}
