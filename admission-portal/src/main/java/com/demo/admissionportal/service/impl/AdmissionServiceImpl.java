package com.demo.admissionportal.service.impl;

import com.demo.admissionportal.dto.request.admisison.CreateAdmissionRequest;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.dto.response.admission.CreateAdmissionResponse;
import com.demo.admissionportal.entity.Major;
import com.demo.admissionportal.entity.Method;
import com.demo.admissionportal.entity.User;
import com.demo.admissionportal.entity.admission.Admission;
import com.demo.admissionportal.entity.admission.AdmissionMethod;
import com.demo.admissionportal.entity.admission.AdmissionTrainingProgram;
import com.demo.admissionportal.exception.StoreDataFailedException;
import com.demo.admissionportal.repository.MethodRepository;
import com.demo.admissionportal.repository.admission.*;
import com.demo.admissionportal.service.AdmissionService;
import com.demo.admissionportal.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdmissionServiceImpl implements AdmissionService {
    private final AdmissionRepository admissionRepository;
    private final AdmissionTrainingProgramMethodRepository admissionTrainingProgramMethodRepository;
    private final AdmisionTrainingProgramRepostory admisionTrainingProgramRepostory;
    private final AdmissionMethodRepository admissionMethodRepository;
    private final AdmissionTrainingProgramSubjectGroupRepository admissionTrainingProgramSubjectGroupRepository;
    private final UserService userService;
    private final MajorServiceImpl majorService;
    private final MethodServiceImpl methodService;

    public Admission save(Admission admission){
        try {
            return admissionRepository.save(admission);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            throw new StoreDataFailedException("Lưu đề án thất bại", error);
        }
    }

    public List<AdmissionMethod> saveAdmissionMethods(List<AdmissionMethod> admissionMethods){
        try {
            return admissionMethodRepository.saveAll(admissionMethods);
        } catch (Exception e){
            throw new StoreDataFailedException("Lưu thông tin phương thức tuyển sinh của đề án thất bại.");
        }
    }

    public List<AdmissionTrainingProgram> saveAdmissionTrainingProgram(List<AdmissionTrainingProgram> admissionTrainingPrograms){
        try {
            return admisionTrainingProgramRepostory.saveAll(admissionTrainingPrograms);
        } catch (Exception e){
            throw new StoreDataFailedException("Lưu thông tin các chương trình đào tạo của đề án thất bại.");
        }
    }

    @Transactional
    public ResponseData<CreateAdmissionResponse> createAdmission(CreateAdmissionRequest request) {
        Integer consultantId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        User consultant = userService.findById(consultantId);

        Admission admission = this.save(new Admission(request.getYear(), request.getDocuments(), consultant.getCreateBy(), consultantId));

        List<Major> majors = majorService.insertNewMajorsAndGetExistedMajors(request.getNewMajors(), request.getMajorIds());

        List<Method> methods = methodService.insertNewMethodsAndGetExistedMethods(request.getNewMethods(), request.getMethodIds());


        return null;
    }


}
