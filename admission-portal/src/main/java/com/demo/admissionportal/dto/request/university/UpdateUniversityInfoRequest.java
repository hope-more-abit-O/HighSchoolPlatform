package com.demo.admissionportal.dto.request.university;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUniversityInfoRequest {
    @NotNull(message = "Id trường không được để trống")
    private Integer id;

    @NotNull(message = "Tên trường không được để trống")
    private String name;

    @NotNull(message = "Mã trường không được để trống")
    private String code;

    @NotNull(message = "Loại trường không được để trống")
    private String type;

    @NotNull(message = "Mô tả không được để trống")
    private String description;

    private String coverImage;
}
