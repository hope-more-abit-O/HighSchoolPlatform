package com.demo.admissionportal.dto.request.admisison;

import com.demo.admissionportal.dto.entity.admission.AdmissionTrainingProgramSubjectGroupDTO;
import lombok.Data;

import java.util.List;

@Data
public class CreateAdmissionTrainingProgramSubjectGroup {
    private List<AdmissionTrainingProgramSubjectGroupDTO> data;
}