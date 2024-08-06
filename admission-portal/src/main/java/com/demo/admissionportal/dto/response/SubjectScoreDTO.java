package com.demo.admissionportal.dto.response;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubjectScoreDTO {
    @NotBlank(message = "Số báo danh không được để trống")
    private Integer subjectId;
    private String subjectName;
    @NotBlank(message = "Số báo danh không được để trống")
    @Min(value = 0, message = "Điểm phải lớn hoặc bằng 0 và bé hơn hoặc bằng 10")
    @Max(value = 10, message = "Điểm phải lớn hoặc bằng 0 và bé hơn hoặc bằng 10")
    private Float score;
}
