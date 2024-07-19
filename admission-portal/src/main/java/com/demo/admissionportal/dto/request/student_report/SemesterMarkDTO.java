package com.demo.admissionportal.dto.request.student_report;
import com.demo.admissionportal.constants.SemesterType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * The type Semester mark dto.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SemesterMarkDTO {
    @NotNull
    private SemesterType semester;
    @NotNull
    private Float mark;
}
