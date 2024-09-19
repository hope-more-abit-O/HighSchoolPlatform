package com.demo.admissionportal.service.impl.admission;

import com.demo.admissionportal.dto.request.admisison.CreateAdmissionQuotaRequest;
import com.demo.admissionportal.dto.request.admisison.ModifyAdmissionMethodRequest;
import com.demo.admissionportal.dto.request.admisison.UpdateAdmissionMethodRequest;
import com.demo.admissionportal.entity.Method;
import com.demo.admissionportal.entity.admission.Admission;
import com.demo.admissionportal.entity.admission.AdmissionMethod;
import com.demo.admissionportal.entity.admission.AdmissionTrainingProgram;
import com.demo.admissionportal.entity.admission.AdmissionTrainingProgramMethod;
import com.demo.admissionportal.exception.exceptions.CreateEntityFailedException;
import com.demo.admissionportal.exception.exceptions.ResourceNotFoundException;
import com.demo.admissionportal.repository.admission.AdmissionMethodRepository;
import com.demo.admissionportal.service.impl.MethodServiceImpl;
import com.demo.admissionportal.util.impl.ServiceUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdmissionMethodServiceImpl {
    private final AdmissionMethodRepository admissionMethodRepository;
    private final MethodServiceImpl methodService;
    private final AdmissionTrainingProgramMethodServiceImpl admissionTrainingProgramMethodServiceImpl;

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

        for (Method method : methods) {
            admissionMethods.add(new AdmissionMethod(admissionId, method));
        }

        try {
            List<AdmissionMethod> savedAdmissionMethods = admissionMethodRepository.saveAll(admissionMethods);
            if (savedAdmissionMethods.size() != admissionMethods.size()) {
                log.error("Mismatch in the number of saved admission methods for admission ID: {}", admissionId);
                throw new CreateEntityFailedException("Mismatch in the number of saved admission methods", Map.of("expected", String.valueOf(admissionMethods.size()), "actual", String.valueOf(savedAdmissionMethods.size())));
            }
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

    public List<AdmissionMethod> findByAdmissionId(Integer id) {
        return admissionMethodRepository.findByAdmissionId(id);
    }

    public List<Integer> getAdmissionId(List<Integer> admissionMethodIds){
        return admissionMethodRepository.findAdmissionIdByAdmissionMethodIds(admissionMethodIds);
    }

    public List<AdmissionMethod> findByAdmissionIds(List<Integer> admissionIds) {
        return admissionMethodRepository.findByAdmissionIdIn(admissionIds);
    }

    public List<AdmissionMethod> findByIds(List<Integer> admissionMethodIds)
            throws ResourceNotFoundException{
        List<AdmissionMethod> methods = admissionMethodRepository.findAllById(admissionMethodIds);

        // Check for IDs that were not found
        List<Integer> foundIds = methods.stream().map(AdmissionMethod::getId).toList();
        List<Integer> notFoundIds = admissionMethodIds.stream().filter(id -> !foundIds.contains(id)).toList();

        Map<String, String> error = new HashMap<>();
        error.put("error", notFoundIds
                .stream()
                .map(Object::toString)
                .collect(Collectors.joining(",")));
        if (!notFoundIds.isEmpty()) {
            throw new ResourceNotFoundException("Không tìm thấy ngành.", error);
        }

        return methods;
    }

    public Integer update(Admission admission, UpdateAdmissionMethodRequest updateAdmissionMethodRequest) {
        List<AdmissionMethod> admissionMethods = this.findByAdmissionId(admission.getId());

        Integer modified = 0;

        if (updateAdmissionMethodRequest.getDeleteAdmissionMethod() != null && updateAdmissionMethodRequest.getDeleteAdmissionMethod().getAdmissionMethodId() != null && !updateAdmissionMethodRequest.getDeleteAdmissionMethod().getAdmissionMethodId().isEmpty()) {
            modified += admissionTrainingProgramMethodServiceImpl.deleteByAdmissionMethodIds(updateAdmissionMethodRequest.getDeleteAdmissionMethod().getAdmissionMethodId());
            modified += this.deleteByAdmissionIds(updateAdmissionMethodRequest.getDeleteAdmissionMethod().getAdmissionMethodId(), admissionMethods);
        }

        if (updateAdmissionMethodRequest.getCreateAdmissionMethods() != null && updateAdmissionMethodRequest.getCreateAdmissionMethods().getMethodIds() != null && !updateAdmissionMethodRequest.getCreateAdmissionMethods().getMethodIds().isEmpty()) {
            modified += this.createAdmissionMethod(admission, admissionMethods, updateAdmissionMethodRequest.getCreateAdmissionMethods().getMethodIds());
        }

        if (updateAdmissionMethodRequest.getModifyAdmissionMethods() != null && !updateAdmissionMethodRequest.getModifyAdmissionMethods().isEmpty()){
            modified += this.modifyAdmissionMethod(admission, admissionMethods, updateAdmissionMethodRequest.getModifyAdmissionMethods());
        }

        return modified;
    }

    private Integer modifyAdmissionMethod(Admission admission, List<AdmissionMethod> admissionMethods, List<ModifyAdmissionMethodRequest> modifyAdmissionMethods) {
        ServiceUtils.checkListIntegerNotInList(admissionMethods.stream().map(AdmissionMethod::getId).toList(), modifyAdmissionMethods.stream().map(ModifyAdmissionMethodRequest::getAdmissionMethodId).toList(), "modifyAdmissionMethodIds", "Mã phương thức tuyển sinh để sửa không tồn tại.");

        List<AdmissionMethod> modifiedAdmissionMethods = new ArrayList<>();

        for (ModifyAdmissionMethodRequest modifyAdmissionMethod : modifyAdmissionMethods) {
            AdmissionMethod admissionMethod = admissionMethods.stream().filter(admissionMethod1 -> admissionMethod1.getId().equals(modifyAdmissionMethod.getAdmissionMethodId())).findFirst().get();
            admissionMethod.setMethodId(modifyAdmissionMethod.getMethodId());
            modifiedAdmissionMethods.add(admissionMethod);
        }

        return admissionMethodRepository.saveAll(modifiedAdmissionMethods).size();
    }

    public Integer deleteByAdmissionIds(List<Integer> ids, List<AdmissionMethod> admissionMethods) {
        ServiceUtils.checkDuplicate(ids, "deleteAdmissionMethodIds", "Mã phương thức tuyển sinh để xoá bị trùng.");
        List<Integer> notfoundIds = new ArrayList<>();

        for (Integer id : ids) {
            boolean found = false;
            for (AdmissionMethod admissionMethod : admissionMethods) {
                if (admissionMethod.getId().equals(id)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                notfoundIds.add(id);
            }
        }

        if (!notfoundIds.isEmpty()) {
            Map<String, String> error = new HashMap<>();
            error.put("deleteAdmissionMethodIds", notfoundIds.stream().map(Object::toString).collect(Collectors.joining(",")));
            throw new ResourceNotFoundException("Không tìm thấy phương thức tuyển sinh.", error);
        }

        return admissionMethodRepository.deleteByIdIn(ids);
    }

    public Integer createAdmissionMethod(Admission admission, List<AdmissionMethod> admissionMethods, List<Integer> createMethodIds) {
        ServiceUtils.checkDuplicate(createMethodIds, "createAdmissionMethodIds", "Mã phương thức tuyển sinh để tạo bị trùng.");
        ServiceUtils.checkListIntegerInList(admissionMethods.stream().map(AdmissionMethod::getId).toList(), createMethodIds, "createAdmissionMethodIds", "Mã phương thức tuyển sinh để tạo đã tồn tại.");
        List<Method> methods = methodService.findByIds(createMethodIds);

        List<AdmissionMethod> newAdmissionMethods = new ArrayList<>();
        createMethodIds.forEach((methodId) -> {
            newAdmissionMethods.add(new AdmissionMethod(admission.getId(), methodId));
        });

        return admissionMethodRepository.saveAll(newAdmissionMethods).size();
    }
}