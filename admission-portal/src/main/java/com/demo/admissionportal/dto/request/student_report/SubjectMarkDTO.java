package com.demo.admissionportal.dto.request.student_report;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The type Subject mark dto.
 */
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