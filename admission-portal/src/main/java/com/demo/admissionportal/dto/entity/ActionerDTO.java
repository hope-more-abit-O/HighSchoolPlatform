package com.demo.admissionportal.dto.entity;

import com.demo.admissionportal.constants.Role;
import com.demo.admissionportal.entity.UniversityInfo;
import com.demo.admissionportal.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Data Transfer Object (DTO) used for representing an action performer.
 *
 * @Field id The unique identifier (ID) of the user who performed the action.
 * @Field username The username of the action performer.
 * @Field email The email address of the action performer (optional).
 * @Field role The user's role (e.g., "ADMIN", "USER").
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ActionerDTO {
    private Integer id;
    private String fullName;
    private String role;
    private String status;
}
