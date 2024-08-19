package com.demo.admissionportal.dto.request.admisison;

import com.demo.admissionportal.util.enum_validator.EnumId;
import com.demo.admissionportal.util.enum_validator.EnumIdList;
import com.demo.admissionportal.util.enum_validator.EnumQuota;
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
    @NotNull(message = "Mã ngành phải không được để trống")
    @EnumId(message = "Mã ngành phải là số và phải lớn hơn 0")
    private Integer majorId;

    @EnumId(message = "Mã môn học chính phải là số và phải lớn hơn 0")
    private Integer mainSubjectId;
    private String language;
    private String trainingSpecific;

    @NotNull(message = "Mã phương thức phải không được để trống")
    @EnumId(message = "Mã phương thức phải là số và phải lớn hơn 0")
    private Integer methodId;

    @NotNull(message = "Mã nhóm môn học phải không được để trống")
    @EnumIdList(message = "Mã nhóm môn học phải là số lớn hơn 0 và có ít nhất 1 phần tử")
    private List<Integer> subjectGroupIds;

    @NotNull(message = "Số chỉ tiêu phải không được để trống")
    @EnumQuota(message = "Số chỉ tiêu phải là số và phải lớn hơn 0")
    private Integer quota;
}
