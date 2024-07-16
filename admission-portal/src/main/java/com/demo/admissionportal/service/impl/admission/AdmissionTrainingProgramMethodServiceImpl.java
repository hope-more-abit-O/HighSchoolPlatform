package com.demo.admissionportal.service.impl.admission;

import com.demo.admissionportal.dto.request.admisison.CreateAdmissionTrainingProgramMethodRequest;
import com.demo.admissionportal.entity.admission.AdmissionTrainingProgramMethod;
import com.demo.admissionportal.entity.admission.sub_entity.AdmissionTrainingProgramMethodId;
import com.demo.admissionportal.repository.admission.AdmissionTrainingProgramMethodRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

}
