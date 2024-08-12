package com.demo.admissionportal.dto.request;

import com.demo.admissionportal.dto.response.SubjectScoreDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateHighschoolExamScoreRequest {
    private String title;
    private Integer year;
    private Integer identificationNumber;
    private List<SubjectScoreDTO> subjectScores;
}

