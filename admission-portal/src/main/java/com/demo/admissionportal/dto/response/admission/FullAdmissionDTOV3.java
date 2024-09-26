package com.demo.admissionportal.dto.response.admission;

import com.demo.admissionportal.dto.request.admisison.CreateAdmissionQuotaRequest;
import com.demo.admissionportal.entity.admission.Admission;
import com.demo.admissionportal.util.enum_validator.EnumCreateAdmissionQuotaRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FullAdmissionDTOV3 {
    private Integer year;

    private String documents;
    private List<CreateAdmissionQuotaRequest> quotas;

    public FullAdmissionDTOV3(Admission admission) {
        this.documents = admission.getSource();
        this.year = admission.getYear();
    }
}
