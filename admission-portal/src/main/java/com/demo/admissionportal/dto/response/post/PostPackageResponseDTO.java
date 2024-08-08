package com.demo.admissionportal.dto.response.post;

import com.demo.admissionportal.dto.entity.post.InfoPostResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * The type Post package response dto.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PostPackageResponseDTO implements Serializable {
    private PostPropertiesResponseDTO postProperties;
    private InfoPostResponseDTO info;
}
