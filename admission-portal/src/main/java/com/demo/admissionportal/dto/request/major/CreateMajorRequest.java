package com.demo.admissionportal.dto.request.major;

import com.demo.admissionportal.util.enum_validator.EnumMajorCode;
import com.demo.admissionportal.util.enum_validator.EnumName;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CreateMajorRequest {
    @NotBlank
    @EnumName
    private String majorName;
    @NotBlank
    @EnumMajorCode
    private String majorCode;
    private String note;
}
