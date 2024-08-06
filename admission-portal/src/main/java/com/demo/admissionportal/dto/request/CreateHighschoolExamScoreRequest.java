package com.demo.admissionportal.dto.request;

import com.demo.admissionportal.dto.response.SubjectScoreDTO;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateHighschoolExamScoreRequest {
    @NotBlank(message = "Số báo danh không được để trống")
    private Integer identificationNumber;
    @NotBlank(message = "Địa phương không được để trống")
    private String local;
    @NotBlank(message = "Hội đồng thi không được để trống")
    private String examinationBoard;
    @NotBlank(message = "Ngày tháng năm sinh không được để trống")
    private String dateOfBirth;
    @NotBlank(message = "Thí sinh không được để trống")
    private String examiner;
    @NotBlank(message = "Điểm thi không được để trống")
    private List<SubjectScoreDTO> subjectScores;
}
