package com.demo.admissionportal.dto.request.admisison;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateAdmissionRequest {
    @NotNull
    private Integer year;

    @NotNull
    private String documents;

    @NotNull
    private List<CreateAdmissionQuotaRequest> quotas;
}
