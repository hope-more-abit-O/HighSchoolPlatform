package com.demo.admissionportal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubjectGroupInfoDTO {
    private Integer id;
    private String name;
    private List<SubjectInfDTO> subjects;

    public SubjectGroupInfoDTO(Integer subjectGroupId, String subjectGroupName, SubjectInfDTO subjectResult) {
    }
}
