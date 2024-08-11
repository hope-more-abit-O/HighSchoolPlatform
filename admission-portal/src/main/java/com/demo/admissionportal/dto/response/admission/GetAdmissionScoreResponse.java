package com.demo.admissionportal.dto.response.admission;

import com.demo.admissionportal.dto.entity.ActionerDTO;
import com.demo.admissionportal.dto.entity.admission.AdmissionMethodDTO;
import com.demo.admissionportal.dto.entity.admission.AdmissionTrainingProgramDTO;
import com.demo.admissionportal.dto.entity.admission.AdmissionTrainingProgramSubjectGroupDTO;
import com.demo.admissionportal.dto.entity.admission.FullAdmissionQuotaDTO;
import com.demo.admissionportal.dto.entity.major.InfoMajorDTO;
import com.demo.admissionportal.dto.entity.method.InfoMethodDTO;
import com.demo.admissionportal.dto.entity.university.InfoUniversityResponseDTO;
import com.demo.admissionportal.entity.admission.Admission;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetAdmissionScoreResponse {
    private Integer admissionId;
    private Integer year;
    private String name;
    private String status;
    private String scoreStatus;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<AdmissionMethodDTO> admissionMethods;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<AdmissionTrainingProgramDTO> admissionTrainingPrograms;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<AdmissionTrainingProgramSubjectGroupDTO> admissionTrainingProgramSubjectGroups;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<FullAdmissionQuotaDTO> details;

    public GetAdmissionScoreResponse(Admission admission) {
        this.admissionId = admission.getId();
        this.year = admission.getYear();
        this.status = admission.getSource();
    }
}
