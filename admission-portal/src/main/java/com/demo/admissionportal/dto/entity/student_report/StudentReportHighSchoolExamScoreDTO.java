package com.demo.admissionportal.dto.entity.student_report;

import com.demo.admissionportal.util.enum_validator.EnumId;
import com.demo.admissionportal.util.enum_validator.EnumScore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentReportHighSchoolExamScoreDTO {
    @EnumId(message = "Id môn học phải lớn hơn 0.")
    private Integer subjectId;
    @EnumScore
    private Float score;
}
