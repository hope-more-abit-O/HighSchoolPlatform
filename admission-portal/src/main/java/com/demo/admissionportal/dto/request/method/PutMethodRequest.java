package com.demo.admissionportal.dto.request.method;

import com.demo.admissionportal.util.enum_validator.EnumMethodCode;
import com.demo.admissionportal.util.enum_validator.EnumName;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PutMethodRequest {
    @NotBlank (message = "Id phương thức không được để trống")
    private Integer methodId;
    @EnumName (message = "Tên phương thức không đúng format")
    private String methodName;
    @EnumMethodCode (message = "Mã phương thức không đúng format")
    private String methodCode;
    private String note;
}
