package com.demo.admissionportal.dto.response.admission;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetAdmissionDetailResponse {
    private AdmissionDetailDTO detail;
}
