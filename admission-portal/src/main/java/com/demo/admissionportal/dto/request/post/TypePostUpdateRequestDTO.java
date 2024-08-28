package com.demo.admissionportal.dto.request.post;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Valid
public class TypePostUpdateRequestDTO implements Serializable {
    @NotBlank(message = "Loại bài đăng không được để trống")
    private String name;
    @NotBlank(message = "Người đăng bài không được để trống")
    private Integer create_by;
    @NotBlank(message = "Người cập nhật loại bài đăng không được để trống")
    private Integer update_by;

    public void setName(String name) {
        name = name.trim();
        this.name = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
    }
}
