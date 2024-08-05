package com.demo.admissionportal.dto.request.consultant;

import com.demo.admissionportal.constants.AccountStatus;
import com.demo.admissionportal.constants.Gender;
import com.demo.admissionportal.util.enum_validator.EnumId;
import com.demo.admissionportal.util.enum_validator.EnumValue;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PatchConsultantStatusRequest {
    @NotNull(message = "Id không được để trống.")
    @EnumId(message = "Id phải lớn hơn 0.")
    private Integer consultantId;
    @NotNull(message = "Ghi chú không được để trống.")
    private String note;
    @NotNull(message = "Trạng thái tài khoản không được để trống.")
    @EnumValue(name = "status", enumClass = AccountStatus.class, message = "Trạng thái tài khoản phải đúng format")
    private String status;
}
