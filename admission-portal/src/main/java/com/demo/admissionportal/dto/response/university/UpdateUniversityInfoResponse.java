package com.demo.admissionportal.dto.response.university;

import com.demo.admissionportal.dto.entity.university.FullUniversityResponseDTO;
import com.demo.admissionportal.dto.entity.user.FullUserResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUniversityInfoResponse {
    private FullUserResponseDTO account;
    private FullUniversityResponseDTO info;
}
