package com.demo.admissionportal.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HighschoolExamScoreResponseV2 {
    private Integer identificationNumber;
    private String local;
    private String examinationBoard;
    private String dateOfBirth;
    private String examiner;
    private Integer year;
    private List<SubjectScoreDTO> subjectScores;
    private int totalElements;
    private int totalPages;
    private int currentPage;
}

