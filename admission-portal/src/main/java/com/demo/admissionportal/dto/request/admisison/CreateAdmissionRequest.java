package com.demo.admissionportal.dto.request.admisison;

import com.demo.admissionportal.util.enum_validator.EnumCreateAdmissionQuotaRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateAdmissionRequest implements Serializable {
    @NotBlank
    private Integer year;

    @NotBlank
    private String documents;

    @NotBlank
    @EnumCreateAdmissionQuotaRequest
    @Valid
    private List<CreateAdmissionQuotaRequest> quotas;
}
