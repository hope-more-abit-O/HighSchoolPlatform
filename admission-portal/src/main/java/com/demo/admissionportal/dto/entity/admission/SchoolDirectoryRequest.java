package com.demo.admissionportal.dto.entity.admission;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SchoolDirectoryRequest {
    private Integer pageNumber;
    private Integer pageSize;
    private List<Integer> subjectGroupIds;
    private List<Integer> methodIds;
    private List<Integer> majorIds;
    private List<String> universityIds;
    private List<Integer> provinceIds;
}
