package com.demo.admissionportal.dto.request.post;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * The type Type post request dto.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Valid
public class TypePostRequestDTO implements Serializable {
    @NotNull(message = "Loại bài đăng không được để trống")
    private String name;
    private Integer create_by;
}
