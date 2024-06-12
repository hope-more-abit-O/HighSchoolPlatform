package com.demo.admissionportal.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * The type Register student request.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterStudentRequestDTO implements Serializable {
    private String username;
    private String firstname;
    private String middle_name;
    private String lastname;
    private String email;
    private String password;
    private int address;
    private LocalDate birthday;
    private String educationLevel;
    private String avatar;
    private String phone;
    private String gender;
}
