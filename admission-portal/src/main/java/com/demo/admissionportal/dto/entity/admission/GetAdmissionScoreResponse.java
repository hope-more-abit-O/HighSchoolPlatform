package com.demo.admissionportal.dto.entity.admission;

import com.demo.admissionportal.dto.response.admission.AdmissionDetailDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetAdmissionScoreResponse {
    private List<AdmissionWithUniversityInfoDTO> admissions;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private AdmissionDetailDTO detail;

    public GetAdmissionScoreResponse(List<AdmissionWithUniversityInfoDTO> admissions) {
        this.admissions = admissions;
        this.detail = null;
    }

    public GetAdmissionScoreResponse(AdmissionDetailDTO detail) {
        this.admissions = null;
        this.detail = detail;
    }
}
