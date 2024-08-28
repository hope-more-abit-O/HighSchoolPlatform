package com.demo.admissionportal.dto.request.admisison;

import com.demo.admissionportal.constants.ActionStatus;
import com.demo.admissionportal.constants.AdmissionStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAdmissionStatusRequest {
    @NotBlank(message = "Ghi chú không được để trống.")
    private String note;
    @NotBlank(message = "Trạng thái đề án không được để trống.")
    private AdmissionStatus status;
}
