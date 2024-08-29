package com.demo.admissionportal.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterIdentificationNumberRequest {
    @NotNull(message = "Số báo danh không được để trống !")
    private Integer identificationNumber;
}
