package com.demo.admissionportal.dto.response.university;

import com.demo.admissionportal.dto.entity.university.UniversityResponseDTO;
import com.demo.admissionportal.dto.entity.user.UserResponseDTOV2;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUniversityInfoResponse {
    private UserResponseDTOV2 account;
    private UniversityResponseDTO info;
}
