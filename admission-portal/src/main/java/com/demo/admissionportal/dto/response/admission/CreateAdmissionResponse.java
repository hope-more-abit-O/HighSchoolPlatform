package com.demo.admissionportal.dto.response.admission;

import com.demo.admissionportal.dto.entity.major.InfoMajorDTO;
import com.demo.admissionportal.dto.entity.method.CreateMethodDTO;

import java.util.List;

public class CreateAdmissionResponse {
    private Integer admissionId;
    private Integer year;
    private List<CreateMethodDTO> methods;
    private List<InfoMajorDTO> majors;
}
