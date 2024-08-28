package com.demo.admissionportal.dto.request;

import com.demo.admissionportal.util.IntegerListDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotBlank;
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
//    @NotBlank(message = "Tên nhóm môn học không được để trống")
    @NotBlank(message = "Tên tổ hợp môn học không được để trống")
    @Size(max = 3, message = "Tên nhóm môn học không được vượt quá 3 ký tự")
    private String name;

    @NotBlank(message = "Danh sách môn học không được để trống")
    @Size(min = 3, max = 3, message = "Danh sách môn học phải chứa đúng 3 môn học!")
    @JsonDeserialize(using = IntegerListDeserializer.class)
    private List<Integer> subjectIds;
}