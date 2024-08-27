package com.demo.admissionportal.dto.response.follow;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * The type User follow university major response dto.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserFollowUniversityMajorResponseDTO implements Serializable {
    private Integer universityMajorId;
    private String universityName;
    private String avatar;
    private String majorName;
    private String training_specific;
    private String language;
    private Date createTime;
}