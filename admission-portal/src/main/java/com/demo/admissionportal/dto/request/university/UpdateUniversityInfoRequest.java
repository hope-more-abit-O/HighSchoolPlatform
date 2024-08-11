package com.demo.admissionportal.dto.request.university;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUniversityInfoRequest {
    private String name;
    private String code;
    private String type;
    private String description;
    private String coverImage;
}
