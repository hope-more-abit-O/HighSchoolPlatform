package com.demo.admissionportal.dto.request.student_report;
import com.demo.admissionportal.constants.SemesterType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SemesterMarkDTO {
    private SemesterType semester;
    private Float mark;
}
