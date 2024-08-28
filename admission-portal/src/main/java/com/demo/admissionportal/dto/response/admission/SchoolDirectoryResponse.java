package com.demo.admissionportal.dto.response.admission;

import com.demo.admissionportal.dto.entity.admission.SchoolDirectoryInfo;
import com.demo.admissionportal.dto.entity.university.InfoUniversityResponseDTO;
import com.demo.admissionportal.dto.entity.user.InfoUserResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SchoolDirectoryResponse {
    private List<SchoolDirectoryInfo> universities;

}
