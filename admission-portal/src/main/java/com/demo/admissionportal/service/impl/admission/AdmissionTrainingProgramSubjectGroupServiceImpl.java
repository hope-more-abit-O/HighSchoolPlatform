package com.demo.admissionportal.service.impl.admission;

import com.demo.admissionportal.dto.request.admisison.UpdateAdmissionSubjectGroupRequest;
import com.demo.admissionportal.entity.admission.Admission;
import com.demo.admissionportal.entity.admission.AdmissionTrainingProgramSubjectGroup;
import com.demo.admissionportal.entity.admission.sub_entity.AdmissionTrainingProgramSubjectGroupId;
import com.demo.admissionportal.exception.exceptions.BadRequestException;
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

    public Integer update(Admission admission, UpdateAdmissionSubjectGroupRequest request) {
        List<AdmissionTrainingProgramSubjectGroup> admissionTrainingProgramSubjectGroups = findByAdmissionId(admission.getId());
        List<AdmissionTrainingProgramSubjectGroupId> allAdmissionTrainingProgramSubjectGroupIds = admissionTrainingProgramSubjectGroups
                .stream()
                .map(AdmissionTrainingProgramSubjectGroup::getId)
                .toList();
        int modified = 0;

        if (request.getDeleteAdmissionDetailRequests() != null && (request.getDeleteAdmissionDetailRequests().getAdmissionTrainingProgramSubjectGroups() != null) && !request.getDeleteAdmissionDetailRequests().getAdmissionTrainingProgramSubjectGroups().isEmpty()) {
            List<AdmissionTrainingProgramSubjectGroupId> admissionTrainingProgramSubjectGroupIds = request.getDeleteAdmissionDetailRequests()
                    .getAdmissionTrainingProgramSubjectGroups()
                    .stream()
                    .map(AdmissionTrainingProgramSubjectGroupId::new)
                    .toList();

            validateDelete(admissionTrainingProgramSubjectGroupIds, admissionTrainingProgramSubjectGroups);

            modified += deleteAdmissionSubjectGroup(admissionTrainingProgramSubjectGroupIds);
        }

        if (request.getCreateAdmissionDetailRequests() != null && request.getCreateAdmissionDetailRequests().getAdmissionTrainingProgramSubjectGroups() != null && !request.getCreateAdmissionDetailRequests().getAdmissionTrainingProgramSubjectGroups().isEmpty()) {
            List<AdmissionTrainingProgramSubjectGroup> admissionTrainingProgramSubjectGroupsToCreates = request.getCreateAdmissionDetailRequests()
                    .getAdmissionTrainingProgramSubjectGroups()
                    .stream()
                    .map(AdmissionTrainingProgramSubjectGroup::new)
                    .toList();

            List<AdmissionTrainingProgramSubjectGroup> existed = admissionTrainingProgramSubjectGroupsToCreates
                    .stream()
                    .filter(admissionTrainingProgramSubjectGroupsToCreate -> allAdmissionTrainingProgramSubjectGroupIds.contains(admissionTrainingProgramSubjectGroupsToCreate.getId()))
                    .toList();
            if (!existed.isEmpty()) {
                throw new BadRequestException("Some of the admission training program subject groups are already existed");
            }

            modified += saveAll(admissionTrainingProgramSubjectGroupsToCreates).size();
        }

        return modified;
    }

    private void validateDelete(List<AdmissionTrainingProgramSubjectGroupId> admissionTrainingProgramSubjectGroupIds, List<AdmissionTrainingProgramSubjectGroup> admissionTrainingProgramSubjectGroups) {

        List<AdmissionTrainingProgramSubjectGroup> admissionTrainingProgramSubjectGroupsToDelete = admissionTrainingProgramSubjectGroups
                .stream()
                .filter(admissionTrainingProgramSubjectGroup -> admissionTrainingProgramSubjectGroupIds.contains(admissionTrainingProgramSubjectGroup.getId()))
                .toList();

        if (admissionTrainingProgramSubjectGroupsToDelete.size() != admissionTrainingProgramSubjectGroupIds.size()) {
            throw new BadRequestException("Some of the admission training program subject groups are not found");
        }
    }

    private Integer deleteAdmissionSubjectGroup(List<AdmissionTrainingProgramSubjectGroupId> admissionTrainingProgramSubjectGroupIds) {
        return admissionTrainingProgramSubjectGroupRepository.deleteByIdIn(admissionTrainingProgramSubjectGroupIds);
    }

    private List<AdmissionTrainingProgramSubjectGroup> findByAdmissionId(Integer id) {
        return admissionTrainingProgramSubjectGroupRepository.findByAdmissionId(id);
    }

    public Integer deleteByAdmissionTrainingProgramIds(List<Integer> admissionTrainingProgramId) {
        return admissionTrainingProgramSubjectGroupRepository.deleteByAdmissionTrainingProgramIdIn(admissionTrainingProgramId);
    }
}
