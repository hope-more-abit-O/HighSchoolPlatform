package com.demo.admissionportal.dto.request;

import com.demo.admissionportal.constants.SubjectStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The type Subject group dto.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubjectGroupRequestDTO {
    @NotBlank(message = "Tên nhóm môn học không được để trống")
    @Size(max = 3, message = "Tên nhóm môn học không được vượt quá 3 ký tự")
    private String name;
}