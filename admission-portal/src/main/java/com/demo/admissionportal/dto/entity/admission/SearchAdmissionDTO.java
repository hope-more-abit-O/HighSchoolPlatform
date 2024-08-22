package com.demo.admissionportal.dto.entity.admission;

import com.demo.admissionportal.entity.UniversityInfo;
import com.demo.admissionportal.entity.admission.Admission;
import com.demo.admissionportal.exception.exceptions.ResourceNotFoundException;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchAdmissionDTO {
    private Integer universityId;
    private String universityName;
    private String universityCode;
    private Integer admissionId;
    private String admissionName;
    private Integer admissionYear;
    private String admissionStatus;
    private String scoreStatus;

    public SearchAdmissionDTO(Admission admission, List<UniversityInfo> universityInfos) {
        UniversityInfo universityInfo = universityInfos
                .stream()
                .filter(element -> element.getId().equals(admission.getUniversityId()))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy thông tin trường học", Map.of("error", admission.getUniversityId().toString())));
        this.universityId = universityInfo.getId();
        this.universityName = universityInfo.getName();
        this.universityCode = universityInfo.getCode();
        this.admissionId = admission.getId();
        this.admissionName = "ĐỀ ÁN TUYỂN SINH NĂM " + admission.getYear() + " CỦA " + universityInfo.getName().toUpperCase();
        this.admissionYear = admission.getYear();
        this.admissionStatus = admission.getAdmissionStatus().name;
        this.scoreStatus = admission.getScoreStatus().name;
    }
}
