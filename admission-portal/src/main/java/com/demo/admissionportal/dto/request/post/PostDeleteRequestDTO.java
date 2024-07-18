package com.demo.admissionportal.dto.request.post;

import jakarta.validation.constraints.NotNull;
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
    @NotNull(message = "postId không được trống")
    private Integer postId;
}
