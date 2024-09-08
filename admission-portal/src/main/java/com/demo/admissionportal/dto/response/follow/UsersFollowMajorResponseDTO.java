package com.demo.admissionportal.dto.response.follow;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * The type Users follow major response dto.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UsersFollowMajorResponseDTO implements Serializable {
    private Integer userId;
    private String avatar;
    private String email;
    private String fullName;
    private String major;
}