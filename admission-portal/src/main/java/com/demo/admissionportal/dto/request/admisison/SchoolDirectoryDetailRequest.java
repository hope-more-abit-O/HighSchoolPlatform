package com.demo.admissionportal.dto.request.admisison;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SchoolDirectoryDetailRequest {
    private List<Integer> admissionTrainingProgramIds;
    private List<Integer> admissionMethodIds;
    private Integer admissionId;
}
