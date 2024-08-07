package com.demo.admissionportal.dto.response.admission;

import com.demo.admissionportal.dto.entity.admission.school_advice.SchoolAdviceDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SchoolAdviceResponse {
    private List<SchoolAdviceDTO> advice;
}
