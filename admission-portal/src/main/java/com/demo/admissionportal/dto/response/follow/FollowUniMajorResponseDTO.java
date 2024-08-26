package com.demo.admissionportal.dto.response.follow;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * The type Follow uni major response dto.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class FollowUniMajorResponseDTO implements Serializable {
    private String currentStatus;
}
