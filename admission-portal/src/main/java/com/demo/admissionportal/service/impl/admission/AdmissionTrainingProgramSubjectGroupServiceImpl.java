package com.demo.admissionportal.service.impl.admission;

import com.demo.admissionportal.entity.admission.AdmissionTrainingProgramSubjectGroup;
import com.demo.admissionportal.repository.admission.AdmissionTrainingProgramSubjectGroupRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdmissionTrainingProgramSubjectGroupServiceImpl {
    private final AdmissionTrainingProgramSubjectGroupRepository admissionTrainingProgramSubjectGroupRepository;

    public AdmissionTrainingProgramSubjectGroup save(AdmissionTrainingProgramSubjectGroup admissionTrainingProgramSubjectGroup) {
        return admissionTrainingProgramSubjectGroupRepository.save(admissionTrainingProgramSubjectGroup);
    }

    public List<AdmissionTrainingProgramSubjectGroup> saveAll(List<AdmissionTrainingProgramSubjectGroup> admissionTrainingProgramSubjectGroups) {
        return admissionTrainingProgramSubjectGroupRepository.saveAll(admissionTrainingProgramSubjectGroups);
    }
}
