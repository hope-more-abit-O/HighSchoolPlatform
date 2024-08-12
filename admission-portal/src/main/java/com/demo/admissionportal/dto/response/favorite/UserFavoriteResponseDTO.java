package com.demo.admissionportal.dto.response.favorite;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * The type User favorite response dto.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserFavoriteResponseDTO implements Serializable {
    private Integer universityId;
    private String universityName;
    private String avatar;
    private Date dateFavorite;
}
