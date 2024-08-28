package com.demo.admissionportal.dto.request;

import com.demo.admissionportal.dto.response.SubjectScoreDTO;
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
    private Integer identificationNumber;
    @NotNull(message = "Địa phương không được để trống")
    private String local;
    @NotNull(message = "Hội đồng thi không được để trống")
    private String examinationBoard;
    @NotNull(message = "Ngày tháng năm sinh không được để trống")
    private String dateOfBirth;
    @NotNull(message = "Thí sinh không được để trống")
    private String examiner;
    @NotNull(message = "Điểm thi không được để trống")
    private List<SubjectScoreDTO> subjectScores;
}
