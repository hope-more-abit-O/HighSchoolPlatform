package com.demo.admissionportal.dto.request.admisison;

import com.demo.admissionportal.dto.entity.admission.UpdateAdmissionQuotaDTO;
import com.demo.admissionportal.util.enum_validator.EnumCreateAdmissionQuotaRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.sql.Update;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAdmissionRequestV2 {
    @NotNull
    private Integer year;

    @NotNull
    private String documents;

    @NotNull
    @Valid
    private List<UpdateAdmissionQuotaDTO> quotas;
}
