package com.demo.admissionportal.dto.response.post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * The type Post detail response dtov 3.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PostDetailResponseDTOV3 implements Serializable {
    private PostPropertiesResponseDTO postProperties;
    private String create_by;
    private String avatar;
    private String role;
}
