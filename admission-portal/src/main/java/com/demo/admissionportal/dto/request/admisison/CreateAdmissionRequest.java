package com.demo.admissionportal.dto.request.admisison;

import com.demo.admissionportal.util.enum_validator.EnumCreateAdmissionQuotaRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateAdmissionRequest implements Serializable {
    @NotNull
    private Integer year;

    @NotNull
    private String documents;

    @NotNull
    @EnumCreateAdmissionQuotaRequest
    @Valid
    private List<CreateAdmissionQuotaRequest> quotas;
}
