package com.demo.admissionportal.dto.entity.admission;

import com.demo.admissionportal.dto.entity.ActionerDTO;
import com.demo.admissionportal.dto.entity.university.InfoUniversityResponseDTO;
import com.demo.admissionportal.entity.admission.Admission;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class FullAdmissionDTO {
    private Integer admissionId;
    private Integer year;
    private String name;
    private List<String> sources;
    private InfoUniversityResponseDTO university;
    private ActionerDTO createBy;
    private Date createTime;
    private ActionerDTO updateBy;
    private Date updateTime;
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

    public FullAdmissionDTO(Admission admission) {
        this.admissionId = admission.getId();
        this.year = admission.getYear();
        this.createBy =  null;
        this.createTime = admission.getCreateTime();
        this.updateBy =  null;
        this.updateTime = admission.getUpdateTime();
        this.status = admission.getSource();
    }
}
