package com.demo.admissionportal.dto.request.admisison;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModifyAdmissionMethodRequest {
    private Integer admissionMethodId;
    private Integer methodId;
}
