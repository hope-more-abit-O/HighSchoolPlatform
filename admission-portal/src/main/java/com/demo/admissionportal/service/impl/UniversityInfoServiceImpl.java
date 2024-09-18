package com.demo.admissionportal.service.impl;

import com.demo.admissionportal.dto.entity.admission.SchoolDirectoryRequest;
import com.demo.admissionportal.entity.UniversityInfo;
import com.demo.admissionportal.entity.admission.AdmissionTrainingProgramMethod;
import com.demo.admissionportal.exception.exceptions.ResourceNotFoundException;
import com.demo.admissionportal.repository.UniversityInfoRepository;
import com.demo.admissionportal.service.UniversityInfoService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class UniversityInfoServiceImpl implements UniversityInfoService {
    private final UniversityInfoRepository universityInfoRepository;
    private final EntityManager entityManager;

    @Override
    public UniversityInfo findById(Integer id)throws ResourceNotFoundException {
        return universityInfoRepository.findById(id).orElseThrow(() -> {
            log.error("University's information with id: {} not found.", id);
            return new ResourceNotFoundException("University's information with id: " + id + " not found");
        });
    }

    public List<UniversityInfo> findByStaffId(Integer id)throws ResourceNotFoundException {
        return universityInfoRepository.findByStaffId(id);
    }

    public List<UniversityInfo> findByIds(List<Integer> ids)throws ResourceNotFoundException {
        return universityInfoRepository.findAllById(ids);
    }

    public List<UniversityInfo> findByCodes(List<String> codes)throws ResourceNotFoundException {
        return universityInfoRepository.findByCodeIn(codes);
    }

    public List<UniversityInfo> schoolDirectoryGetUniversityInfo(SchoolDirectoryRequest schoolDirectoryRequest) {
        Map<String, Object> parameters = new HashMap<>();

        String query = buildQueryForSchoolDirectory(schoolDirectoryRequest, parameters);

        Query executeQuery = entityManager.createNativeQuery(query.toString(), UniversityInfo.class);

        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            executeQuery.setParameter(entry.getKey(), entry.getValue());
        }

        return executeQuery.getResultList();
    }

    private String buildQueryForSchoolDirectory(SchoolDirectoryRequest schoolDirectoryRequest, Map<String, Object> parameters) {

        StringBuilder queryBuilder = new StringBuilder("select distinct ui.*\n" +
                "from university_info ui\n" +
                "inner join university_campus uc on uc.university_id = ui.university_id\n" +
                "inner join admission a on a.university_id = ui.university_id\n" +
                "inner join admission_training_program atp on atp.admission_id = a.id\n" +
                "inner join admission_training_program_subject_group atpsg on atp.id = atpsg.admission_training_program_id\n" +
                "inner join admission_method am on a.id = am.admission_id\n" +
                "inner join admission_training_program_method atpm on atpm.admission_training_program_id = atp.id\n");

        boolean hasConditions = false;

        if (schoolDirectoryRequest.getUniversityIds() != null && !schoolDirectoryRequest.getUniversityIds().isEmpty()) {
            queryBuilder.append("where ui.university_id in (:universityIds)\n");
            parameters.put("universityIds", schoolDirectoryRequest.getUniversityIds());
            hasConditions = true;
        }

        if (schoolDirectoryRequest.getSubjectGroupIds() != null && !schoolDirectoryRequest.getSubjectGroupIds().isEmpty()) {
            queryBuilder.append(hasConditions ? "and " : "where ").append("atpsg.subject_group_id in (:subjectGroupIds)\n");
            parameters.put("subjectGroupIds", schoolDirectoryRequest.getSubjectGroupIds());
            hasConditions = true;
        }

        if (schoolDirectoryRequest.getMethodIds() != null && !schoolDirectoryRequest.getMethodIds().isEmpty()) {
            queryBuilder.append(hasConditions ? "and " : "where ").append("atpm.method_id in (:methodIds)\n");
            parameters.put("methodIds", schoolDirectoryRequest.getMethodIds());
            hasConditions = true;
        }

        if (schoolDirectoryRequest.getProvinceIds() != null && !schoolDirectoryRequest.getProvinceIds().isEmpty()) {
            queryBuilder.append(hasConditions ? "and " : "where ").append("uc.province_id in (:provinceIds)\n");
            parameters.put("provinceIds", schoolDirectoryRequest.getProvinceIds());
            hasConditions = true;
        }

        if (schoolDirectoryRequest.getPageNumber() != null && schoolDirectoryRequest.getPageSize() != null) {
            queryBuilder.append("ORDER BY ui.university_id desc\n")
                    .append("OFFSET :PageNumber * :PageSize ROWS\n")
                    .append("FETCH NEXT :PageSize ROWS ONLY");
            parameters.put("PageNumber", schoolDirectoryRequest.getPageNumber());
            parameters.put("PageSize", schoolDirectoryRequest.getPageSize());
        }

        return queryBuilder.toString();
    }
}