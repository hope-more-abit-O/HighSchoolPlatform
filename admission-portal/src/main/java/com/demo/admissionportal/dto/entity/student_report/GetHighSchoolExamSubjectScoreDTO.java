package com.demo.admissionportal.dto.entity.student_report;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetHighSchoolExamSubjectScoreDTO {
    private Boolean isNaturalScience;
    private List<GetStudentReportHighSchoolExamScoreDTO> mainSubjects;
    private List<GetStudentReportHighSchoolExamScoreDTO> naturalScienceSubjects;
    private List<GetStudentReportHighSchoolExamScoreDTO> socialScienceSubjects;

    public static GetHighSchoolExamSubjectScoreDTO mapping(List<GetStudentReportHighSchoolExamScoreDTO> scores){
        List<Integer> mainSubjectIds = List.of(36,38,28);
        List<Integer> naturalScienceSubjectIds = List.of(16,23,27);
        List<Integer> socialScienceSubjectIds = List.of(9,54,34);
        List<GetStudentReportHighSchoolExamScoreDTO> mainSubjects = new ArrayList<>();
        List<GetStudentReportHighSchoolExamScoreDTO> naturalScienceSubjects = new ArrayList<>();
        List<GetStudentReportHighSchoolExamScoreDTO> socialScienceSubjects = new ArrayList<>();
        GetHighSchoolExamSubjectScoreDTO result = new GetHighSchoolExamSubjectScoreDTO();
        for (GetStudentReportHighSchoolExamScoreDTO score : scores) {
            if (mainSubjectIds.contains(score.getSubject().getSubjectId()))
                mainSubjects.add(score);
            else if (naturalScienceSubjectIds.contains(score.getSubject().getSubjectId()))
                naturalScienceSubjects.add(score);
            else if (socialScienceSubjectIds.contains(score.getSubject().getSubjectId()))
                socialScienceSubjects.add(score);
        }

        result.mainSubjects = mainSubjects;
        result.naturalScienceSubjects = naturalScienceSubjects;
        result.socialScienceSubjects = socialScienceSubjects;

        Float naturalScienceScore = naturalScienceSubjects.stream().map(GetStudentReportHighSchoolExamScoreDTO::getScore).filter(Objects::nonNull).findFirst().orElse(null);
        Float socialScienceScore = socialScienceSubjects.stream().map(GetStudentReportHighSchoolExamScoreDTO::getScore).filter(Objects::nonNull).findFirst().orElse(null);

        if (naturalScienceScore == null && socialScienceScore == null){
            result.isNaturalScience = null;
        }
        else result.isNaturalScience = naturalScienceScore != null;

        return result;
    }
}
