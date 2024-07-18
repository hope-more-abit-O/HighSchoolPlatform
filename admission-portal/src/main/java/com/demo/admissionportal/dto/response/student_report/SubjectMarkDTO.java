package com.demo.admissionportal.dto.response.student_report;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubjectMarkDTO {
    private Integer subjectId;
    private String subjectName;
    private String grade;
    private String semester;
    private Float mark;
}