package com.demo.admissionportal.dto.request.method;

import com.demo.admissionportal.util.enum_validator.EnumMethodCode;
import com.demo.admissionportal.util.enum_validator.EnumName;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PostMethodRequest {
    @NotNull
    @EnumName
    private String name;
    @NotNull
    @EnumMethodCode
    private String code;
}
