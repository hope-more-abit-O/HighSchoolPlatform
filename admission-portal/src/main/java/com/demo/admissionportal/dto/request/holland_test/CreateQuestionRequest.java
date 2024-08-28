package com.demo.admissionportal.dto.request.holland_test;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateQuestionRequest {
    @NotBlank(message = "Câu hỏi không được để trống")
    private String content;
    @NotBlank(message = "Loại câu hỏi không được để trống")
    private Integer typeId;
    private List<Integer> jobs;

    public void setContent(String content) {
        this.content = content.trim();
    }
}
