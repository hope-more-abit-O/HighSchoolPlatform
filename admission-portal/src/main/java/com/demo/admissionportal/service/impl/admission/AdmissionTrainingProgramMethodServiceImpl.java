package com.demo.admissionportal.service.impl.admission;

import com.demo.admissionportal.dto.entity.admission.dto.AdmissionTrainingProgramMethodDTOV2;
import com.demo.admissionportal.dto.request.admisison.CreateAdmissionTrainingProgramMethodRequest;
import com.demo.admissionportal.dto.request.admisison.UpdateAdmissionScoreRequest;
import com.demo.admissionportal.entity.admission.AdmissionMethod;
import com.demo.admissionportal.entity.admission.AdmissionTrainingProgramMethod;
import com.demo.admissionportal.entity.admission.sub_entity.AdmissionTrainingProgramMethodId;
import com.demo.admissionportal.exception.exceptions.ResourceNotFoundException;
import com.demo.admissionportal.repository.admission.AdmissionTrainingProgramMethodRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdmissionTrainingProgramMethodServiceImpl {
    private final AdmissionTrainingProgramMethodRepository admissionTrainingProgramMethodRepository;
    private final EntityManager entityManager;


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

    public List<AdmissionTrainingProgramMethod> findAdmissionTrainingProgramIds(List<Integer> admissionTrainingProgramIds) {
        return admissionTrainingProgramMethodRepository.findById_AdmissionTrainingProgramIdIn(admissionTrainingProgramIds);
    }

    public List<AdmissionTrainingProgramMethod> findBySubjectGroupIdAndScoreWithOffset(Integer subjectGroupId, Float score, Float offset, String majorId, Integer provinceId) {
        return admissionTrainingProgramMethodRepository.findBySubjectGroupIdAndScoreWithOffset(subjectGroupId, score, offset, majorId + "%", provinceId, Calendar.getInstance().get(Calendar.YEAR));
    }

    public List<AdmissionTrainingProgramMethod> findAdmissionData(List<Integer> subjectGroupId, Float score, Float offset, List<Integer> methodId,
                                                                       List<Integer> majorId, List<Integer> provinceId, Integer year) {

        // 1. Start building the base query
        StringBuilder queryBuilder = new StringBuilder(
                "SELECT DISTINCT atpm.* " +
                        "from admission a\n" +
                        " inner join admission_training_program atp on a.id = atp.admission_id\n" +
                        " inner join admission_training_program_method atpm on atp.id = atpm.admission_training_program_id\n" +
                        " inner join admission_training_program_subject_group atpsg on atp.id = atpsg.admission_training_program_id\n" +
                        " inner join subject_group sg on sg.id = atpsg.subject_group_id\n" +
                        " inner join university_campus uc on uc.university_id = a.university_id\n" +
                        " inner join dbo.admission_method am on atpm.admission_method_id = am.id\n" +
                        " where a.status = 'ACTIVE' \n"
        );

        // 2. Create a Map to store parameters
        Map<String, Object> parameters = new HashMap<>();

        if (methodId != null && !methodId.isEmpty()) {
            List<String> placeholders = new ArrayList<>();
            for (int i = 0; i < methodId.size(); i++) {
                placeholders.add(":methodId" + i);
            }

            queryBuilder.append(" AND am.method_id IN (")
                    .append(String.join(",", placeholders))
                    .append(") ");

            for (int i = 0; i < methodId.size(); i++) {
                parameters.put("methodId" + i, methodId.get(i));
            }
        }

        if (subjectGroupId != null && !subjectGroupId.isEmpty()) {
            List<String> placeholders = new ArrayList<>();
            for (int i = 0; i < subjectGroupId.size(); i++) {
                placeholders.add(":subjectGroupId" + i);
            }

            queryBuilder.append(" AND atpsg.subject_group_id IN (")
                    .append(String.join(",", placeholders))
                    .append(") ");

            for (int i = 0; i < subjectGroupId.size(); i++) {
                parameters.put("subjectGroupId" + i, subjectGroupId.get(i));
            }
        }

        if (score != null) {
            if (offset != null) offset = Float.parseFloat(String.valueOf("2"));
            queryBuilder.append(" AND atpm.addmission_score <= (:score) ");
            parameters.put("score", score + offset);
        }

        if (majorId != null && !majorId.isEmpty()) {
            List<String> placeholders = new ArrayList<>();
            for (int i = 0; i < majorId.size(); i++) {
                placeholders.add(":majorId" + i);
            }

            queryBuilder.append(" AND atp.major_id IN (")
                    .append(String.join(",", placeholders))
                    .append(") ");

            for (int i = 0; i < majorId.size(); i++) {
                parameters.put("majorId" + i, majorId.get(i));
            }
        }

        if (provinceId != null && !provinceId.isEmpty()) {
            List<String> placeholders = new ArrayList<>();
            for (int i = 0; i < provinceId.size(); i++) {
                placeholders.add(":provinceId" + i);
            }

            queryBuilder.append(" AND uc.province_id IN (")
                    .append(String.join(",", placeholders))
                    .append(") ");

            for (int i = 0; i < provinceId.size(); i++) {
                parameters.put("provinceId" + i, provinceId.get(i));
            }
        }

        // 4. Add the year filter (seems to be always required)
        queryBuilder.append(" AND a.year IN (:yearMinus3, :yearMinus2, :yearMinus1, :currentYear) ");
        parameters.put("yearMinus3", year - 3);
        parameters.put("yearMinus2", year - 2);
        parameters.put("yearMinus1", year - 1);
        parameters.put("currentYear", 2024);

        // 5. Create and execute the TypedQuery
        Query a = entityManager.createNativeQuery(queryBuilder.toString(), AdmissionTrainingProgramMethod.class);

        // 6. Set the parameters
        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            a.setParameter(entry.getKey(), entry.getValue());
        }

        // 7. Execute and return the results
        var b = a.getResultList();
        return b;
    }
}
