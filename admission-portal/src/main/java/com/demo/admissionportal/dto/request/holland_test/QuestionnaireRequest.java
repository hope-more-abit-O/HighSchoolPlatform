package com.demo.admissionportal.dto.request.holland_test;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * The type Questionnaire request.
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class QuestionnaireRequest implements Serializable {
    @NotBlank(message = "Tên bộ câu hỏi không được trống")
    private String name;
    @NotBlank(message = "Mô tả bộ câu hỏi không được trống")
    private String description;
    @NotBlank(message = "Background ảnh câu hỏi không được trống")
    private String coverImage;
    @Size(min = 60 , max = 60, message = "Số câu hỏi bắt buộc là 60 câu")
    private List<QuestionCreateRequestDTO> questions;
}
