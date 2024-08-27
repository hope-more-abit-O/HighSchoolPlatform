package com.demo.admissionportal.dto.entity.admission.school_advice;

import com.demo.admissionportal.dto.entity.admission.AdmissionTrainingProgramDTOV2;
import com.demo.admissionportal.dto.entity.university.InfoUniversityResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SchoolAdviceDTO {
    private InfoUniversityResponseDTO universityInfo;
    private String source;
    private Integer majorCount;
    private List<AdmissionTrainingProgramDTOV2> detail;
}
