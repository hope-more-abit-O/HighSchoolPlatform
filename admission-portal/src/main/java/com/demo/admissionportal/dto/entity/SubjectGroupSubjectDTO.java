package com.demo.admissionportal.dto.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubjectGroupSubjectDTO {
    private String subjectGroupName;
    private List<String> subjectName;
}
