package com.demo.admissionportal.dto;

import com.demo.admissionportal.constants.SubjectStatus;
import lombok.Data;

@Data
public class AvailableSubjectGroupInAdmission {
    private Integer subjectGroupId;
    private Integer subjectGroupName;
    private SubjectStatus status;
}
