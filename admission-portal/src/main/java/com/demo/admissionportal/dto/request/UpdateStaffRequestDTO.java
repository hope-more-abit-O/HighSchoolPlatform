package com.demo.admissionportal.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateStaffRequestDTO {
    @NotBlank
    private String firstName;
    private String middleName;
    @NotBlank
    private String lastName;
    private String avatar;
    @NotBlank
    private String phone;
    @NotBlank
    private Integer provinceId;
}