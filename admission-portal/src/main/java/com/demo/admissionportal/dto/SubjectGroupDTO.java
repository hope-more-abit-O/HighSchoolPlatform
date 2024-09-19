package com.demo.admissionportal.dto;

import com.demo.admissionportal.constants.SubjectStatus;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
public class SubjectGroupDTO {
    private Integer subjectGroupId;
    private String subjectGroupName;
    private SubjectStatus status;

    public SubjectGroupDTO(Integer id, String name, SubjectStatus status) {
        this.subjectGroupId = id;
        this.subjectGroupName = name;
        this.status = status;
    }
}