package com.demo.admissionportal.service.impl.admission;

import com.demo.admissionportal.constants.AdmissionStatus;
import com.demo.admissionportal.dto.entity.admission.AdmissionTrainingProgramMethodQuotaDTO;
import com.demo.admissionportal.dto.request.admisison.*;
import com.demo.admissionportal.entity.admission.*;
import com.demo.admissionportal.entity.admission.sub_entity.AdmissionTrainingProgramMethodId;
import com.demo.admissionportal.exception.exceptions.BadRequestException;
import com.demo.admissionportal.exception.exceptions.ResourceNotFoundException;
import com.demo.admissionportal.repository.admission.AdmissionTrainingProgramMethodRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
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
    private final AdmissionTrainingProgramServiceImpl admissionTrainingProgramServiceImpl;
    private final AdmissionMethodServiceImpl admissionMethodServiceImpl;


    public List<AdmissionTrainingProgramMethod> saveAll(List<AdmissionTrainingProgramMethod> admissionTrainingProgramMethods) {
        return admissionTrainingProgramMethodRepository.saveAll(admissionTrainingProgramMethods);
    }

//    public List<AdmissionTrainingProgramMethod> createAdmissionTrainingProgramMethod(CreateAdmissionTrainingProgramMethodRequest request) {
//        //TODO: CHECK EXIST by admissionTrainingProgramId and admissionMethodId
//
//        List<AdmissionTrainingProgramMethod> admissionTrainingProgramMethods = request.getQuotas().stream()
//                .map(quota -> new AdmissionTrainingProgramMethod(quota.getAdmissionTrainingProgramId(), quota.getAdmissionMethodId(), quota.getQuota()))
//                .toList();
//
//        return saveAll(admissionTrainingProgramMethods);
//    }

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
    public List<AdmissionTrainingProgramMethod> findAdmissionTrainingPrograms(List<Integer> subjectId, Float fromScore, Float toScore, List<Integer> methodId,
                                                                              List<Integer> majorId, List<Integer> provinceId, Integer year, List<String> regions, Integer pageNumber, Integer rowsPerPage) {
        Map<String, Object> parameters = new HashMap<>();

        String query = buildQueryForScoreAdvice(subjectId, fromScore, toScore, methodId, majorId, provinceId, year, regions, pageNumber, rowsPerPage, parameters);

        // 5. Create and execute the TypedQuery
        Query executeQuery = entityManager.createNativeQuery(query.toString(), AdmissionTrainingProgramMethod.class);

        // 6. Set the parameters
        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            executeQuery.setParameter(entry.getKey(), entry.getValue());
        }

        // 7. Execute and return the results
        var b = executeQuery.getResultList();
        return b;
    }
    public List<AdmissionTrainingProgramMethod> findAdmissionTrainingProgramsV2(List<Integer> subjectId, Float fromScore, Float toScore, List<Integer> methodId,
                                                                                List<String> majorCode, List<Integer> provinceId, Integer year, List<String> regions, Integer pageNumber, Integer rowsPerPage) {
        Map<String, Object> parameters = new HashMap<>();

        String query = buildQueryForScoreAdviceV2(subjectId, fromScore, toScore, methodId, majorCode, provinceId, year, regions, pageNumber, rowsPerPage, parameters);

        // 5. Create and execute the TypedQuery
        Query executeQuery = entityManager.createNativeQuery(query.toString(), AdmissionTrainingProgramMethod.class);

        // 6. Set the parameters
        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            executeQuery.setParameter(entry.getKey(), entry.getValue());
        }

        // 7. Execute and return the results
        var b = executeQuery.getResultList();
        return b;
    }

    public Integer countAll(){
        return admissionTrainingProgramMethodRepository.countAll();
    }

    public Integer count(List<Integer> subjectId, Float fromScore, Float toScore, List<Integer> methodId,
                         List<Integer> majorId, List<Integer> provinceId, Integer year, List<String> regions, Integer pageNumber, Integer rowsPerPage) {
        Map<String, Object> parameters = new HashMap<>();

        String query = buildCountQuery(buildQueryForScoreAdvice(subjectId, fromScore, toScore, methodId, majorId, provinceId, year, regions, null, null, parameters));

        // 5. Create and execute the TypedQuery
        Query executeQuery = entityManager.createNativeQuery(query.toString(), Integer.class);

        // 6. Set the parameters
        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            executeQuery.setParameter(entry.getKey(), entry.getValue());
        }

        // 7. Execute and return the results
        Integer b = (Integer) executeQuery.getSingleResult();
        return b;
    }

    public Integer countV2(List<Integer> subjectId, Float fromScore, Float toScore, List<Integer> methodId,
                           List<String> majorId, List<Integer> provinceId, Integer year, List<String> regions, Integer pageNumber, Integer rowsPerPage) {
        Map<String, Object> parameters = new HashMap<>();

        String query = buildCountQuery(buildQueryForScoreAdviceV2(subjectId, fromScore, toScore, methodId, majorId, provinceId, year, regions, null, null, parameters));

        // 5. Create and execute the TypedQuery
        Query executeQuery = entityManager.createNativeQuery(query.toString(), Integer.class);

        // 6. Set the parameters
        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            executeQuery.setParameter(entry.getKey(), entry.getValue());
        }

        // 7. Execute and return the results
        Integer b = (Integer) executeQuery.getSingleResult();
        return b;
    }

    public String buildCountQuery(String query){
        String stringBuilder = new StringBuilder("SELECT COUNT(*) FROM (")
                .append(query)
                .append(") as count").toString();

        return stringBuilder.toString();
    }

    public String buildQueryForScoreAdvice(List<Integer> subjectId, Float fromScore, Float toScore, List<Integer> methodId,
                                           List<Integer> majorId, List<Integer> provinceId, Integer year, List<String> regions,
                                           Integer pageNumber, Integer rowsPerPage, Map<String, Object> parameters){
        // 1. Start building the base query
        StringBuilder queryBuilder = new StringBuilder(
                "SELECT DISTINCT atpm.* " +
                        "from admission a\n" +
                        " inner join admission_training_program atp on a.id = atp.admission_id\n" +
                        " inner join admission_training_program_method atpm on atp.id = atpm.admission_training_program_id\n" +
                        " inner join admission_training_program_subject_group atpsg on atp.id = atpsg.admission_training_program_id\n" +
                        " inner join subject_group_subject sgs on sgs.subject_group_id = atpsg.subject_group_id\n" +
                        " inner join university_campus uc on uc.university_id = a.university_id\n" +
                        " inner join province p on p.id = uc.province_id\n" +
                        " inner join dbo.admission_method am on atpm.admission_method_id = am.id\n" +
                        " where a.status = 'ACTIVE' \n"
//                        " where 1 = 1 \n"
        );

        if (regions != null && !regions.isEmpty() && provinceId != null && !provinceId.isEmpty()) {
            // Case where both regions and provinceId are not null
            List<String> regionPlaceholders = new ArrayList<>();
            List<String> provincePlaceholders = new ArrayList<>();

            for (int i = 0; i < regions.size(); i++) {
                regionPlaceholders.add(":region" + i);
            }
            for (int i = 0; i < provinceId.size(); i++) {
                provincePlaceholders.add(":provinceId" + i);
            }

            queryBuilder.append(" AND (p.region IN (")
                    .append(String.join(",", regionPlaceholders))
                    .append(") OR uc.province_id IN (")
                    .append(String.join(",", provincePlaceholders))
                    .append(")) ");

            for (int i = 0; i < regions.size(); i++) {
                parameters.put("region" + i, regions.get(i));
            }
            for (int i = 0; i < provinceId.size(); i++) {
                parameters.put("provinceId" + i, provinceId.get(i));
            }

        } else {
            // Case where only regions or only provinceId is not null
            if (regions != null && !regions.isEmpty()) {
                List<String> placeholders = new ArrayList<>();
                for (int i = 0; i < regions.size(); i++) {
                    placeholders.add(":region" + i);
                }

                queryBuilder.append(" AND p.region IN (")
                        .append(String.join(",", placeholders))
                        .append(") ");

                for (int i = 0; i < regions.size(); i++) {
                    parameters.put("region" + i, regions.get(i));
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
        }

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

        if (subjectId != null && !subjectId.isEmpty()) {
            List<String> placeholders = new ArrayList<>();
            for (int i = 0; i < subjectId.size(); i++) {
                placeholders.add(":subjectId" + i);
            }

            queryBuilder.append(" AND sgs.subject_id IN (")
                    .append(String.join(",", placeholders))
                    .append(") ");

            for (int i = 0; i < subjectId.size(); i++) {
                parameters.put("subjectId" + i, subjectId.get(i));
            }
        }

        if (fromScore != null && toScore != null) {
            queryBuilder.append(" AND atpm.addmission_score <= (:toScore) AND atpm.addmission_score >= (:fromScore) ");
            parameters.put("fromScore", fromScore);
            parameters.put("toScore", toScore);
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

        // 4. Add the year filter (seems to be always required)
        queryBuilder.append(" AND a.year IN (:yearMinus3, :yearMinus2, :yearMinus1, :currentYear)\n");
        parameters.put("yearMinus3", year - 3);
        parameters.put("yearMinus2", year - 2);
        parameters.put("yearMinus1", year - 1);
        parameters.put("currentYear", year);


        if (pageNumber == null || rowsPerPage == null) {
            return queryBuilder.toString();
        }
        queryBuilder.append("ORDER BY atpm.admission_training_program_id desc\n" +
                "OFFSET :PageNumber * :RowsPerPage ROWS\n" +
                "FETCH NEXT :RowsPerPage ROWS ONLY");
        parameters.put("PageNumber", pageNumber);
        parameters.put("RowsPerPage", rowsPerPage);
        return queryBuilder.toString();
    }

    public String buildQueryForScoreAdviceV2(List<Integer> subjectId, Float fromScore, Float toScore, List<Integer> methodId,
                                             List<String> majorCode, List<Integer> provinceId, Integer year, List<String> regions,
                                             Integer pageNumber, Integer rowsPerPage, Map<String, Object> parameters){
        // 1. Start building the base query
        StringBuilder queryBuilder = new StringBuilder(
                "SELECT DISTINCT atpm.* " +
                        "from admission a\n" +
                        " inner join admission_training_program atp on a.id = atp.admission_id\n" +
                        " inner join admission_training_program_method atpm on atp.id = atpm.admission_training_program_id\n" +
                        " inner join admission_training_program_subject_group atpsg on atp.id = atpsg.admission_training_program_id\n" +
                        " inner join subject_group_subject sgs on sgs.subject_group_id = atpsg.subject_group_id\n" +
                        " inner join university_campus uc on uc.university_id = a.university_id\n" +
                        " inner join province p on p.id = uc.province_id\n" +
                        " inner join dbo.admission_method am on atpm.admission_method_id = am.id\n" +
                        " inner join dbo.major m on m.id = atp.major_id\n" +
                        " where a.status = 'ACTIVE' \n"
//                        " where 1 = 1 \n"
        );

        if (regions != null && !regions.isEmpty() && provinceId != null && !provinceId.isEmpty()) {
            // Case where both regions and provinceId are not null
            List<String> regionPlaceholders = new ArrayList<>();
            List<String> provincePlaceholders = new ArrayList<>();

            for (int i = 0; i < regions.size(); i++) {
                regionPlaceholders.add(":region" + i);
            }
            for (int i = 0; i < provinceId.size(); i++) {
                provincePlaceholders.add(":provinceId" + i);
            }

            queryBuilder.append(" AND (p.region IN (")
                    .append(String.join(",", regionPlaceholders))
                    .append(") OR uc.province_id IN (")
                    .append(String.join(",", provincePlaceholders))
                    .append(")) ");

            for (int i = 0; i < regions.size(); i++) {
                parameters.put("region" + i, regions.get(i));
            }
            for (int i = 0; i < provinceId.size(); i++) {
                parameters.put("provinceId" + i, provinceId.get(i));
            }

        } else {
            // Case where only regions or only provinceId is not null
            if (regions != null && !regions.isEmpty()) {
                List<String> placeholders = new ArrayList<>();
                for (int i = 0; i < regions.size(); i++) {
                    placeholders.add(":region" + i);
                }

                queryBuilder.append(" AND p.region IN (")
                        .append(String.join(",", placeholders))
                        .append(") ");

                for (int i = 0; i < regions.size(); i++) {
                    parameters.put("region" + i, regions.get(i));
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
        }

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

        if (subjectId != null && !subjectId.isEmpty()) {
            List<String> placeholders = new ArrayList<>();
            for (int i = 0; i < subjectId.size(); i++) {
                placeholders.add(":subjectId" + i);
            }

            queryBuilder.append(" AND sgs.subject_id IN (")
                    .append(String.join(",", placeholders))
                    .append(") ");

            for (int i = 0; i < subjectId.size(); i++) {
                parameters.put("subjectId" + i, subjectId.get(i));
            }
        }

        if (fromScore != null && toScore != null) {
            queryBuilder.append(" AND atpm.addmission_score <= (:toScore) AND atpm.addmission_score >= (:fromScore) ");
            parameters.put("fromScore", fromScore);
            parameters.put("toScore", toScore);
        }

        if (majorCode != null && !majorCode.isEmpty()) {
            List<String> likeClauses = new ArrayList<>();

            for (int i = 0; i < majorCode.size(); i++) {
                likeClauses.add("m.code LIKE :majorCode" + i);
            }

            queryBuilder.append(" AND (")
                    .append(String.join(" OR ", likeClauses))
                    .append(") \n");

            for (int i = 0; i < majorCode.size(); i++) {
                parameters.put("majorCode" + i, majorCode.get(i) + "%");
            }
        }

        // 4. Add the year filter (seems to be always required)
        queryBuilder.append(" AND a.year IN (:yearMinus3, :yearMinus2, :yearMinus1, :currentYear)\n");
        parameters.put("yearMinus3", year - 3);
        parameters.put("yearMinus2", year - 2);
        parameters.put("yearMinus1", year - 1);
        parameters.put("currentYear", year);


        if (pageNumber == null || rowsPerPage == null) {
            return queryBuilder.toString();
        }
        queryBuilder.append("ORDER BY atpm.admission_training_program_id desc\n" +
                "OFFSET :PageNumber * :RowsPerPage ROWS\n" +
                "FETCH NEXT :RowsPerPage ROWS ONLY");
        parameters.put("PageNumber", pageNumber);
        parameters.put("RowsPerPage", rowsPerPage);
        return queryBuilder.toString();
    }

    public List<AdmissionTrainingProgramMethod> findByUniversityIdAndYearAndStatus(Integer id, int year, AdmissionStatus admissionStatus) {
        return admissionTrainingProgramMethodRepository.findByUniversityIdAndYearAndStatus(id, year, admissionStatus.name());
    }

    public List<AdmissionTrainingProgramMethod> findByUniversityIdInAndYearAndStatus(List<Integer> id, int year, AdmissionStatus admissionStatus) {
        return admissionTrainingProgramMethodRepository.findByUniversityIdAndYearAndStatus(id, year, admissionStatus.name());
    }

    public List<AdmissionTrainingProgramMethod> findByAdmissionTrainingProgramIdInAndMethodIds(List<Integer> admissionTrainingProgramIds, List<Integer> methodIds) {
        return admissionTrainingProgramMethodRepository.findByAdmissionTrainingProgramIdInAndMethodIds(admissionTrainingProgramIds, methodIds);
    }

    public List<AdmissionTrainingProgramMethod> findByAdmissionTrainingProgramIdsAndAdmissionMethodIds(List<Integer> admissionTrainingProgramIds, List<Integer> admissionMethodIds) {
        return admissionTrainingProgramMethodRepository.findById_AdmissionTrainingProgramIdInAndId_AdmissionMethodIdIn(admissionTrainingProgramIds, admissionMethodIds);
    }

    public Integer update(Admission admission, UpdateAdmissionTrainingMethodRequest request) {
        List<AdmissionTrainingProgramMethod> admissionTrainingProgramMethods = findByAdmissionId(admission.getId());
        Integer modified = 0;
        if (request.getDeleteAdmissionTrainingProgramRequests() != null && request.getDeleteAdmissionTrainingProgramRequests().getDeleteAdmissionTrainingProgramMethods() != null && !request.getDeleteAdmissionTrainingProgramRequests().getDeleteAdmissionTrainingProgramMethods().isEmpty()){
            modified += deleteAdmissionTrainingProgramMethod(request.getDeleteAdmissionTrainingProgramRequests(), admissionTrainingProgramMethods);
        }

        if (request.getCreateAdmissionTrainingProgramMethodRequests() != null && request.getCreateAdmissionTrainingProgramMethodRequests().getAdmissionTrainingProgramMethodQuotas() != null && !request.getCreateAdmissionTrainingProgramMethodRequests().getAdmissionTrainingProgramMethodQuotas().isEmpty()){
            modified += createAdmissionTrainingProgramMethod(request.getCreateAdmissionTrainingProgramMethodRequests());
        }

        if (request.getModifyAdmissionTrainingProgramMethodRequests() != null && !request.getModifyAdmissionTrainingProgramMethodRequests().isEmpty()){
            modified += updateAdmissionTrainingProgramMethod(request.getModifyAdmissionTrainingProgramMethodRequests());
        }

        return modified;
    }

    private Integer updateAdmissionTrainingProgramMethod(List<ModifyAdmissionTrainingProgramMethodRequest> modifyAdmissionTrainingProgramMethodRequests) {
        List<AdmissionTrainingProgramMethodId> admissionTrainingProgramMethodIds = modifyAdmissionTrainingProgramMethodRequests.stream().map(AdmissionTrainingProgramMethodId::new).toList();
        List<AdmissionTrainingProgramMethod> existed = admissionTrainingProgramMethodRepository.findAllById(admissionTrainingProgramMethodIds);
        List<AdmissionTrainingProgramMethodId> existedIds = existed.stream().map(AdmissionTrainingProgramMethod::getId).toList();
        Map<String, String> errors = new HashMap<>();
        if (existed.size() < modifyAdmissionTrainingProgramMethodRequests.size()){
            admissionTrainingProgramMethodIds.forEach(id -> {
                if (!existedIds.contains(id)){
                    errors.put("existed", id.toAObject() + ";");
                }
            });
            throw new ResourceNotFoundException("Admission training program method not existed", errors);
        }

        List<AdmissionTrainingProgramMethod> admissionTrainingProgramMethods = modifyAdmissionTrainingProgramMethodRequests.stream().map(AdmissionTrainingProgramMethod::new).toList();
        return saveAll(admissionTrainingProgramMethods).size();
    }

    public Integer createAdmissionTrainingProgramMethod(CreateAdmissionTrainingProgramMethodRequest request) {
        List<Integer> admissionTrainingProgramIds = request.getAdmissionTrainingProgramMethodQuotas().stream().map(AdmissionTrainingProgramMethodQuotaDTO::getAdmissionTrainingProgramId).distinct().toList();
        List<Integer> admissionMethodIds = request.getAdmissionTrainingProgramMethodQuotas().stream().map(AdmissionTrainingProgramMethodQuotaDTO::getAdmissionMethodId).distinct().toList();

        List<AdmissionTrainingProgram> admissionTrainingPrograms = admissionTrainingProgramServiceImpl.findByIds(admissionTrainingProgramIds);
        List<AdmissionMethod> admissionMethods = admissionMethodServiceImpl.findByIds(admissionMethodIds);

        Set<Integer> admissionIds = new HashSet<>();
        admissionTrainingPrograms.forEach(admissionTrainingProgram -> admissionIds.add(admissionTrainingProgram.getAdmissionId()));
        admissionMethods.forEach(admissionMethod -> admissionIds.add(admissionMethod.getAdmissionId()));

        if (admissionIds.size() > 1){
            throw new BadRequestException("Chương trình đào tạo và phương thức xét tuyển không cùng một đề án");
        }

        List<AdmissionTrainingProgramMethod> existedAdmissionTrainingProgramMethods = admissionTrainingProgramMethodRepository.findById_AdmissionMethodIdInAndId_AdmissionTrainingProgramIdIn(admissionMethodIds, admissionTrainingProgramIds);
        if (!existedAdmissionTrainingProgramMethods.isEmpty()){
            throw new BadRequestException("Có chương trình đào tạo và phương thức xét tuyển đã tồn tại");
        }

        List<AdmissionTrainingProgramMethod> admissionTrainingProgramMethods = request.getAdmissionTrainingProgramMethodQuotas().stream()
                .map(quota -> new AdmissionTrainingProgramMethod(quota.getAdmissionTrainingProgramId(), quota.getAdmissionMethodId(), quota.getQuota()))
                .toList();

        return saveAll(admissionTrainingProgramMethods).size();
    }

    public Integer deleteAdmissionTrainingProgramMethod(DeleteAdmissionTrainingProgramMethodRequest request, List<AdmissionTrainingProgramMethod> admissionTrainingProgramMethods) {
        List<AdmissionTrainingProgramMethodId> admissionTrainingProgramMethodIds = request.getDeleteAdmissionTrainingProgramMethods().stream().map(AdmissionTrainingProgramMethodId::new).toList();

//        checkNotExist(admissionTrainingProgramMethodIds, request.getDeleteAdmissionTrainingProgramMethods(), "Admission training program method not existed");

        return admissionTrainingProgramMethodRepository.deleteByIdIn(admissionTrainingProgramMethodIds);
    }

    public List<AdmissionTrainingProgramMethodId> findNotExists(List<AdmissionTrainingProgramMethodId> admissionTrainingProgramMethodIds, List<AdmissionTrainingProgramMethodId> deleteAdmissionTrainingProgramMethodIds) {
        return deleteAdmissionTrainingProgramMethodIds.stream().filter(deleteAdmissionTrainingProgramMethodId -> !admissionTrainingProgramMethodIds.contains(deleteAdmissionTrainingProgramMethodId)).toList();
    }

//    public void checkNotExist(List<AdmissionTrainingProgramMethodId> admissionTrainingProgramMethodIds, List<DeleteAdmissionTrainingProgramMethodIdDTO> deleteAdmissionTrainingProgramMethodIds, String errorMessage){
//        List<AdmissionTrainingProgramMethod> admissionTrainingProgramMethods = findByAdmissionId(admissionTrainingProgramMethodIds, false);
//        List<AdmissionTrainingProgramMethodId> notExists = findNotExists(admissionTrainingProgramMethodIds, deleteAdmissionTrainingProgramMethodIds);
//        Map<String, String> errors = new HashMap<>();
//        if (!notExists.isEmpty()){
//            notExists.forEach(notExist -> {
//                errors.put("existed", notExist.toAObject() + ";");
//            });
//
//            throw new BadRequestException(errorMessage, errors);
//        };
//    }

    public Integer deleteByAdmissionTrainingProgramIds(List<Integer> admissionTrainingProgramIds) {
        return admissionTrainingProgramMethodRepository.deleteById_AdmissionTrainingProgramIdIn(admissionTrainingProgramIds);
    }

    public Integer deleteByAdmissionMethodIds(List<Integer> admissionMethodId) {
        return admissionTrainingProgramMethodRepository.deleteById_AdmissionTrainingProgramIdIn(admissionMethodId);
    }
}