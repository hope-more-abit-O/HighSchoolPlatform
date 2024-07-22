package com.demo.admissionportal.dto.response.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * The type User detail comment response dto.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserDetailCommentResponseDTO implements Serializable {
    private Integer id;
    private String fullName;
    private String avatar;
}
