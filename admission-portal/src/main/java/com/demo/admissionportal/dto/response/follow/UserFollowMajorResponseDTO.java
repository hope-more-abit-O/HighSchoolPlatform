package com.demo.admissionportal.dto.response.follow;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * The type User follow major response dto.
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UserFollowMajorResponseDTO implements Serializable {
    private Integer majorId;
    private String avatar;
    private Date dateFollow;
}
