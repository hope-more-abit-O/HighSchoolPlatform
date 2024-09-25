package com.demo.admissionportal.dto.response.favorite;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * The type Favorite users list response dto.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class FavoriteUsersListResponseDTO implements Serializable {
    private Integer userId;
    private String email;
    private String fullName;
    private String avatar;
    private String fcmToken;
}
