package com.demo.admissionportal.dto.response.admission;

import com.demo.admissionportal.dto.entity.admission.SchoolDirectoryDetailDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SchoolDirectoryResponse {
    private Integer year;
    private String source;
    private List<SchoolDirectoryDetailDTO> detail;
}
