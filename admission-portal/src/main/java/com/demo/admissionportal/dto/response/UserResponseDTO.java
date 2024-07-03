package com.demo.admissionportal.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * The type User response dto.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO implements Serializable {
    private String username;
    private String email;
    private String name;
    private String create_time;
    private String status;
    private String note;
}
