package com.demo.admissionportal.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateStaffRequestDTO {
    @NotNull
    private String firstName;
    private String middleName;
    @NotNull
    private String lastName;
    private String avatar;
    @NotNull
    private String phone;
    @NotNull
    private Integer provinceId;
}