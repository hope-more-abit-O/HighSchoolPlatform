package com.demo.admissionportal.dto.response.post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * The type Post detail response dto.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PostDetailResponseDTO implements Serializable {
    private PostPropertiesResponseDTO postProperties;
    private List<TypeResponseDTO> listType;
    private List<TagResponseDTO> listTag;
    private UserInfoPostResponseDTO create_by;
}