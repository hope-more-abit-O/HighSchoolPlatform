package com.demo.admissionportal.dto.entity.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) used for transferring basic user information responses.
 *
 * @Field id The unique identifier (ID) of the user.
 * @Field email The user's email address.
 * @Field username The user's username.
 * @Field avatar A URL or path to the user's avatar image (optional).
 * @Field role The user's role (e.g., "ADMIN", "USER").
 * @Field status The user's account status (e.g., "ACTIVE", "INACTIVE").
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InfoUserResponseDTO {
    private Integer id;
    private String email;
    private String username;
    private String avatar;
    private String role;
    private String status;
}
