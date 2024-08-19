package com.demo.admissionportal.dto.response.university_major;

import com.demo.admissionportal.dto.entity.major.InfoMajorDTO;
import com.demo.admissionportal.entity.UniversityMajor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetUniversityMajorResponse {
    private List<InfoMajorDTO> majors;
}
