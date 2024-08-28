package com.demo.admissionportal.dto.request.post;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * The type Post delete request dto.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostDeleteRequestDTO implements Serializable {
    @NotBlank(message = "postId không được trống")
    private Integer postId;
    private String note;
}
