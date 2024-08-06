package com.demo.admissionportal.dto.response.admission;

import com.demo.admissionportal.dto.entity.major.InfoMajorDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetLatestTrainingProgramResponse {
    private Integer year;
    private List<InfoMajorDTO> majorDTOS;
}
