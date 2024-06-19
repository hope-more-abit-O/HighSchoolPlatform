package com.demo.admissionportal.dto.response.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class StaffResponseDTO {
    private String username;
    private String name;
    private String email;
    private String avatar;
    private String phone;
    private String status;
}
