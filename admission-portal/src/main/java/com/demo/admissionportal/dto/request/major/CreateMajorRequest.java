package com.demo.admissionportal.dto.request.major;

import com.demo.admissionportal.util.enum_validator.EnumMajorCode;
import com.demo.admissionportal.util.enum_validator.EnumName;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CreateMajorRequest {
    @NotNull
    @EnumName
    private String majorName;
    @NotNull
    @EnumMajorCode
    private String majorCode;
    private String note;
}
