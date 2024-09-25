package com.demo.admissionportal.dto.request.admisison;

import com.demo.admissionportal.constants.AdmissionStatus;
import com.demo.admissionportal.constants.AdmissionUpdateStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAdmissionUpdateStatusRequest {
    @NotNull(message = "Ghi chú không được để trống.")
    private String note;
    @NotNull(message = "Trạng thái đề án không được để trống.")
    private AdmissionUpdateStatus status;
}
