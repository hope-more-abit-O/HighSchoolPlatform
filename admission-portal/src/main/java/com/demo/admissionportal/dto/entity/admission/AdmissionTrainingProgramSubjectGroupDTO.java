package com.demo.admissionportal.dto.entity.admission;

import com.demo.admissionportal.dto.response.sub_entity.SubjectGroupResponseDTO2;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdmissionTrainingProgramSubjectGroupDTO {
    private Integer admissionTrainingProgramId;
    private List<SubjectGroupResponseDTO2> subjectGroups;
}
