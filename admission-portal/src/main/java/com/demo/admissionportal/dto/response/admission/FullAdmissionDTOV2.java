package com.demo.admissionportal.dto.response.admission;

import com.demo.admissionportal.dto.entity.ActionerDTO;
import com.demo.admissionportal.dto.entity.admission.*;
import com.demo.admissionportal.dto.entity.university.InfoUniversityResponseDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FullAdmissionDTOV2 {
    private Integer admissionId;
    private Integer year;
    private String name;
    private List<String> sources;
    private String note;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private InfoUniversityResponseDTO university;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ActionerDTO createBy;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Date createTime;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ActionerDTO updateBy;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Date updateTime;

    private String status;

    private String scoreStatus;

    private String confirmStatus;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<AdmissionMethodDTO> admissionMethods;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<AdmissionTrainingProgramDTO> admissionTrainingPrograms;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<AdmissionTrainingProgramSubjectGroupDTO> admissionTrainingProgramSubjectGroups;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<FullAdmissionQuotaDTOV2> details;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer totalMethods;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer totalMajors;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer totalQuota;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<ScoreRange> scoreRanges;


}
