package com.demo.admissionportal.service.impl.admission;

import com.demo.admissionportal.dto.entity.admission.CreateTrainingProgramRequest;
import com.demo.admissionportal.dto.entity.admission.SchoolDirectoryRequest;
import com.demo.admissionportal.dto.entity.admission.TrainingProgramDTO;
import com.demo.admissionportal.dto.request.admisison.CompareMajorsFromUniversitiesRequest;
import com.demo.admissionportal.dto.request.admisison.CreateAdmissionQuotaRequest;
import com.demo.admissionportal.dto.request.admisison.ModifyAdmissionTrainingProgramRequest;
import com.demo.admissionportal.dto.request.admisison.UpdateAdmissionTrainingProgramRequest;
import com.demo.admissionportal.entity.Major;
import com.demo.admissionportal.entity.admission.Admission;
import com.demo.admissionportal.entity.admission.AdmissionTrainingProgram;
import com.demo.admissionportal.exception.exceptions.BadRequestException;
import com.demo.admissionportal.exception.exceptions.ResourceNotFoundException;
import com.demo.admissionportal.exception.exceptions.StoreDataFailedException;
import com.demo.admissionportal.repository.admission.AdmissionTrainingProgramRepository;
import com.demo.admissionportal.service.SubjectService;
import com.demo.admissionportal.util.impl.ServiceUtils;
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
    private final AdmissionTrainingProgramMethodServiceImpl admissionTrainingProgramMethodService;
    private final AdmissionTrainingProgramSubjectGroupServiceImpl admissionTrainingProgramSubjectGroupService;

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

    public Integer update(Admission admission, UpdateAdmissionTrainingProgramRequest request) {
        List<AdmissionTrainingProgram> admissionTrainingPrograms = this.findByAdmissionId(admission.getId());

        Integer modified = 0;

        if (request.getDeleteAdmissionTrainingPrograms() != null && request.getDeleteAdmissionTrainingPrograms().getAdmissionTrainingProgramId() != null && !request.getDeleteAdmissionTrainingPrograms().getAdmissionTrainingProgramId().isEmpty()){
            modified += deleteByIds(request.getDeleteAdmissionTrainingPrograms().getAdmissionTrainingProgramId(), admissionTrainingPrograms);
            modified += admissionTrainingProgramMethodService.deleteByAdmissionTrainingProgramIds(request.getDeleteAdmissionTrainingPrograms().getAdmissionTrainingProgramId());
        }

        admissionTrainingPrograms = this.findByAdmissionId(admission.getId());

        if (request.getCreateAdmissionTrainingProgramRequests() != null && !request.getCreateAdmissionTrainingProgramRequests().isEmpty()){
            modified += createAdmissionTrainingProgram(admission.getId(), request.getCreateAdmissionTrainingProgramRequests(), admissionTrainingPrograms);
        }

        admissionTrainingPrograms = this.findByAdmissionId(admission.getId());

        if (request.getModifyAdmissionTrainingPrograms() != null && !request.getModifyAdmissionTrainingPrograms().isEmpty()){
            modified += modifyAdmissionTrainingProgram(admissionTrainingPrograms, request.getModifyAdmissionTrainingPrograms());
        }

        return modified;
    }

    public Integer modifyAdmissionTrainingProgram(List<AdmissionTrainingProgram> admissionTrainingPrograms, List<ModifyAdmissionTrainingProgramRequest> modifyAdmissionTrainingPrograms) throws BadRequestException {

        ServiceUtils.checkDuplicate(modifyAdmissionTrainingPrograms.stream().map(ModifyAdmissionTrainingProgramRequest::getAdmissionTrainingProgramId).toList(), "modifyAdmissionTrainingProgramId", "Có id chương trình đào tạo để chỉnh sửa bị trùng.");

        List<AdmissionTrainingProgram> modifiedPrograms = new ArrayList<>();

        for (ModifyAdmissionTrainingProgramRequest request : modifyAdmissionTrainingPrograms) {
            for (AdmissionTrainingProgram admissionTrainingProgram: admissionTrainingPrograms) {
                if (request.idIsEqual(admissionTrainingProgram) && request.isModified(admissionTrainingProgram)){
                    modifiedPrograms.add(admissionTrainingProgram);
                    break;
                }
            }
        }

        if (modifiedPrograms.isEmpty())
            return 0;
        try {
            List<AdmissionTrainingProgram> savedPrograms = this.saveAllAdmissionTrainingProgram(modifiedPrograms);
            return savedPrograms.size();
        } catch (Exception e){
            throw new StoreDataFailedException("Lưu thông tin đề án - chương trình đào tạo thất bại", Map.of("error", e.getMessage()));
        }
    }

    public Integer deleteByIds(List<Integer> ids, List<AdmissionTrainingProgram> admissionTrainingPrograms) {

        ServiceUtils.checkDuplicate(ids, "deleteAdmissionTrainingProgramIds", "Có id chương trình đào tạo để xoá bị trùng.");

        ServiceUtils.checkListIntegerNotInList(admissionTrainingPrograms.stream().map(AdmissionTrainingProgram::getId).toList(), ids, "deleteAdmissionTrainingProgramIds", "Không tìm thấy chương trình đào tạo.");

        return admissionTrainingProgramRepository.deleteByIdIn(ids);
    }

    public Integer createAdmissionTrainingProgram(Integer admissionId, List<CreateTrainingProgramRequest> createTrainingProgramRequests, List<AdmissionTrainingProgram> admissionTrainingPrograms) {
        Map<String, String> error = new HashMap<>();
        List<CreateTrainingProgramRequest> duplicateRequests = new ArrayList<>();

        if (createTrainingProgramRequests.isEmpty()) {
            List<AdmissionTrainingProgram> savedPrograms = this.saveAllAdmissionTrainingProgram(admissionId, createTrainingProgramRequests);
            return savedPrograms.size();
        }

        for (AdmissionTrainingProgram admissionTrainingProgram : admissionTrainingPrograms) {
            for (CreateTrainingProgramRequest createTrainingProgramRequest : createTrainingProgramRequests) {
                if (createTrainingProgramRequest.isEqual(admissionTrainingProgram)) {
                    duplicateRequests.add(createTrainingProgramRequest);
                }
            }
        }

        String duplicatePrograms = duplicateRequests.stream().map(CreateTrainingProgramRequest::toString).collect(Collectors.joining(", "));

        if (!duplicateRequests.isEmpty()) {
            error.put("error", "Một hoặc nhiều chương trình đào tạo đã tồn tai.");
            error.put("duplicatePrograms", duplicatePrograms);
            throw new BadRequestException("Chương trình đào tạo bị trùng.",error);
        }
        List<AdmissionTrainingProgram> savedPrograms = this.saveAllAdmissionTrainingProgram(admissionId, createTrainingProgramRequests);
        return savedPrograms.size();
    }

    public List<AdmissionTrainingProgram> findAll() {
        return admissionTrainingProgramRepository.findAll();
    }

    public List<AdmissionTrainingProgram> findByMajorIdAndUniversityIdsAndYear(Integer majorId, List<Integer> universityIds, Integer year) {
        List<AdmissionTrainingProgram> admissionTrainingPrograms = admissionTrainingProgramRepository.findByMajorIdAndUniversityIdsAndYearCustom(majorId, universityIds, year);
        if (admissionTrainingPrograms.isEmpty()) {
            throw new ResourceNotFoundException("Không tìm thấy dữ liệu so sánh.");
        }
        if (admissionTrainingPrograms.size() < 2){
            throw new ResourceNotFoundException("Số lượng ngành học cần so sánh phải lớn hơn 1.");
        }
        return admissionTrainingPrograms;
    }

    public List<AdmissionTrainingProgram> findByMajorIdAndYear(Integer majorId, Integer year) {
        return admissionTrainingProgramRepository.findByMajorIdAndYear(majorId, year);
    }

    public List<AdmissionTrainingProgram> findByMajorIdWithUniversityIdIdAndYear(List<CompareMajorsFromUniversitiesRequest> request, Integer year) {
        String query
        return null;
    }

    private String buildQueryFindByMajorIdWithUniversityIdIdAndYear(List<CompareMajorsFromUniversitiesRequest> request, Integer year, Map<String, Object> parameters) {

        StringBuilder queryBuilder = new StringBuilder("select a.university_id, a.id, atp.*\n" +
                "from admission_training_program atp\n" +
                "inner join dbo.admission a on a.id = atp.admission_id\n" +
                "where a.status = 'ACTIVE' and a.year = 2024");

        parameters.put("year", year);

        queryBuilder.append("and (");

        queryBuilder.append(")");
        return queryBuilder.toString();
    }
}
