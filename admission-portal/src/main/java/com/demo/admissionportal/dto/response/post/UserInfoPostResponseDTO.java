package com.demo.admissionportal.dto.response.post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * The type User info post response dto.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserInfoPostResponseDTO implements Serializable {
    private Integer id;
    private String fullName;
}
