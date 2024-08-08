package com.demo.admissionportal.service.impl.admission;

import com.demo.admissionportal.dto.request.admisison.CreateAdmissionTrainingProgramMethodRequest;
import com.demo.admissionportal.dto.request.admisison.UpdateAdmissionScoreRequest;
import com.demo.admissionportal.entity.admission.AdmissionMethod;
import com.demo.admissionportal.entity.admission.AdmissionTrainingProgramMethod;
import com.demo.admissionportal.entity.admission.sub_entity.AdmissionTrainingProgramMethodId;
import com.demo.admissionportal.exception.exceptions.ResourceNotFoundException;
import com.demo.admissionportal.repository.admission.AdmissionTrainingProgramMethodRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdmissionTrainingProgramMethodServiceImpl {
    private final AdmissionTrainingProgramMethodRepository admissionTrainingProgramMethodRepository;


    public List<AdmissionTrainingProgramMethod> saveAll(List<AdmissionTrainingProgramMethod> admissionTrainingProgramMethods) {
        return admissionTrainingProgramMethodRepository.saveAll(admissionTrainingProgramMethods);
    }

    public List<AdmissionTrainingProgramMethod> createAdmissionTrainingProgramMethod(CreateAdmissionTrainingProgramMethodRequest request) {
        //TODO: CHECK EXIST by admissionTrainingProgramId and admissionMethodId

        List<AdmissionTrainingProgramMethod> admissionTrainingProgramMethods = request.getQuotas().stream()
                .map(quota -> new AdmissionTrainingProgramMethod(quota.getAdmissionTrainingProgramId(), quota.getAdmissionMethodId(), quota.getQuota()))
                .toList();

        return saveAll(admissionTrainingProgramMethods);
    }

    public List<AdmissionTrainingProgramMethod> findByAdmissionTrainingProgramIds(List<Integer> admissionTrainingProgramIds) {
        return admissionTrainingProgramMethodRepository.findById_AdmissionTrainingProgramIdIn(admissionTrainingProgramIds);
    }

    public List<AdmissionTrainingProgramMethod> updateAdmissionScore(UpdateAdmissionScoreRequest request){
        return admissionTrainingProgramMethodRepository.saveAll(request.getAdmissionScores().stream().map(AdmissionTrainingProgramMethod::new).toList());
    }

    public List<AdmissionTrainingProgramMethod> findByAdmissionId(List<AdmissionTrainingProgramMethodId> admissionTrainingProgramMethodIds, boolean needAll) {
        List<AdmissionTrainingProgramMethod> admissionTrainingProgramMethods = admissionTrainingProgramMethodRepository.findAllById(admissionTrainingProgramMethodIds);

        if (!needAll)
            return admissionTrainingProgramMethods;

        if (admissionTrainingProgramMethods.size() < admissionTrainingProgramMethodIds.size()){
            //TODO: THROW EXCEPTION
            throw new ResourceNotFoundException("");
        }

        return admissionTrainingProgramMethods;
    }

    public List<AdmissionTrainingProgramMethod> findByAdmissionId(Integer id) {
        return admissionTrainingProgramMethodRepository.findByAdmissionId(id);
    }

    public List<AdmissionTrainingProgramMethod> findByMethodIdAndAdmissionTrainingProgramIds(Integer methodId, List<Integer> admissionTrainingProgramIds) {
        return admissionTrainingProgramMethodRepository.findByMethodIdAndAdmissionTrainingProgramIdIn(methodId, admissionTrainingProgramIds);
    }

    public List<AdmissionTrainingProgramMethod> findBySubjectGroupIdAndScoreWithOffset(Integer subjectGroupId, Float score, Float offset, String majorId, Integer provinceId) {
        return admissionTrainingProgramMethodRepository.findBySubjectGroupIdAndScoreWithOffset(subjectGroupId, score, offset, majorId + "%", provinceId, Calendar.getInstance().get(Calendar.YEAR));
    }
}
