package com.demo.admissionportal.dto.request.post;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


/**
 * The type Tag request dto.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Valid
public class TagRequestDTO implements Serializable {
    @NotBlank(message = "Tag bài đăng không được để trống")
    private String name;
    private Integer create_by;
}
