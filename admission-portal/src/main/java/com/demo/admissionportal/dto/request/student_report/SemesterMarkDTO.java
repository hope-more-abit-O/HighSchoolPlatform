package com.demo.admissionportal.dto.request.student_report;
import com.demo.admissionportal.constants.SemesterType;
import lombok.Data;

import java.util.List;

@Data
public class SemesterMarkDTO {
    private SemesterType semester;
    private Float mark;
}
