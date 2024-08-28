package com.demo.admissionportal.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterIdentificationNumberRequest {
    @NotBlank(message = "Số báo danh không được để trống !")
    private Integer identificationNumber;
}
