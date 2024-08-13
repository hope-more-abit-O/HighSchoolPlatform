package com.demo.admissionportal.dto.request.admisison;

import com.demo.admissionportal.util.enum_validator.EnumId;
import com.demo.admissionportal.util.enum_validator.EnumIdList;
import com.demo.admissionportal.util.enum_validator.EnumScore;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateAdmissionQuotaRequest implements Serializable {
    @EnumId(message = "Mã ngành phải là số và phải lớn hơn 0")
    private Integer majorId;

    private String majorName;
    private String majorCode;

    @EnumId(message = "Mã môn học chính phải là số và phải lớn hơn 0")
    private Integer mainSubjectId;
    private String language;
    private String trainingSpecific;

    @EnumId(message = "Mã ngành phải là số và phải lớn hơn 0")
    private Integer methodId;

    private String methodName;
    private String methodCode;

    @EnumIdList
    private List<Integer> subjectGroupIds;

    @EnumScore
    private Integer quota;
}
