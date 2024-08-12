package com.demo.admissionportal.dto;

import com.demo.admissionportal.constants.HollandCharacteristicType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateQuestionRequest {
    @NotNull(message = "Câu hỏi không được để trống")
    private String content;
    @NotNull(message = "Loại câu hỏi không được để trống")
    private Integer typeId;
    private List<Integer> jobs;
}
