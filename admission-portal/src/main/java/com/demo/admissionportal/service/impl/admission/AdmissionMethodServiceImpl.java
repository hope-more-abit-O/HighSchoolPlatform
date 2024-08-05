package com.demo.admissionportal.service.impl.admission;

import com.demo.admissionportal.dto.request.admisison.CreateAdmissionQuotaRequest;
import com.demo.admissionportal.entity.Method;
import com.demo.admissionportal.entity.admission.AdmissionMethod;
import com.demo.admissionportal.entity.admission.AdmissionTrainingProgram;
import com.demo.admissionportal.entity.admission.AdmissionTrainingProgramMethod;
import com.demo.admissionportal.exception.exceptions.CreateEntityFailedException;
import com.demo.admissionportal.exception.exceptions.ResourceNotFoundException;
import com.demo.admissionportal.repository.admission.AdmissionMethodRepository;
import com.demo.admissionportal.service.impl.MethodServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdmissionMethodServiceImpl {
    private final AdmissionMethodRepository admissionMethodRepository;
    private final MethodServiceImpl methodService;

    public List<AdmissionMethod> saveAllAdmissionMethod(Integer admissionId, List<Integer> methodIds) {
        log.info("Saving admission methods for admission ID: {}", admissionId);
        List<AdmissionMethod> admissionMethods = new ArrayList<>();
        List<String> errorMessages = new ArrayList<>();
        Map<String, String> error = new HashMap<>();

        for (Integer methodId : methodIds) {
            try {
                AdmissionMethod admissionMethod = new AdmissionMethod(admissionId, methodId);
                admissionMethods.add(admissionMethod);
                log.info("Created admission method: {}", admissionMethod);
            } catch (Exception e) {
                log.info("Creating admission method failed for method ID: {}", methodId);
                log.error(e.getMessage(), e);
                errorMessages.add("Method ID: " + methodId + " - Error: " + e.getMessage());
            }
        }

        if (!errorMessages.isEmpty()) {
            String combinedErrorMessage = String.join("; ", errorMessages);
            throw new CreateEntityFailedException("Tạo model cho Phương thức tuyển sinh thất bại", Map.of("errors", combinedErrorMessage));
        }

        try {
            List<AdmissionMethod> savedAdmissionMethods = admissionMethodRepository.saveAll(admissionMethods);
            log.info("Admission methods saved successfully for admission ID: {}", admissionId);
            return savedAdmissionMethods;
        } catch (Exception e) {
            log.info("Saving admission methods failed for admission ID: {}", admissionId);
            log.error(e.getMessage(), e);
            errorMessages.add("Saving methods failed for admission ID: " + admissionId + " - Error: " + e.getMessage());
        }
        String combinedErrorMessage = String.join("; ", errorMessages);
        throw new CreateEntityFailedException("Lưu Đề án - Phương thức tuyển sinh thất bại", Map.of("errors", combinedErrorMessage));

    }

    public List<AdmissionMethod> saveAllAdmissionMethodWithListMethods(Integer admissionId, List<Method> methods) {
        log.info("Saving admission methods for admission ID: {}", admissionId);

        List<AdmissionMethod> admissionMethods = new ArrayList<>();
        List<String> errorMessages = new ArrayList<>();
        Map<String, String> error = new HashMap<>();

        for (Method method : methods) {
            try {
                AdmissionMethod admissionMethod = new AdmissionMethod(admissionId, method);
                admissionMethods.add(admissionMethod);
                log.info("Created admission method: {}", admissionMethod);
            } catch (Exception e) {
                log.info("Creating admission method failed for method ID: {}", method);
                log.error(e.getMessage(), e);
                errorMessages.add("Method ID: " + method + " - Error: " + e.getMessage());
            }
        }

        if (!errorMessages.isEmpty()) {
            String combinedErrorMessage = String.join("; ", errorMessages);
            throw new CreateEntityFailedException("Tạo model cho Phương thức tuyển sinh thất bại", Map.of("errors", combinedErrorMessage));
        }

        try {
            List<AdmissionMethod> savedAdmissionMethods = admissionMethodRepository.saveAll(admissionMethods);
            log.info("Admission methods saved successfully for admission ID: {}", admissionId);
            return savedAdmissionMethods;
        } catch (Exception e) {
            log.info("Saving admission methods failed for admission ID: {}", admissionId);
            log.error(e.getMessage(), e);
            errorMessages.add("Saving methods failed for admission ID: " + admissionId + " - Error: " + e.getMessage());
        }
        String combinedErrorMessage = String.join("; ", errorMessages);
        throw new CreateEntityFailedException("Lưu Đề án - Phương thức tuyển sinh thất bại", Map.of("errors", combinedErrorMessage));
    }

    public List<AdmissionMethod> saveAdmissionMethod(Integer admissionId, List<CreateAdmissionQuotaRequest> quotas)
            throws ResourceNotFoundException {
            
        List<Method> methods = methodService.insertNewMethodsAndGetExistedMethods(quotas);

        return saveAllAdmissionMethodWithListMethods(admissionId, methods);
    }

    public List<AdmissionMethod> findByAdmissionId(Integer id) {
        return admissionMethodRepository.findByAdmissionId(id);
    }

    public List<Integer> getAdmissionId(List<Integer> admissionMethodIds){
        return admissionMethodRepository.findAdmissionIdByAdmissionMethodIds(admissionMethodIds);
    }
}