package com.demo.admissionportal.dto.entity.admission;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SchoolDirectoryRequest {
    private List<Integer> subjectGroupIds;
    private List<Integer> methodIds;
    private List<String> universityCodes;
    private List<Integer> provinceIds;
}
