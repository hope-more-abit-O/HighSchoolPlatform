package com.demo.admissionportal.dto.request.student_report;

import com.demo.admissionportal.constants.SemesterType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The type Update mark dto.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateMarkDTO {
    private Integer subjectId;
    private Integer grade;
    private SemesterType semester;
    private Float mark;
}
