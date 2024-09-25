package com.demo.admissionportal.dto.response.subject;

import com.demo.admissionportal.dto.entity.SubjectDTO;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetHighSchoolExamSubjectsResponse {
    private List<SubjectDTO> mainSubjects;
    private List<SubjectDTO> naturalScienceSubjects;
    private List<SubjectDTO> socialScienceSubjects;

    public GetHighSchoolExamSubjectsResponse(List<SubjectDTO> subjectDTOS) {
        List<Integer> mainSubjectIds = List.of(36,38,28);
        List<Integer> naturalScienceSubjectIds = List.of(16,23,27);
        List<Integer> socialScienceSubjectIds = List.of(9,54,34);
        this.mainSubjects = subjectDTOS.stream().filter((element) -> mainSubjectIds.contains(element.getSubjectId())).toList();
        this.naturalScienceSubjects = subjectDTOS.stream().filter((element) -> naturalScienceSubjectIds.contains(element.getSubjectId())).toList();
        this.socialScienceSubjects = subjectDTOS.stream().filter((element) -> socialScienceSubjectIds.contains(element.getSubjectId())).toList();
    }
}
