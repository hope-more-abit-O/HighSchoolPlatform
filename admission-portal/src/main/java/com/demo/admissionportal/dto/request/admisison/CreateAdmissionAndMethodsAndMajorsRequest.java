package com.demo.admissionportal.dto.request.admisison;

import com.demo.admissionportal.dto.entity.major.CreateMajorDTO;
import com.demo.admissionportal.dto.entity.method.CreateMethodDTO;
import lombok.Data;

import java.util.List;

@Data

public class CreateAdmissionAndMethodsAndMajorsRequest {
    private Integer year;
    private String documents;
    private List<Integer> methodIds;
    private List<CreateMethodDTO> newMethodsNames;
    private List<Integer> majorIds;
    private List<CreateMajorDTO> newMajorsNames;
}
