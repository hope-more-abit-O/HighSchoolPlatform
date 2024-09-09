package com.demo.admissionportal.service.impl.admission;

import com.demo.admissionportal.dto.request.admisison.UpdateAdmissionSubjectGroupRequest;
import com.demo.admissionportal.entity.admission.Admission;
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

    public List<AdmissionTrainingProgramSubjectGroup> findByAdmissionTrainingProgramId(List<Integer> admissionTrainingProgramIds) {
        return admissionTrainingProgramSubjectGroupRepository.findById_AdmissionTrainingProgramIdIn(admissionTrainingProgramIds);
    }

    public List<AdmissionTrainingProgramSubjectGroup> findByAdmissionTrainingProgramIdsAndSubjectGroupIds(List<Integer> admissionTrainingProgramIds, List<Integer> subjectGroupIds) {
        return admissionTrainingProgramSubjectGroupRepository.findById_AdmissionTrainingProgramIdInAndId_SubjectGroupIdIn(admissionTrainingProgramIds, subjectGroupIds);
    }

    public Integer update(Admission admission, UpdateAdmissionSubjectGroupRequest updateAdmissionSubjectGroupRequest) {
        return null;
    }
}
