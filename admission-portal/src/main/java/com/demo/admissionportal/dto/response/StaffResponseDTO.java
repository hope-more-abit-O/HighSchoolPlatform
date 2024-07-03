package com.demo.admissionportal.dto.response;

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
    private String email;
    private String firstName;
    private String middleName;
    private String lastName;
    private String avatar;
    private String phone;
    private String status;
}
