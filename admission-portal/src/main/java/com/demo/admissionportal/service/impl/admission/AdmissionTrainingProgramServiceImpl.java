package com.demo.admissionportal.service.impl.admission;

import com.demo.admissionportal.dto.entity.admission.CreateTrainingProgramRequest;
import com.demo.admissionportal.dto.entity.admission.TrainingProgramDTO;
import com.demo.admissionportal.dto.request.admisison.CreateAdmissionQuotaRequest;
import com.demo.admissionportal.entity.Major;
import com.demo.admissionportal.entity.admission.AdmissionTrainingProgram;
import com.demo.admissionportal.exception.exceptions.ResourceNotFoundException;
import com.demo.admissionportal.exception.exceptions.StoreDataFailedException;
import com.demo.admissionportal.repository.admission.AdmissionTrainingProgramRepository;
import com.demo.admissionportal.service.SubjectService;
import com.demo.admissionportal.service.impl.MajorServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdmissionTrainingProgramServiceImpl {
    private final AdmissionTrainingProgramRepository admissionTrainingProgramRepository;
    private final SubjectService subjectService;
    private final MajorServiceImpl majorService;


    public AdmissionTrainingProgram save(AdmissionTrainingProgram admissionTrainingProgram){
        log.info("Saving admission training program: {}", admissionTrainingProgram.toString());
        try {
            AdmissionTrainingProgram savedProgram = admissionTrainingProgramRepository.save(admissionTrainingProgram);
            log.info("Admission training program saved successfully: {}", savedProgram.getId());
            return savedProgram;
        } catch (Exception e) {
            log.info("Saving admission training program failed.");
            log.error(e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            throw new StoreDataFailedException("Lưu thông tin đề án - chương trình đào tạo thất bại", error);
        }
    }

    public List<AdmissionTrainingProgram> saveAllAdmissionTrainingProgram(List<AdmissionTrainingProgram> admissionTrainingPrograms) throws StoreDataFailedException{
        log.info("Saving admission training program");
        List<AdmissionTrainingProgram> savedPrograms = new ArrayList<>();
        List<String> errorMessages = new ArrayList<>();
        Map<String, String> error = new HashMap<>();
        for (AdmissionTrainingProgram program : admissionTrainingPrograms) {
            log.info("Saving admission training program: {}", program.getId());
            try {
                AdmissionTrainingProgram savedProgram = admissionTrainingProgramRepository.save(program);
                log.info("Admission training program saved successfully: {}", savedProgram.getId());
                savedPrograms.add(savedProgram);
            } catch (Exception e) {
                log.info("Saving admission training program failed: {}", program.getId());
                log.error(e.getMessage(), e);
                errorMessages.add("ID: " + program.getId() + " - Error: " + e.getMessage());
            }
        }

        if (!errorMessages.isEmpty()) {
            String combinedErrorMessage = String.join("; ", errorMessages);
            error.put("errors", combinedErrorMessage);
            throw new StoreDataFailedException("Lưu thông tin đề án - chương trình đào tạo thất bại: ", error);
        }
        log.info("Saving admission training program success");

        return savedPrograms;
    }

    public List<AdmissionTrainingProgram> saveAllAdmissionTrainingProgram(Integer admissionId, List<CreateTrainingProgramRequest> createTrainingProgramRequests) {
        log.info("Saving admission training program");
        List<AdmissionTrainingProgram> savedPrograms = new ArrayList<>();
        List<String> errorMessages = new ArrayList<>();

        for (CreateTrainingProgramRequest dto : createTrainingProgramRequests) {
            try {
                if (dto.getMainSubjectId() != null)
                    subjectService.findById(dto.getMainSubjectId());
                AdmissionTrainingProgram program = new AdmissionTrainingProgram(admissionId, dto);
                AdmissionTrainingProgram savedProgram = this.save(program);
                log.info("Admission training program saved successfully: {} - {} - {}", savedProgram.getId(), savedProgram.getMajorId(), savedProgram.getTrainingSpecific());
                savedPrograms.add(savedProgram);
            } catch (Exception e) {
                log.info("Saving admission training program failed for DTO: {}", dto);
                log.error(e.getMessage(), e);
                errorMessages.add("DTO: " + dto + " - Error: " + e.getMessage());
            }
        }

        if (!errorMessages.isEmpty()) {
            String combinedErrorMessage = String.join("; ", errorMessages);
            throw new StoreDataFailedException("Lưu thông tin đề án - chương trình đào tạo thất bại: ", Map.of("errors", combinedErrorMessage));
        }

        return savedPrograms;
    }

    public List<AdmissionTrainingProgram> saveAdmissionTrainingProgram(Integer admissionId, List<CreateAdmissionQuotaRequest> quotas, List<Major> majors) throws StoreDataFailedException{
        List<AdmissionTrainingProgram> result;
        List<TrainingProgramDTO> trainingProgramDTOs = quotas.stream().map(TrainingProgramDTO::new).distinct().toList();
        Set<String> seen = new LinkedHashSet<>();

        List<AdmissionTrainingProgram> admissionTrainingPrograms = trainingProgramDTOs.stream()
                .map(quota -> {
                    String trainingProgramString = quota.getMajorId() + "-" +
                            (quota.getMainSubjectId() != null ? quota.getMainSubjectId() : "null") + "-" +
                            (quota.getLanguage() != null ? quota.getLanguage() : "null") + "-" +
                            (quota.getTrainingSpecific() != null ? quota.getTrainingSpecific() : "null");
                    if (seen.add(trainingProgramString)) {
                        return new AdmissionTrainingProgram(admissionId, quota);
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .toList();

        result = this.saveAllAdmissionTrainingProgram(admissionTrainingPrograms);

        return result;
    }


    public List<AdmissionTrainingProgram> findByAdmissionId(Integer id) {
        return admissionTrainingProgramRepository.findByAdmissionId(id);
    }
    public List<AdmissionTrainingProgram> findByAdmissionIds(List<Integer> id) {
        return admissionTrainingProgramRepository.findByAdmissionIdIn(id);
    }
    
    public List<AdmissionTrainingProgram> findByIds(List<Integer> admissionMethodIds)
            throws ResourceNotFoundException{
        List<AdmissionTrainingProgram> methods = admissionTrainingProgramRepository.findAllById(admissionMethodIds);

        // Check for IDs that were not found
        List<Integer> foundIds = methods.stream().map(AdmissionTrainingProgram::getId).toList();
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

    public List<AdmissionTrainingProgram> findByAdmissionIdsAndMajorIds(List<Integer> admissionIds, List<Integer> majorIds) {
        return admissionTrainingProgramRepository.findByAdmissionIdInAndMajorIdIn(admissionIds, majorIds);
    }
}
