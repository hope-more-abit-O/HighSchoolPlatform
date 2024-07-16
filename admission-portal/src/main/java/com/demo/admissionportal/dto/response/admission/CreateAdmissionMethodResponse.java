package com.demo.admissionportal.dto.response.admission;

import com.demo.admissionportal.constants.AdmissionStatus;
import com.demo.admissionportal.dto.entity.admission.FullAdmissionDTO;
import com.demo.admissionportal.dto.entity.major.InfoMajorDTO;
import com.demo.admissionportal.dto.entity.method.InfoMethodDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateAdmissionMethodResponse {
    private FullAdmissionDTO admission;
    private List<InfoMethodDTO> methods;
}
