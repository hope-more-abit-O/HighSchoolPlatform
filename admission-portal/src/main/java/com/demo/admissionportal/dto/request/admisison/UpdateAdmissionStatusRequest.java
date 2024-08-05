package com.demo.admissionportal.dto.request.admisison;

import com.demo.admissionportal.constants.ActionStatus;
import com.demo.admissionportal.constants.AdmissionStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UpdateAdmissionStatusRequest {
    private Integer admissionId;
    private String note;
    private AdmissionStatus status;
}
