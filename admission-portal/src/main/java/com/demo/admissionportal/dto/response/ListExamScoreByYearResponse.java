package com.demo.admissionportal.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListExamScoreByYearResponse {
    private Integer id;
    private String title;
    private Integer year;
    private String status;
}
