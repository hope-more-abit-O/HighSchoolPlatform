package com.demo.admissionportal.dto.request.admisison;

import com.demo.admissionportal.constants.AdmissionConfirmStatus;
import com.demo.admissionportal.constants.AdmissionStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAdmissionConfirmStatusRequest {
    @NotBlank(message = "Ghi chú không được để trống.")
    private String note;
    private AdmissionConfirmStatus confirmStatus;
    private AdmissionStatus admissionStatus;
}
