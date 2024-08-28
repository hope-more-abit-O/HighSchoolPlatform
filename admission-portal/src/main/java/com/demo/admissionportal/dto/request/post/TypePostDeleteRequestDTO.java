package com.demo.admissionportal.dto.request.post;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * The type Type post delete request dto.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TypePostDeleteRequestDTO implements Serializable {
    @NotBlank(message = "PostId không được để trống")
    private Integer postId;
}
