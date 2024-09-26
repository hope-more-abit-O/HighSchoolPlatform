package com.demo.admissionportal.dto.request;

import com.demo.admissionportal.dto.response.SubjectScoreDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateHighschoolExamScoreRequest {
    @NotNull(message = "Số báo danh không được để trống")
    private String identificationNumber;
    @NotNull(message = "Địa phương không được để trống")
    private Integer localId;
    @NotNull(message = "Năm không được để trống")
    private Integer year;
    @NotNull(message = "Hội đồng thi không được để trống")
    private List<SubjectScoreDTO> subjectScores;
}
