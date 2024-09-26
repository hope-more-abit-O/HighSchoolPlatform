package com.demo.admissionportal.dto;

import com.demo.admissionportal.dto.request.CreateHighschoolExamScoreRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExamYearData {
    private String title;
    private Integer year;
    private List<CreateHighschoolExamScoreRequest> examScoreData;
}