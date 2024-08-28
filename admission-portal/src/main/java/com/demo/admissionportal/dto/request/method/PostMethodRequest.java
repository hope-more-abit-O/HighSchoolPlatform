package com.demo.admissionportal.dto.request.method;

import com.demo.admissionportal.util.enum_validator.EnumMethodCode;
import com.demo.admissionportal.util.enum_validator.EnumName;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PostMethodRequest {
    @NotBlank (message = "Tên phương thức không được để trống")
    @EnumName (message = "Tên phương thức phải là chứ cái")
    private String name;
    @NotBlank (message = "Mã phương thức không được để trống")
    @EnumMethodCode (message = "Mã phương thức phải là số, có từ 3 - 9 số")
    private String code;
}
