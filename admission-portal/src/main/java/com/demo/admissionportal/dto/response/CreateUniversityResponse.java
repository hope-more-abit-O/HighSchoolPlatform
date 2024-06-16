package com.demo.admissionportal.dto.response;

import com.demo.admissionportal.dto.response.entity.UniversityResponseDTO;
import com.demo.admissionportal.dto.response.entity.sub_entity.StaffUniversityResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CreateUniversityResponse {
    private UniversityResponseDTO university;
    private StaffUniversityResponseDTO staffUniversity;
}
