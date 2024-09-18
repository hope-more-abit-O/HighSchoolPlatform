package com.demo.admissionportal.dto.request.follow;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * The type Update follow uni request dto.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UpdateFollowUniRequestDTO implements Serializable {
    private Integer indexOfFollow;
    private Integer universityMajorId;
}
