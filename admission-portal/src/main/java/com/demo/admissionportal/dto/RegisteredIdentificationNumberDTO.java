package com.demo.admissionportal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisteredIdentificationNumberDTO {
    private String identificationNumber;
    private Integer year;
    private String status;
}