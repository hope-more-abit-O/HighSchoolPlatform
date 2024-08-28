package com.demo.admissionportal.dto.request.major;

import com.demo.admissionportal.util.enum_validator.EnumMajorCode;
import com.demo.admissionportal.util.enum_validator.EnumName;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateMajorRequest {
    @NotBlank
    private Integer majorId;
    @EnumName
    private String majorName;
    @EnumMajorCode
    private String majorCode;
    private String note;
}
