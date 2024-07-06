package com.demo.admissionportal.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * The type Subject group dto.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateSubjectGroupRequestDTO {
    @NotNull(message = "Tên nhóm môn học không được để trống")
    @Size(max = 3, message = "Tên nhóm môn học không được vượt quá 3 ký tự")
    private String name;

    @NotNull(message = "Danh sách môn học không được để trống")
    @Size(min = 3, max = 3, message = "Danh sách môn học phải chứa đúng 3 môn học!")
    private List<Integer> subjectIds;

    public boolean isEmpty() {
        return (name == null && name.isEmpty() || subjectIds == null && subjectIds.isEmpty());
    }
}