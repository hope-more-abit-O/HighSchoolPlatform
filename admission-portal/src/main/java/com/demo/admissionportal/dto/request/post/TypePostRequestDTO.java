package com.demo.admissionportal.dto.request.post;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

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
    @Size(min = 2 , max = 20, message = "Loại bài đăng bao gồm từ 2 - 20 ký tự")
    private String name;

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) {
        name = name.trim();
        this.name = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
    }
}
