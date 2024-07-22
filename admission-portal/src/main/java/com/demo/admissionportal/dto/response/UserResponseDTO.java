package com.demo.admissionportal.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * The type User response dto.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO implements Serializable {
    private Integer id;
    private String username;
    private String email;
    private String name;
    private Date create_time;
    private String status;
    private String note;
}
