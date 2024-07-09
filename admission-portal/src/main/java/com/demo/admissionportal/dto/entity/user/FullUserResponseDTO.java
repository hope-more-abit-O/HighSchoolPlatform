package com.demo.admissionportal.dto.entity.user;

import com.demo.admissionportal.dto.entity.ActionerDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) used for transferring detailed user information responses.
 *
 * @Field id The unique identifier (ID) of the user.
 * @Field email The user's email address.
 * @Field username The user's username.
 * @Field avatar A URL or path to the user's avatar image (optional).
 * @Field note An optional note associated with the user.
 * @Field role The user's role (e.g., "ADMIN", "USER").
 * @Field createTime The timestamp of the user's account creation.
 * @Field createBy An {@link ActionerDTO} object representing the user who created the account.
 * @Field updateTime The timestamp of the user's most recent account update.
 * @Field updateBy An {@link ActionerDTO} object representing the user who last updated the account.
 * @Field status The user's account status (e.g., "ACTIVE", "INACTIVE").
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FullUserResponseDTO {
    private Integer id;
    private String email;
    private String username;
    private String avatar;
    private String note;
    private String role;
    private String createTime;
    private ActionerDTO createBy;
    private String updateTime;
    private ActionerDTO updateBy;
    private String status;
}
