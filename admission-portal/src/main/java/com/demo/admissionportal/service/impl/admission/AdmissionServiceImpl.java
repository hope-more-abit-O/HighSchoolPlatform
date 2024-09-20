package com.demo.admissionportal.service.impl.admission;

import com.demo.admissionportal.constants.*;
import com.demo.admissionportal.dto.entity.admission.UniversityCompareMajorDTO;
import com.demo.admissionportal.dto.entity.ActionerDTO;
import com.demo.admissionportal.dto.entity.admission.*;
import com.demo.admissionportal.dto.entity.admission.school_advice.SchoolAdviceDTO;
import com.demo.admissionportal.dto.entity.admission.school_advice.SchoolAdviceMajorDetailDTO;
import com.demo.admissionportal.dto.entity.major.InfoMajorDTO;
import com.demo.admissionportal.dto.entity.method.InfoMethodDTO;
import com.demo.admissionportal.dto.entity.university.InfoUniversityResponseDTO;
import com.demo.admissionportal.dto.entity.university.UniversityInfoResponseDTO;
import com.demo.admissionportal.dto.entity.university_campus.CampusProvinceDTO;
import com.demo.admissionportal.dto.entity.user.InfoUserResponseDTO;
import com.demo.admissionportal.dto.request.AdmissionAnalysisRequest;
import com.demo.admissionportal.dto.request.admisison.*;
import com.demo.admissionportal.dto.response.AdmissionAnalysisResponse;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.dto.response.admission.*;
import com.demo.admissionportal.dto.response.sub_entity.SubjectGroupResponseDTO2;
import com.demo.admissionportal.entity.*;
import com.demo.admissionportal.entity.admission.*;
import com.demo.admissionportal.entity.admission.sub_entity.AdmissionTrainingProgramMethodId;
import com.demo.admissionportal.entity.admission.sub_entity.AdmissionTrainingProgramSubjectGroupId;
import com.demo.admissionportal.exception.exceptions.*;
import com.demo.admissionportal.repository.MajorRepository;
import com.demo.admissionportal.repository.SubjectGroupRepository;
import com.demo.admissionportal.repository.UniversityInfoRepository;
import com.demo.admissionportal.repository.admission.AdmissionRepository;
import com.demo.admissionportal.repository.admission.AdmissionTrainingProgramMethodRepository;
import com.demo.admissionportal.repository.admission.AdmissionTrainingProgramSubjectGroupRepository;
import com.demo.admissionportal.service.AdmissionService;
import com.demo.admissionportal.service.UserService;
import com.demo.admissionportal.service.impl.*;
import com.demo.admissionportal.util.impl.ServiceUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdmissionServiceImpl implements AdmissionService {
    private final AdmissionRepository admissionRepository;
    private final AdmissionTrainingProgramMethodServiceImpl admissionTrainingProgramMethodService;
    private final AdmissionTrainingProgramServiceImpl admissionTrainingProgramService;
    private final AdmissionTrainingProgramSubjectGroupServiceImpl admissionTrainingProgramSubjectGroupService;
    private final AdmissionMethodServiceImpl admissionMethodService;
    private final UserService userService;
    private final MajorServiceImpl majorService;
    private final MethodServiceImpl methodService;
    private final ModelMapper modelMapper;
    private final SubjectGroupServiceImpl subjectGroupService;
    private final UniversityInfoServiceImpl universityInfoServiceImpl;
    private final SubjectGroupServiceImpl subjectGroupServiceImpl;
    private final SubjectServiceImpl subjectServiceImpl;
    private final ConsultantInfoServiceImpl consultantInfoServiceImpl;
    private final UniversityTrainingProgramServiceImpl universityTrainingProgramService;
    private final UniversityCampusServiceImpl universityCampusService;
    private final EntityManager entityManager;
    private final UniversityInfoRepository universityInfoRepository;
    private final AddressServiceImpl addressServiceImpl;
    private final SubjectServiceImpl subjectService;
    private final AdmissionTrainingProgramMethodRepository admissionTrainingProgramMethodRepository;
    private final SubjectGroupRepository subjectGroupRepository;
    private final AdmissionTrainingProgramSubjectGroupRepository admissionTrainingProgramSubjectGroupRepository;
    private final MajorRepository majorRepository;
    private final StudentReportServiceImpl studentReportService;
    private final DistrictServiceImpl districtServiceImpl;
    private final WardServiceImpl wardServiceImpl;

    public Admission save(Admission admission) {
        try {
            return admissionRepository.save(admission);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            throw new StoreDataFailedException("Lưu đề án thất bại", error);
        }
    }

    public Admission findById(Integer id) {
        log.info("Finding admission by id: {}", id);
        return admissionRepository.findById(id).orElseThrow(() -> {
            log.error("Admission with id: {} not found.", id);
            return new ResourceNotFoundException("Đề án với id:" + id + " không tìm thấy.");
        });
    }

    @Transactional
    public ResponseData<CreateAdmissionResponse> createAdmission(CreateAdmissionAndMethodsAndMajorsRequest request)
            throws ResourceNotFoundException, DataExistedException, StoreDataFailedException {
        Integer consultantId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        User consultant = userService.findById(consultantId);

        Admission admission = this.createAdmission(request.getYear(), request.getDocuments(), consultant);

        List<Major> majors = majorService.insertNewMajorsAndGetExistedMajors(request.getNewMajorsNames(), request.getMajorIds());

        List<Method> methods = methodService.insertNewMethodsAndGetExistedMethods(request.getNewMethodsNames(), request.getMethodIds());

        return ResponseData.ok("Tạo đề án thành công", new CreateAdmissionResponse(admission.getId(),
                admission.getYear(),
                methodService.toListInfoMethodDTO(methods),
                majorService.toListInfoMajorDTO(majors)
        ));
    }

    public FullAdmissionDTO mapFullResponse(Admission admission) {
        FullAdmissionDTO fullAdmissionDTO = modelMapper.map(admission, FullAdmissionDTO.class);

        if (admission.getUpdateBy() == null) {
            fullAdmissionDTO.setCreateBy(modelMapper.map(userService.findById(admission.getCreateBy()), ActionerDTO.class));
            fullAdmissionDTO.setUpdateBy(null);
        } else if (Objects.equals(admission.getCreateBy(), admission.getUpdateBy())) {
            ActionerDTO actionerDTO = modelMapper.map(userService.findById(admission.getCreateBy()), ActionerDTO.class);
            fullAdmissionDTO.setUpdateBy(actionerDTO);
            fullAdmissionDTO.setCreateBy(actionerDTO);
        } else {
            List<User> actioners = userService.findByIds(Arrays.asList(admission.getCreateBy(), admission.getUpdateBy()));
            fullAdmissionDTO.setUpdateBy(modelMapper.map(actioners.stream()
                    .filter(user -> user.getId().equals(admission.getUpdateBy()))
                    .findFirst(), ActionerDTO.class));
            fullAdmissionDTO.setCreateBy(modelMapper.map(actioners.stream()
                    .filter(user -> user.getId().equals(admission.getUpdateBy()))
                    .findFirst(), ActionerDTO.class));
        }
        return fullAdmissionDTO;
    }

    public Admission createAdmission(Integer year, String documents, User consultantAccount) {
        Admission admission = new Admission(year, documents, consultantAccount.getCreateBy(), consultantAccount.getId());
        return this.save(admission);
    }

//    public CreateAdmissionTrainingProgramResponse createAdmissionTrainingProgram(CreateAdmissionTrainingProgramRequest request) {
//        Admission admission = this.findById(request.getAdmissionId());
//
//        List<AdmissionTrainingProgram> admissionTrainingPrograms = admissionTrainingProgramService.saveAllAdmissionTrainingProgram(request.getAdmissionId(), request.getTrainingPrograms());
//
//        return new CreateAdmissionTrainingProgramResponse(this.mapFullResponse(admission),
//                admissionTrainingPrograms.stream()
//                        .map(admissionTrainingProgram -> modelMapper.map(admissionTrainingProgram, CreateTrainingProgramRequest.class))
//                        .toList());
//    }

//    public CreateAdmissionMethodResponse createAdmissionMethod(CreateAdmissionMethodRequest request) {
//        Admission admission = this.findById(request.getAdmissionId());
//
//        List<AdmissionMethod> admissionMethods = admissionMethodService.saveAllAdmissionMethod(request.getAdmissionId(), request.getMethodIds());
//
//        List<Method> methods = methodService.findByIds(request.getMethodIds());
//        return new CreateAdmissionMethodResponse(this.mapFullResponse(admission),
//                methods.stream()
//                        .map(admissionTrainingProgram -> modelMapper.map(admissionTrainingProgram, InfoMethodDTO.class))
//                        .toList());
//    }
//
//    public ResponseData<CreateAdmissionTrainingProgramMethodQuotaResponse> createAdmissionTrainingProgramMethodQuota(CreateAdmissionTrainingProgramMethodRequest request) {
//        CreateAdmissionTrainingProgramMethodQuotaResponse response = new CreateAdmissionTrainingProgramMethodQuotaResponse();
//        List<AdmissionTrainingProgramMethod> admissionTrainingProgramMethods = admissionTrainingProgramMethodService.createAdmissionTrainingProgramMethod(request);
//
//        response.setQuotas(admissionTrainingProgramMethods
//                .stream()
//                .map(admissionTrainingProgramMethod -> new AdmissionTrainingProgramMethodQuotaDTO(admissionTrainingProgramMethod.getId().getAdmissionTrainingProgramId(), admissionTrainingProgramMethod.getId().getAdmissionMethodId(), admissionTrainingProgramMethod.getQuota()))
//                .toList());
//        return ResponseData.ok("Tạo chi tiết chỉ tiêu thành công", response);
//    }

    public List<AdmissionTrainingProgramSubjectGroup> createAdmissionTrainingProgramSubjectGroup(List<CreateAdmissionQuotaRequest> request, List<AdmissionTrainingProgram> admissionTrainingPrograms, List<Major> majors) {
        List<AdmissionTrainingProgramSubjectGroup> admissionTrainingProgramSubjectGroups = new ArrayList<>();

        for (CreateAdmissionQuotaRequest quotaRequest : request) {
            Integer admissionTrainingProgramId = getAdmissionTrainingProgramId(quotaRequest, admissionTrainingPrograms, majors);

            List<AdmissionTrainingProgramSubjectGroup> admissionTrainingProgramSubjectGroupList = quotaRequest.getSubjectGroupIds().stream()
                    .map(subjectGroupId -> new AdmissionTrainingProgramSubjectGroup(admissionTrainingProgramId, subjectGroupId)).toList();

            admissionTrainingProgramSubjectGroups.addAll(admissionTrainingProgramSubjectGroupList);
        }

        List<AdmissionTrainingProgramSubjectGroup> result = admissionTrainingProgramSubjectGroupService.saveAll(admissionTrainingProgramSubjectGroups);

        return result;
    }

//    public CreateAdmissionTrainingProgramSubjectGroupResponse createAdmissionTrainingProgramSubjectGroup(CreateAdmissionTrainingProgramSubjectGroupRequest request) {
//        List<AdmissionTrainingProgramSubjectGroup> admissionTrainingProgramSubjectGroups = new ArrayList<>();
//        request.getSubjectGroupId().forEach(subjectGroupId -> admissionTrainingProgramSubjectGroups.add(new AdmissionTrainingProgramSubjectGroup(request.getAdmissionTrainingProgramId(), subjectGroupId)));
//
//        List<AdmissionTrainingProgramSubjectGroup> addList = admissionTrainingProgramSubjectGroupService.saveAll(admissionTrainingProgramSubjectGroups);
//
//        List<Integer> subjectGroupIds = addList.stream()
//                .map(subjectGroup -> subjectGroup.getId().getSubjectGroupId())
//                .toList();
//
//        List<SubjectGroup> subjectGroups = subjectGroupService.findAllByIds(subjectGroupIds);
//
//        CreateAdmissionTrainingProgramSubjectGroupResponse response = CreateAdmissionTrainingProgramSubjectGroupResponse.builder()
//                .admissionTrainingProgramId(request.getAdmissionTrainingProgramId())
//                .subjectGroupIds(subjectGroupIds)
//                .build();
//
//        return response;
//    }

    @Transactional
    public void createAdmission(CreateAdmissionRequest request) throws DataExistedException {
        List<AdmissionQuotaDTO> admissionQuotaDTOs = request.getQuotas().stream().map(AdmissionQuotaDTO::new).toList();
        User consultant = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());

//        Admission checkAdmission = admissionRepository.findByUniversityIdAndYearAndConfirmStatus(consultant.getCreateBy(), request.getYear(), AdmissionConfirmStatus.CONFIRMED).orElse(null);
        Admission checkAdmission = admissionRepository.findByUniversityIdAndYearAndAdmissionStatus(consultant.getCreateBy(), request.getYear(), AdmissionStatus.ACTIVE).orElse(null);
        if (checkAdmission != null) {
            throw new DataExistedException("Đã có đề án năm " + request.getYear() + " đang được active.");
        }

        Admission admission = this.createAdmission(request.getYear(), request.getDocuments(), consultant);

        List<Method> methods = methodService.findByIds(request.getQuotas().stream().map(CreateAdmissionQuotaRequest::getMethodId).distinct().toList());

        List<Major> majors = majorService.findByIds(request.getQuotas().stream().map(CreateAdmissionQuotaRequest::getMajorId).distinct().toList());

        List<AdmissionMethod> admissionMethods = admissionMethodService.saveAllAdmissionMethodWithListMethods(admission.getId(), methods);

        List<AdmissionTrainingProgram> admissionTrainingPrograms = admissionTrainingProgramService.saveAdmissionTrainingProgram(admission.getId(), request.getQuotas(), majors);

        List<AdmissionTrainingProgramMethod> admissionTrainingProgramMethod = setAdmissionTrainingProgramMethod(majors, methods, admissionTrainingPrograms, admissionMethods, request.getQuotas());

        List<AdmissionTrainingProgramSubjectGroup> admissionTrainingProgramSubjectGroups = createAdmissionTrainingProgramSubjectGroup(request.getQuotas(), admissionTrainingPrograms, majors);

    }

    public List<AdmissionTrainingProgramMethod> setAdmissionTrainingProgramMethod(List<Major> majors,
                                                                                  List<Method> methods,
                                                                                  List<AdmissionTrainingProgram> admissionTrainingPrograms,
                                                                                  List<AdmissionMethod> admissionMethods,
                                                                                  List<CreateAdmissionQuotaRequest> quotaRequests) {

        List<AdmissionTrainingProgramMethod> result = new ArrayList<>();

        for (CreateAdmissionQuotaRequest quotaRequest : quotaRequests) {

            AdmissionTrainingProgramMethod admissionTrainingProgramMethod = new AdmissionTrainingProgramMethod();

            Integer admissionMethodId = getAdmissionMethodId(quotaRequest, admissionMethods, methods);

            Integer admissionTrainingProgramId = getAdmissionTrainingProgramId(quotaRequest, admissionTrainingPrograms, majors);

            result.add(new AdmissionTrainingProgramMethod(admissionTrainingProgramId, admissionMethodId, quotaRequest.getQuota()));
        }


        return admissionTrainingProgramMethodService.saveAll(result);
    }

    private Integer getAdmissionMethodId(CreateAdmissionQuotaRequest quotaRequest,
                                         List<AdmissionMethod> admissionMethods,
                                         List<Method> methods) {
        log.info("Get admission method id by method id");
        return findAdmissionMethodIdByMethodId(quotaRequest.getMethodId(), admissionMethods);
    }

    private Integer findAdmissionMethodIdByMethodId(Integer methodId, List<AdmissionMethod> admissionMethods) {
        return admissionMethods.stream()
                .filter(ad -> ad.getMethodId().equals(methodId))
                .map(AdmissionMethod::getId)
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Admission method id not found"));
    }

    private Integer findAdmissionMethodIdByNameAndCode(String majorName,
                                                       String majorCode,
                                                       List<AdmissionMethod> admissionMethods,
                                                       List<Method> methods) {
        Method method = methods.stream()
                .filter(m -> m.getName().equals(majorName) && m.getCode().equals(majorCode))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Admission method id not found"));

        return admissionMethods.stream()
                .filter(ad -> ad.getMethodId().equals(method.getId()))
                .map(AdmissionMethod::getId)
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Admission method id not found"));
    }

    private Integer getAdmissionTrainingProgramId(CreateAdmissionQuotaRequest quotaRequest,
                                                  List<AdmissionTrainingProgram> admissionTrainingPrograms,
                                                  List<Major> majors) {
        log.info("Get admission method id by method id");
        AdmissionTrainingProgram admissionTrainingProgram = null;

        return admissionTrainingPrograms.stream()
                .filter(ad -> ad.getMajorId().equals(quotaRequest.getMajorId())
                        && (ad.getLanguage() == null ? quotaRequest.getLanguage() == null : quotaRequest.getLanguage() != null && quotaRequest.getLanguage().equals(ad.getLanguage()))
                        && (ad.getTrainingSpecific() == null ? quotaRequest.getTrainingSpecific() == null : quotaRequest.getTrainingSpecific() != null && quotaRequest.getTrainingSpecific().equals(ad.getTrainingSpecific()))
                )
                .map(AdmissionTrainingProgram::getId)
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Admission training program id not found"));
    }

    private Integer findAdmissionTrainingProgramIdByMajorId(Integer majorId,
                                                            List<AdmissionTrainingProgram> admissionTrainingPrograms) {
        return admissionTrainingPrograms.stream()
                .filter(ad -> ad.getMajorId().equals(majorId))
                .map(AdmissionTrainingProgram::getId)
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Admission training program id not found"));
    }

    private Integer findAdmissionTrainingProgramIdByNameAndCode(String majorName,
                                                                String majorCode,
                                                                List<AdmissionTrainingProgram> admissionTrainingPrograms,
                                                                List<Major> majors) {
        return admissionTrainingPrograms.stream()
                .filter(ad -> majors.stream()
                        .anyMatch(major -> major.getId().equals(ad.getMajorId()) && major.getName().equals(majorName) && major.getCode().equals(majorCode)))
                .map(AdmissionTrainingProgram::getId)
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Admission training program id not found"));
    }

    public ResponseData<Page<FullAdmissionDTO>> getBy(Pageable pageable,
                                                      Integer id,
                                                      Integer staffId,
                                                      Integer year,
                                                      String source,
                                                      Integer universityId,
                                                      Date createTime,
                                                      Integer createBy,
                                                      Integer updateBy,
                                                      Date updateTime,
                                                      AdmissionStatus status,
                                                      AdmissionScoreStatus scoreStatus,
                                                      List<AdmissionConfirmStatus> confirmStatus) {
        Page<Admission> admissions = null;
        try {
            if (confirmStatus != null){
                List<String> confirmStatusString = confirmStatus.stream().map(Enum::name).toList();
                admissions = admissionRepository.findAllBy(pageable, id, staffId, year, source, universityId, createTime, createBy, updateBy, updateTime, (status != null) ? status.name() : null, (scoreStatus != null) ? scoreStatus.name() : null, confirmStatusString);
            }
            else
                admissions = admissionRepository.findAllBy(pageable, id, staffId, year, source, universityId, createTime, createBy, updateBy, updateTime, (status != null) ? status.name() : null, (scoreStatus != null) ? scoreStatus.name() : null);
        } catch (Exception e) {
            throw new QueryException("Lỗi tìm kiếm.", Map.of("queryError", e.getCause().getMessage()));
        }

        if (admissions == null || admissions.isEmpty()) {
            throw new ResourceNotFoundException("Không tìm thấy đề án nào.");
        }
        List<ActionerDTO> actionerDTOs = this.getActioners(admissions.getContent());

        List<UniversityInfo> universityInfos = universityInfoServiceImpl.findByIds(admissions.getContent().stream().map(Admission::getUniversityId).toList());

        return ResponseData.ok("Lấy thông tin các đề án thành công.", admissions.map((element) -> this.mappingInfo(element, actionerDTOs, universityInfos)));
    }

    public ResponseData<Page<FullAdmissionDTO>> getByV2(Pageable pageable,
                                                        Integer id,
                                                        Integer staffId,
                                                        Integer year,
                                                        String source,
                                                        Integer universityId,
                                                        Date createTime,
                                                        Integer createBy,
                                                        Integer updateBy,
                                                        Date updateTime,
                                                        List<AdmissionStatus> status,
                                                        List<AdmissionScoreStatus> scoreStatus,
                                                        List<AdmissionConfirmStatus> confirmStatus) {
        Page<Admission> admissions = null;

        String countBuilder;
        StringBuilder queryBuilder = new StringBuilder(
                "SELECT a.* " +
                        "FROM admission a " +
                        "INNER JOIN university_info ui ON a.university_id = ui.university_id " +
                        "WHERE 1=1 "
        );

        // 2. Create a Map to store parameters
        Map<String, Object> parameters = new HashMap<>();

        // 3. Add conditions based on method parameters
        if (id != null) {
            queryBuilder.append(" AND a.id = :id ");
            parameters.put("id", id);
        }

        if (staffId != null) {
            queryBuilder.append(" AND ui.staff_id = :staffId ");
            parameters.put("staffId", staffId);
        }

        if (year != null) {
            queryBuilder.append(" AND a.year = :year ");
            parameters.put("year", year);
        }

        if (source != null && !source.isEmpty()) {
            queryBuilder.append(" AND a.source = :source ");
            parameters.put("source", source);
        }

        if (universityId != null) {
            queryBuilder.append(" AND a.university_id = :universityId ");
            parameters.put("universityId", universityId);
        }

        if (createTime != null) {
            queryBuilder.append(" AND a.create_time = :createTime ");
            parameters.put("createTime", createTime);
        }

        if (createBy != null) {
            queryBuilder.append(" AND a.create_by = :createBy ");
            parameters.put("createBy", createBy);
        }

        if (updateBy != null) {
            queryBuilder.append(" AND a.update_by = :updateBy ");
            parameters.put("updateBy", updateBy);
        }

        if (updateTime != null) {
            queryBuilder.append(" AND a.update_time = :updateTime ");
            parameters.put("updateTime", updateTime);
        }

        if (status != null && !status.isEmpty()) {
            queryBuilder.append(" AND a.status IN (:status) ");
            parameters.put("status", status.stream().map(Enum::name).toList());
        }

        if (scoreStatus != null && !scoreStatus.isEmpty()) {
            queryBuilder.append(" AND a.score_status IN (:scoreStatus) ");
            parameters.put("scoreStatus", scoreStatus.stream().map(Enum::name).toList());
        }

        if (confirmStatus != null && !confirmStatus.isEmpty()) {
            queryBuilder.append(" AND a.confirm_status IN (:confirmStatus) ");
            parameters.put("confirmStatus", confirmStatus.stream().map(Enum::name).toList());
        }

        countBuilder = queryBuilder.toString();

        if (pageable.getSort().isSorted()) {
            queryBuilder.append(" ORDER BY ");
            List<String> sortingCriteria = new ArrayList<>();
            pageable.getSort().forEach(order -> {
                sortingCriteria.add("a." + order.getProperty() + " " + order.getDirection().name());
            });
            queryBuilder.append(String.join(", ", sortingCriteria));
        }

        // 4. Create and execute the TypedQuery
        Query query = entityManager.createNativeQuery(queryBuilder.toString(), Admission.class);

        // 5. Set the parameters
        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }

        // 6. Handle pagination
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        // 7. Execute the query and get results
        var admissionList = query.getResultList();

        // 8. Get total count for pagination
        // You may want to create a count query for the total results based on the same criteria
        Query countQuery = entityManager.createNativeQuery("SELECT COUNT(*) FROM (" + countBuilder.toString() + ") as countQuery");
        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            countQuery.setParameter(entry.getKey(), entry.getValue());
        }
        Long total = ((Number) countQuery.getSingleResult()).longValue();

        admissions = new PageImpl<>(admissionList, pageable, total);

        if (admissions == null || admissions.isEmpty()) {
            throw new ResourceNotFoundException("Không tìm thấy đề án nào.");
        }
        List<ActionerDTO> actionerDTOs = this.getActioners(admissions.getContent());

        List<UniversityInfo> universityInfos = universityInfoServiceImpl.findByIds(admissions.getContent().stream().map(Admission::getUniversityId).toList());


        return ResponseData.ok("Lấy thông tin các đề án thành công.", admissions.map((element) -> this.mappingInfo(element, actionerDTOs, universityInfos)));
    }

    public ResponseData<List<String>> getSource(Integer year, String universityCode) {
        Optional<Admission> admissions = admissionRepository.findByYearAndUniversityCode(year, universityCode);

        if (admissions.isEmpty()) {
            throw new ResourceNotFoundException("Không tìm thấy đề án.");
        }

        return ResponseData.ok("Lấy tài liệu thành công.", Arrays.stream(admissions.get().getSource().split(";")).toList());
    }

    public ResponseData<List<AdmissionSourceDTO>> getSourceV2(Pageable pageable, List<Integer> year, List<String> universityId) {
        try {
            List<Admission> admissions;
            if ((year == null || year.isEmpty()) && (universityId == null || universityId.isEmpty())) {
                admissions = admissionRepository.findAllActiveWithPageable(pageable);
            } else if ((year == null || year.isEmpty())) {
                admissions = admissionRepository.findAllByListUniversityCode(pageable, universityId);
            } else if ((universityId == null || universityId.isEmpty())) {
                admissions = admissionRepository.findAllByListYear(pageable, year);
            } else
                admissions = admissionRepository.findAllByListYearAndListUniversityCode(pageable, year, universityId);

            if (admissions.isEmpty()) {
                throw new ResourceNotFoundException("Không tìm thấy đề án.");
            }
            List<UniversityInfo> universityInfos = universityInfoServiceImpl.findByIds(admissions.stream().map(Admission::getUniversityId).toList());

            return ResponseData.ok("Lấy tài liệu thành công.", admissions.stream().map(element -> new AdmissionSourceDTO(element, universityInfos)).toList());
        } catch (Exception e) {
            throw new QueryException("Lỗi tìm kiếm.", Map.of("queryError", e.getCause().getMessage()));
        }
    }

    public Page<SearchAdmissionDTO> searchAdmission(Pageable pageable, List<Integer> year, List<String> universityId, Integer staffId, List<AdmissionStatus> status) {
        try {
            List<String> statusString = (status == null || status.isEmpty()) ? null : status.stream().map(Enum::name).toList();
            Page<Admission> admissions;
            if (statusString == null || statusString.isEmpty()) {
                if ((year == null || year.isEmpty()) && (universityId == null || universityId.isEmpty())) {
                    admissions = admissionRepository.find(pageable, staffId);
                } else if ((year == null || year.isEmpty())) {
                    admissions = admissionRepository.findAllByListUniversityCodeV2(pageable, universityId, staffId);
                } else if ((universityId == null || universityId.isEmpty())) {
                    admissions = admissionRepository.findAllByListYearV2(pageable, year, staffId);
                } else
                    admissions = admissionRepository.findAllByListYearAndListUniversityCodeV2(pageable, year, universityId, staffId);
                if (admissions.isEmpty()) {
                    throw new ResourceNotFoundException("Không tìm thấy đề án.");
                }
            } else {
                if ((year == null || year.isEmpty()) && (universityId == null || universityId.isEmpty())) {
                    admissions = admissionRepository.findWithPageableAndListStatus(pageable, statusString, staffId);
                } else if ((year == null || year.isEmpty())) {
                    admissions = admissionRepository.findAllByListUniversityCodeAndListStatus(pageable, universityId,statusString, staffId);
                } else if ((universityId == null || universityId.isEmpty())) {
                    admissions = admissionRepository.findAllByListYearAndListStatus(pageable, year, statusString, staffId);
                } else
                    admissions = admissionRepository.findAllByListYearAndListUniversityCodeAndListStatus(pageable, year, universityId, statusString, staffId);
                if (admissions.isEmpty()) {
                    throw new ResourceNotFoundException("Không tìm thấy đề án.");
                }
            }
            List<UniversityInfo> universityInfos = universityInfoServiceImpl.findByIds(admissions.stream().map(Admission::getUniversityId).distinct().toList());

            Page<SearchAdmissionDTO> searchAdmissionDTOPage = new PageImpl<>(admissions.getContent().stream().map(element -> new SearchAdmissionDTO(element, universityInfos)).toList(), pageable, admissions.getTotalElements());
            return searchAdmissionDTOPage;
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new QueryException("Lỗi tìm kiếm.", Map.of("queryError", e.getCause().getMessage()));
        }
    }

    protected List<ActionerDTO> getActioners(List<Admission> admissions) {
        Set<Integer> actionerIds = admissions.stream()
                .flatMap(ad -> Stream.of(ad.getCreateBy(), ad.getUpdateBy()).filter(Objects::nonNull))
                .collect(Collectors.toSet());

        return userService.getActioners(actionerIds.stream().toList());
    }

    protected List<ActionerDTO> getActioners(Admission admission) {
        Set<Integer> actionerIds = Stream.of(admission.getCreateBy(), admission.getUpdateBy()).filter(Objects::nonNull).collect(Collectors.toSet());

        return userService.getActioners(actionerIds.stream().toList());
    }

    protected FullAdmissionDTO mappingInfo(Admission admission, List<ActionerDTO> actionerDTOs, List<UniversityInfo> universityInfos) {
        FullAdmissionDTO result = modelMapper.map(admission, FullAdmissionDTO.class);
        result.setStatus(admission.getAdmissionStatus().name);
        result.setScoreStatus(admission.getScoreStatus().name);
        result.setConfirmStatus(admission.getConfirmStatus().name);
        UniversityInfo universityInfo = universityInfos.stream().filter((ele) -> ele.getId().equals(admission.getUniversityId())).findFirst().orElseThrow(() -> new ResourceNotFoundException("University info not found"));
        result.setName("ĐỀ ÁN TUYỂN SINH NĂM " + admission.getYear() + " CỦA " + universityInfo.getName().toUpperCase());
        List<String> sources = Arrays.stream(admission.getSource().split(";")).toList();
        result.setSources(sources);
        if (admission.getUpdateBy() != null) {
            if (admission.getUpdateBy() == admission.getCreateBy()) {
                ActionerDTO actionerDTO = getCreateBy(admission, actionerDTOs);
                result.setUpdateBy(actionerDTO);
                result.setCreateBy(actionerDTO);
            } else {
                result.setUpdateBy(getUpdateBy(admission, actionerDTOs));
                result.setCreateBy(getCreateBy(admission, actionerDTOs));
            }
        } else {
            result.setUpdateBy(null);
            result.setCreateBy(getCreateBy(admission, actionerDTOs));
        }

        result.setUniversity(modelMapper.map(universityInfo, InfoUniversityResponseDTO.class));

        return result;
    }

    protected FullAdmissionDTO mappingInfo(Admission admission) {
        FullAdmissionDTO result = modelMapper.map(admission, FullAdmissionDTO.class);
        result.setStatus(admission.getAdmissionStatus().name);
        result.setScoreStatus(admission.getScoreStatus().name);
        UniversityInfo universityInfo = universityInfoRepository.findUniversityInfoByConsultantId(admission.getCreateBy());
        result.setCreateBy(new ActionerDTO(universityInfo.getId(), universityInfo.getName(), null, null));
        result.setName("ĐỀ ÁN TUYỂN SINH NĂM " + (admission.getYear() - 1) + "-" + admission.getYear() + " CỦA " + universityInfo.getName().toUpperCase());
        List<String> sources = Arrays.stream(admission.getSource().split(";")).toList();
        result.setSources(sources);

        result.setUniversity(null);

        return result;
    }

    protected AdmissionDetailDTO mappingInfoAdmissionDetail(Admission admission, UniversityInfo universityInfo) {
        AdmissionDetailDTO result = modelMapper.map(admission, AdmissionDetailDTO.class);
        result.setStatus(admission.getAdmissionStatus().name);
        result.setScoreStatus(admission.getScoreStatus().name);
        result.setName("ĐỀ ÁN TUYỂN SINH NĂM " + admission.getYear() + " CỦA " + universityInfo.getName().toUpperCase());
        return result;
    }

    protected FullAdmissionDTO mappingFull(Admission admission, List<AdmissionMethod> admissionMethods, List<AdmissionTrainingProgram> admissionTrainingPrograms, List<AdmissionTrainingProgramSubjectGroup> admissionTrainingProgramSubjectGroups, List<ActionerDTO> actionerDTOs, List<UniversityInfo> universityInfos, List<AdmissionTrainingProgramMethod> admissionTrainingProgramMethods)
            throws ResourceNotFoundException {
        FullAdmissionDTO result = this.mappingInfo(admission, actionerDTOs, universityInfos);

        List<Method> methods = methodService.findByIds(admissionMethods.stream().map(AdmissionMethod::getMethodId).toList());
        List<AdmissionMethodDTO> admissionMethodDTOS = admissionMethods.stream().map((element) -> new AdmissionMethodDTO(element, methods)).toList();
        result.setAdmissionMethods(admissionMethodDTOS);


        List<Major> majors = majorService.findByIds(admissionTrainingPrograms.stream().map(AdmissionTrainingProgram::getMajorId).distinct().toList());
        List<Subject> subjects = subjectServiceImpl.findByIds(admissionTrainingPrograms.stream().filter(Objects::nonNull).map(AdmissionTrainingProgram::getMainSubjectId).distinct().toList());
        List<AdmissionTrainingProgramDTO> admissionTrainingProgramDTOS = admissionTrainingPrograms.stream()
                .map((element) -> new AdmissionTrainingProgramDTO(element, subjects, majors))
                .toList();
        result.setAdmissionTrainingPrograms(admissionTrainingProgramDTOS);

        List<SubjectGroupResponseDTO2> subjectGroupDTOs = subjectGroupServiceImpl.getByAdmissionTrainingProgramIds(admissionTrainingPrograms.stream().map(AdmissionTrainingProgram::getId).toList());
        List<AdmissionTrainingProgramSubjectGroupDTO> admissionTrainingProgramSubjectGroupDTOS = new ArrayList<>();
        for (AdmissionTrainingProgram admissionTrainingProgram : admissionTrainingPrograms) {
            List<Integer> subjectGroupIds = admissionTrainingProgramSubjectGroups
                    .stream()
                    .filter(el -> el.getId().getAdmissionTrainingProgramId().equals(admissionTrainingProgram.getId()))
                    .map(el -> el.getId().getSubjectGroupId()).toList();

            List<SubjectGroupResponseDTO2> subjectGroupDTOs2 = new ArrayList<>();

            for (Integer subjectGroupId : subjectGroupIds) {
                subjectGroupDTOs2.add(subjectGroupDTOs.stream().filter((ob) -> ob.getId().equals(subjectGroupId)).findFirst().get());
            }

            admissionTrainingProgramSubjectGroupDTOS.add(AdmissionTrainingProgramSubjectGroupDTO.builder()
                    .admissionTrainingProgramId(admissionTrainingProgram.getId())
                    .subjectGroups(subjectGroupDTOs2)
                    .build());
        }
        result.setAdmissionTrainingProgramSubjectGroups(admissionTrainingProgramSubjectGroupDTOS);

        result.setDetails(admissionTrainingProgramMethods.stream().map((element) -> modelMapper.map(element, FullAdmissionQuotaDTO.class)).collect(Collectors.toList()));

        return result;
    }

    protected FullAdmissionDTO mappingFullWithNoUniInfo(Admission admission, List<AdmissionMethod> admissionMethods, List<AdmissionTrainingProgram> admissionTrainingPrograms, List<AdmissionTrainingProgramSubjectGroup> admissionTrainingProgramSubjectGroups, List<AdmissionTrainingProgramMethod> admissionTrainingProgramMethods, List<UniversityTrainingProgram> universityTrainingPrograms)
            throws ResourceNotFoundException {
        FullAdmissionDTO result = this.mappingInfo(admission);

        List<Method> methods = methodService.findByIds(admissionMethods.stream().map(AdmissionMethod::getMethodId).toList());
        List<AdmissionMethodDTO> admissionMethodDTOS = admissionMethods.stream().map((element) -> new AdmissionMethodDTO(element, methods)).toList();
        result.setAdmissionMethods(admissionMethodDTOS);


        List<Major> majors = majorService.findByIds(admissionTrainingPrograms.stream().map(AdmissionTrainingProgram::getMajorId).distinct().toList());

        List<Integer> subjectIds = admissionTrainingPrograms.stream()
                .filter(Objects::nonNull)
                .map(AdmissionTrainingProgram::getMainSubjectId)
                .filter(Objects::nonNull)
                .distinct()
                .toList();

        List<Subject> subjects;

        if (subjectIds.isEmpty()) {
            subjects = new ArrayList<>();
        } else {
            subjects = subjectServiceImpl.findByIds(subjectIds);
        }

        List<AdmissionTrainingProgramDTO> admissionTrainingProgramDTOS = admissionTrainingPrograms.stream()
                .map((element) -> new AdmissionTrainingProgramDTO(element, subjects, majors, universityTrainingPrograms))
                .toList();
        result.setAdmissionTrainingPrograms(admissionTrainingProgramDTOS);

        List<SubjectGroupResponseDTO2> subjectGroupDTOs = subjectGroupServiceImpl.getByAdmissionTrainingProgramIds(admissionTrainingPrograms.stream().map(AdmissionTrainingProgram::getId).toList());

        List<AdmissionTrainingProgramSubjectGroupDTO> admissionTrainingProgramSubjectGroupDTOS = new ArrayList<>();

        for (AdmissionTrainingProgram admissionTrainingProgram : admissionTrainingPrograms) {
            List<Integer> subjectGroupIds = admissionTrainingProgramSubjectGroups
                    .stream()
                    .filter(el -> el.getId().getAdmissionTrainingProgramId().equals(admissionTrainingProgram.getId()))
                    .map(el -> el.getId().getSubjectGroupId()).toList();

            List<SubjectGroupResponseDTO2> subjectGroupDTOs2 = new ArrayList<>();

            for (Integer subjectGroupId : subjectGroupIds) {
                subjectGroupDTOs2.add(subjectGroupDTOs.stream().filter((ob) -> ob.getId().equals(subjectGroupId)).findFirst().get());
            }

            admissionTrainingProgramSubjectGroupDTOS.add(AdmissionTrainingProgramSubjectGroupDTO.builder()
                    .admissionTrainingProgramId(admissionTrainingProgram.getId())
                    .subjectGroups(subjectGroupDTOs2)
                    .build());
        }
        result.setAdmissionTrainingProgramSubjectGroups(admissionTrainingProgramSubjectGroupDTOS);

        result.setDetails(admissionTrainingProgramMethods.stream().map((element) -> modelMapper.map(element, FullAdmissionQuotaDTO.class)).collect(Collectors.toList()));

        result.setTotalMethods(admissionMethods.size());

        result.setTotalMajors(admissionTrainingPrograms.stream().map(AdmissionTrainingProgram::getMajorId).distinct().toList().size());

        result.setTotalQuota(admissionTrainingProgramMethods.stream().mapToInt(AdmissionTrainingProgramMethod::getQuota).sum());

        List<ScoreRange> scoreRanges = getScoreRange(admissionMethods, admissionTrainingProgramMethods);

        result.setScoreRanges(scoreRanges);

        return result;
    }

    protected List<ScoreRange> getScoreRange(List<AdmissionMethod> admissionMethods, List<AdmissionTrainingProgramMethod> admissionTrainingProgramMethods) {
        List<ScoreRange> scoreRanges = new ArrayList<>();

        for (AdmissionMethod admissionMethod : admissionMethods) {
            List<AdmissionTrainingProgramMethod> admissionTrainingProgramMethods1 = admissionTrainingProgramMethods.stream()
                    .filter(ad -> ad.getId().getAdmissionMethodId().equals(admissionMethod.getId()))
                    .toList();
            Float smallestAdmissionScore = null, biggestAdmissionScore = null;

            if (admissionTrainingProgramMethods1.isEmpty()){
                scoreRanges.add(new ScoreRange(admissionMethod.getMethodId(), smallestAdmissionScore, biggestAdmissionScore));
                continue;
            }

            if (admissionTrainingProgramMethods1.size() == 1) {
                smallestAdmissionScore = admissionTrainingProgramMethods1.get(0).getAdmissionScore();
                biggestAdmissionScore = admissionTrainingProgramMethods1.get(0).getAdmissionScore();
                scoreRanges.add(new ScoreRange(admissionMethod.getMethodId(), smallestAdmissionScore, biggestAdmissionScore));
                continue;
            }

            smallestAdmissionScore = admissionTrainingProgramMethods1.stream()
                    .map(AdmissionTrainingProgramMethod::getAdmissionScore)
                    .filter(Objects::nonNull)
                    .min(Float::compareTo)
                    .orElse(null);

            biggestAdmissionScore = admissionTrainingProgramMethods1.stream()
                    .map(AdmissionTrainingProgramMethod::getAdmissionScore)
                    .filter(Objects::nonNull)
                    .max(Float::compareTo)
                    .orElse(null);

            scoreRanges.add(new ScoreRange(admissionMethod.getId(), smallestAdmissionScore, biggestAdmissionScore));
        }
        return scoreRanges;
    }

    protected AdmissionDetailDTO mappingFullAdmissionDetail(Admission admission, List<AdmissionMethod> admissionMethods, List<AdmissionTrainingProgram> admissionTrainingPrograms, List<AdmissionTrainingProgramSubjectGroup> admissionTrainingProgramSubjectGroups, UniversityInfo universityInfo, List<AdmissionTrainingProgramMethod> admissionTrainingProgramMethods)
            throws ResourceNotFoundException {
        AdmissionDetailDTO result = this.mappingInfoAdmissionDetail(admission, universityInfo);

        List<Method> methods = methodService.findByIds(admissionMethods.stream().map(AdmissionMethod::getMethodId).toList());
        List<AdmissionMethodDTO> admissionMethodDTOS = admissionMethods.stream().map((element) -> new AdmissionMethodDTO(element, methods)).toList();
        result.setAdmissionMethods(admissionMethodDTOS);


        List<Major> majors = majorService.findByIds(admissionTrainingPrograms.stream().map(AdmissionTrainingProgram::getMajorId).distinct().toList());
        List<Subject> subjects = subjectServiceImpl.findByIds(admissionTrainingPrograms.stream().filter(Objects::nonNull).map(AdmissionTrainingProgram::getMainSubjectId).distinct().toList());
        List<AdmissionTrainingProgramDTO> admissionTrainingProgramDTOS = admissionTrainingPrograms.stream()
                .map((element) -> new AdmissionTrainingProgramDTO(element, subjects, majors))
                .toList();
        result.setAdmissionTrainingPrograms(admissionTrainingProgramDTOS);

        List<SubjectGroupResponseDTO2> subjectGroupDTOs = subjectGroupServiceImpl.getByAdmissionTrainingProgramIds(admissionTrainingPrograms.stream().map(AdmissionTrainingProgram::getId).toList());
        List<AdmissionTrainingProgramSubjectGroupDTO> admissionTrainingProgramSubjectGroupDTOS = new ArrayList<>();
        for (AdmissionTrainingProgram admissionTrainingProgram : admissionTrainingPrograms) {
            List<Integer> subjectGroupIds = admissionTrainingProgramSubjectGroups
                    .stream()
                    .filter(el -> el.getId().getAdmissionTrainingProgramId().equals(admissionTrainingProgram.getId()))
                    .map(el -> el.getId().getSubjectGroupId()).toList();

            List<SubjectGroupResponseDTO2> subjectGroupDTOs2 = new ArrayList<>();

            for (Integer subjectGroupId : subjectGroupIds) {
                subjectGroupDTOs2.add(subjectGroupDTOs.stream().filter((ob) -> ob.getId().equals(subjectGroupId)).findFirst().get());
            }

            admissionTrainingProgramSubjectGroupDTOS.add(AdmissionTrainingProgramSubjectGroupDTO.builder()
                    .admissionTrainingProgramId(admissionTrainingProgram.getId())
                    .subjectGroups(subjectGroupDTOs2)
                    .build());
        }
        result.setAdmissionTrainingProgramSubjectGroups(admissionTrainingProgramSubjectGroupDTOS);

        result.setDetails(admissionTrainingProgramMethods.stream().map((element) -> modelMapper.map(element, FullAdmissionQuotaDTO.class)).collect(Collectors.toList()));

        return result;
    }

    protected ActionerDTO getCreateBy(Admission admission, List<ActionerDTO> actionerDTOs) {
        return actionerDTOs.stream().filter(ac -> ac.getId().equals(admission.getCreateBy()))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Admission create by id not found"));
    }

    protected ActionerDTO getUpdateBy(Admission admission, List<ActionerDTO> actionerDTOs) {
        return actionerDTOs.stream().filter(ac -> ac.getId().equals(admission.getUpdateBy()))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Admission create by id not found"));
    }

    public ResponseData<FullAdmissionDTO> getById(Integer id)
            throws ResourceNotFoundException {
        Admission admission = this.findById(id);

        List<ActionerDTO> actionerDTOs = this.getActioners(admission);

        List<AdmissionMethod> admissionMethods = admissionMethodService.findByAdmissionId(admission.getId());

        List<AdmissionTrainingProgram> admissionTrainingPrograms = admissionTrainingProgramService.findByAdmissionId(admission.getId());

        List<AdmissionTrainingProgramSubjectGroup> admissionTrainingProgramSubjectGroups = admissionTrainingProgramSubjectGroupService.findByAdmissionTrainingProgramId(admissionTrainingPrograms.stream().map(AdmissionTrainingProgram::getId).collect(Collectors.toList()));

        List<AdmissionTrainingProgramMethod> admissionTrainingProgramMethods = admissionTrainingProgramMethodService.findByAdmissionTrainingProgramIds(admissionTrainingPrograms.stream().map(AdmissionTrainingProgram::getId).collect(Collectors.toList()));

        List<UniversityInfo> universityInfos = universityInfoServiceImpl.findByIds(Collections.singletonList(admission.getUniversityId()));

        return ResponseData.ok("Lấy thông tin các đề án thành công.", this.mappingFull(admission, admissionMethods, admissionTrainingPrograms, admissionTrainingProgramSubjectGroups, actionerDTOs, universityInfos, admissionTrainingProgramMethods));
    }

    public ResponseData<FullAdmissionDTO> getByIdV2(Integer id)
            throws ResourceNotFoundException {
        Admission admission = this.findById(id);

        List<AdmissionMethod> admissionMethods = admissionMethodService.findByAdmissionId(admission.getId());

        List<AdmissionTrainingProgram> admissionTrainingPrograms = admissionTrainingProgramService.findByAdmissionId(admission.getId());

        List<AdmissionTrainingProgramSubjectGroup> admissionTrainingProgramSubjectGroups = admissionTrainingProgramSubjectGroupService.findByAdmissionTrainingProgramId(admissionTrainingPrograms.stream().map(AdmissionTrainingProgram::getId).collect(Collectors.toList()));

        List<AdmissionTrainingProgramMethod> admissionTrainingProgramMethods = admissionTrainingProgramMethodService.findByAdmissionTrainingProgramIds(admissionTrainingPrograms.stream().map(AdmissionTrainingProgram::getId).collect(Collectors.toList()));

        Integer totalQuota = admissionTrainingProgramMethods.stream().mapToInt(AdmissionTrainingProgramMethod::getQuota).sum();

        List<UniversityTrainingProgram> universityTrainingPrograms = universityTrainingProgramService.findByUniversityId(admission.getUniversityId());

        FullAdmissionDTO result = this.mappingFullWithNoUniInfo(admission, admissionMethods, admissionTrainingPrograms, admissionTrainingProgramSubjectGroups, admissionTrainingProgramMethods, universityTrainingPrograms) ;
        return ResponseData.ok("Lấy thông tin các đề án thành công.", result);
    }

    @Transactional
    public ResponseData updateAdmissionScore(UpdateAdmissionScoreRequest request) {
        List<Integer> admissionTrainingProgramIds = request.getAdmissionScores().stream().map(AdmissionScoreDTO::getAdmissionTrainingProgramId).distinct().toList();
        List<Integer> admissionMethodIds = request.getAdmissionScores().stream().map(AdmissionScoreDTO::getAdmissionMethodId).distinct().toList();

        List<AdmissionMethod> admissionMethods = admissionMethodService.findByIds(admissionMethodIds);
        List<AdmissionTrainingProgram> admissionTrainingPrograms = admissionTrainingProgramService.findByIds(admissionTrainingProgramIds);

        Set<Integer> admissionIds = new HashSet<>();
        admissionIds.addAll(admissionMethods.stream().map(AdmissionMethod::getAdmissionId).toList());
        admissionIds.addAll(admissionTrainingPrograms.stream().map(AdmissionTrainingProgram::getAdmissionId).toList());

        if ((admissionIds.size() != 1))
            throw new BadRequestException("Các giá trị thuộc các đề án khác nhau");

        Integer admissionId = admissionIds.iterator().next();

        Admission admission = findById(admissionId);
        User user = ServiceUtils.getUser();
        if (user.getRole().equals(Role.UNIVERSITY) && !admission.getUniversityId().equals(user.getId()))
            throw new NotAllowedException("Bạn không có quyền thực hiện chức năng này");
        if (user.getRole().equals(Role.CONSULTANT) && !admission.getUniversityId().equals(user.getCreateBy()))
            throw new NotAllowedException("Bạn không có quyền thực hiện chức năng này");

        List<AdmissionTrainingProgramMethodId> admissionTrainingProgramMethodIds = request.getAdmissionScores().stream().map(AdmissionTrainingProgramMethodId::new).toList();

        List<AdmissionTrainingProgramMethod> admissionTrainingProgramMethods = admissionTrainingProgramMethodService.findByAdmissionId(admissionTrainingProgramMethodIds, true);

        for (AdmissionTrainingProgramMethod admissionTrainingProgramMethod : admissionTrainingProgramMethods) {

            AdmissionScoreDTO admissionScoreDTO = null;
            for (AdmissionScoreDTO admissionScore : request.getAdmissionScores()) {
                if (admissionScore.getAdmissionMethodId().equals(admissionTrainingProgramMethod.getId().getAdmissionMethodId()) && admissionScore.getAdmissionTrainingProgramId().equals(admissionTrainingProgramMethod.getId().getAdmissionTrainingProgramId())) {
                    admissionScoreDTO = admissionScore;
                }
            }

            if (admissionScoreDTO == null) {
                throw new ResourceNotFoundException("Admission score not found");
            }

            admissionTrainingProgramMethod.setAdmissionScore(admissionScoreDTO.getAdmissionScore());
        }
        admissionTrainingProgramMethodService.saveAll(admissionTrainingProgramMethods);
        updateAdmissionScoreStatuses(admissionId);
        return ResponseData.ok("Cập nhật điểm thành công.");
    }

    protected void updateUniversityTrainingProgram(User user, Integer universityId, Admission admission) {
        if (admission.getAdmissionStatus().equals(AdmissionStatus.INACTIVE)){
            admission = admissionRepository.findFirstByUniversityIdAndAdmissionStatusAndYearLessThanEqualOrderByYearDesc(universityId, AdmissionStatus.ACTIVE, LocalDate.now().getYear())
                    .orElse(null);
        }

        if (admission == null){
            universityTrainingProgramService.inactiveAll(universityId, user.getId());
            return;
        }

        List<AdmissionTrainingProgram> admissionTrainingPrograms = admissionTrainingProgramService.findByAdmissionId(admission.getId());
        universityTrainingProgramService.createFromAdmission(admissionTrainingPrograms, universityId, user.getId());
    }

    public void universityAndConsultantUpdateAdmissionStatusAndUpdateUniversityTrainingProgram(Integer id, UpdateAdmissionStatusRequest request) throws NotAllowedException, BadRequestException, StoreDataFailedException{
        User user = ServiceUtils.getUser();
        Admission ad = findById(id);
        
        List<Admission> admissions = admissionRepository.findByUniversityIdAndYearAndAdmissionStatusV2(ad.getUniversityId(), ad.getYear(), AdmissionStatus.ACTIVE);

        if (admissions.size() > 1 || (admissions.size() == 1 && !admissions.get(0).getId().equals(ad.getId()))){
            throw new BadRequestException("Đề án khác cùng năm đã được kích hoạt.", Map.of("admissionId", admissions.get(0).getId().toString()));
        }

        ad = universityAndConsultantUpdateAdmissionStatus(id, request, user, ad);
        updateUniversityTrainingProgram(user, ad.getUniversityId(), ad);
    }

    public Admission universityAndConsultantUpdateAdmissionStatus(Integer id, UpdateAdmissionStatusRequest request, User user, Admission admission){
        Integer uniId = null;
        ConsultantInfo consultantInfo = null;
        if (admission.getAdmissionStatus().equals(AdmissionStatus.STAFF_INACTIVE)){
            throw new NotAllowedException("Đề án này đã bị ngưng hoạt động bới staff.");
        }
        if (user.getRole().equals(Role.UNIVERSITY)) {
            if (!admission.getUniversityId().equals(user.getId()))
                throw new NotAllowedException("Bạn không có quyền thực hiện hành động này.");
            uniId = user.getId();
        }

        if (user.getRole().equals(Role.CONSULTANT)) {
            consultantInfo = consultantInfoServiceImpl.findById(user.getId());
            uniId = consultantInfo.getUniversityId();
            if (!admission.getUniversityId().equals(consultantInfo.getUniversityId())) {
                throw new NotAllowedException("Bạn không có quyền thực hiện hành động này.");
            }
        }

        if (request.getStatus().equals(AdmissionStatus.ACTIVE)) {
            if (!admission.getConfirmStatus().equals(AdmissionConfirmStatus.CONFIRMED))
                throw new NotAllowedException("Đề án này phải được chấp nhận trước.");

            if (user.getRole().equals(Role.UNIVERSITY)) {
                Admission otherAdmission = admissionRepository.findByUniversityIdAndYearAndAdmissionStatus(user.getId(), admission.getYear(), AdmissionStatus.ACTIVE)
                        .orElse(null);
                if (otherAdmission != null)
                    throw new BadRequestException("Đề án khác cùng năm đã được kích hoạt.", Map.of("admissionId", otherAdmission.getId().toString()));
            } else {
                if (consultantInfo == null)
                    consultantInfo = consultantInfoServiceImpl.findById(user.getId());
                Admission otherAdmission = admissionRepository.findByUniversityIdAndYearAndAdmissionStatus(consultantInfo.getUniversityId(), admission.getYear(), AdmissionStatus.ACTIVE)
                        .orElse(null);
                if (otherAdmission != null)
                    throw new BadRequestException("Đề án khác cùng năm đã được kích hoạt.", Map.of("admissionId", otherAdmission.getId().toString()));
            }
        }

        if (request.getStatus().equals(AdmissionStatus.PENDING))
            throw new BadRequestException("Không thể chuyển đề án về trạng thái chờ kích hoạt.");

        admission.setAdmissionStatus(request.getStatus());
        admission.setNote(request.getNote());
        admission.setUpdateBy(user.getId());
        admission.setUpdateTime(new Date());

        try {
            return admissionRepository.save(admission);
        } catch (Exception e) {
            throw new StoreDataFailedException("Cập nhật thông tin đề án thất bại.", Map.of("error", e.getCause().getMessage()));
        }
    }

    public void staffUpdateAdmissionConfirmStatusAndUpdateUniversityTrainingProgram(Integer id, UpdateAdmissionConfirmStatusRequest request) throws NotAllowedException, BadRequestException, StoreDataFailedException{
        User user = ServiceUtils.getUser();
        Admission ad = findById(id);
        Admission existedWithTheSameYear = admissionRepository.findByUniversityIdAndYearAndAdmissionStatus(ad.getUniversityId(), ad.getYear(), AdmissionStatus.ACTIVE).orElse(null);
        if (existedWithTheSameYear != null){
            throw new BadRequestException("Đề án khác cùng năm đã được kích hoạt.", Map.of("admissionId", existedWithTheSameYear.getId().toString()));
        }
        ad = staffUpdateAdmissionConfirmStatus(id, request, user, ad);
        updateUniversityTrainingProgram(user, ad.getUniversityId(), ad);
    }

    public Admission staffUpdateAdmissionConfirmStatus(Integer id, UpdateAdmissionConfirmStatusRequest request, User user, Admission admission) throws NotAllowedException, BadRequestException, StoreDataFailedException {

        UniversityInfo universityInfo = universityInfoServiceImpl.findById(admission.getUniversityId());
        if (request.getConfirmStatus() != null){
            if (!universityInfo.getStaffId().equals(user.getId()))
                throw new NotAllowedException("Bạn không có quyền thực hiện hành động này.");

            if (request.getConfirmStatus().equals(AdmissionConfirmStatus.CONFIRMED)) {
                if (admission.getConfirmStatus().equals(AdmissionConfirmStatus.CONFIRMED)) {
                    throw new BadRequestException("Đề án đã được chấp nhận.");
                } else if (!admission.getConfirmStatus().equals(AdmissionConfirmStatus.PENDING)) {
                    throw new BadRequestException("Đề án không ở trạng thái chờ kích hoạt.");
                }
            }

            if (request.getConfirmStatus().equals(AdmissionConfirmStatus.REJECTED) && admission.getConfirmStatus().equals(AdmissionConfirmStatus.REJECTED))
                throw new BadRequestException("Đề án đã bị hủy.");

            if (request.getConfirmStatus().equals(AdmissionConfirmStatus.REJECTED)){
                admission.setConfirmStatus(AdmissionConfirmStatus.REJECTED);
                admission.setAdmissionStatus(AdmissionStatus.INACTIVE);
            }

            if (request.getConfirmStatus().equals(AdmissionConfirmStatus.PENDING)){
                admission.setConfirmStatus(AdmissionConfirmStatus.REJECTED);
                admission.setAdmissionStatus(AdmissionStatus.INACTIVE);
            }

            if (request.getConfirmStatus().equals(AdmissionConfirmStatus.CONFIRMED)){
                admission.setConfirmStatus(AdmissionConfirmStatus.CONFIRMED);
                admission.setAdmissionStatus(AdmissionStatus.ACTIVE);
            }
        }
        else {
            if (request.getAdmissionStatus() == null)
                throw new BadRequestException("Trạng thái không được để trống.");
            if (request.getAdmissionStatus().equals(AdmissionStatus.STAFF_INACTIVE)){
                admission.setAdmissionStatus(AdmissionStatus.STAFF_INACTIVE);
            }
            if (request.getAdmissionStatus().equals(AdmissionStatus.INACTIVE)){
                admission.setAdmissionStatus(AdmissionStatus.INACTIVE);
            }
            if (request.getAdmissionStatus().equals(AdmissionStatus.ACTIVE)){
                if (!admission.getConfirmStatus().equals(AdmissionConfirmStatus.CONFIRMED))
                    throw new BadRequestException("Đề án này phải được chấp nhận trước.");
                admission.setAdmissionStatus(AdmissionStatus.ACTIVE);
            }
        }

        admission.setNote(request.getNote());
        admission.setUpdateBy(user.getId());
        admission.setUpdateTime(new Date());

        try {
            return admissionRepository.save(admission);
        } catch (Exception e) {
            throw new StoreDataFailedException("Cập nhật thông tin đề án thất bại.", Map.of("error", e.getCause().getMessage()));
        }
    }

    @Transactional
    public ResponseData updateAdmission(Integer admissionId, UpdateAdmissionRequest request) {
        Admission admission = this.findById(admissionId);
        User user = ServiceUtils.getUser();
        validateAdmissionOwnership(user, admission);
        if (admission.getConfirmStatus().equals(AdmissionConfirmStatus.PENDING))
            updateNotAcceptedAdmission(admission, request, user);
        if (admission.getConfirmStatus().equals(AdmissionConfirmStatus.CONFIRMED))
            updateAcceptedAdmission(admission, request, user);
        return null;
    }

    private void updateAcceptedAdmission(Admission admission, UpdateAdmissionRequest request, User user) {

    }

    public void updateNotAcceptedAdmission(Admission admission, UpdateAdmissionRequest request, User user){
        boolean admissionModified = false;
        Integer modifiedCount = 0;

        if (request.getYear() != null && !request.getYear().equals(admission.getYear())) {
            admission.setYear(request.getYear());
            admissionModified = true;
        }

        if (request.getUpdateAdmissionTrainingProgramRequest() != null)
            modifiedCount += admissionTrainingProgramService.update(admission, request.getUpdateAdmissionTrainingProgramRequest());

        if (request.getUpdateAdmissionMethodRequest() != null)
            modifiedCount += admissionMethodService.update(admission, request.getUpdateAdmissionMethodRequest());

        if (request.getUpdateAdmissionTrainingMethodRequest() != null)
            modifiedCount += admissionTrainingProgramMethodService.update(admission, request.getUpdateAdmissionTrainingMethodRequest());

        if (request.getUpdateAdmissionSubjectGroupRequest() != null)
            modifiedCount += admissionTrainingProgramSubjectGroupService.update(admission, request.getUpdateAdmissionSubjectGroupRequest());


        if (admissionModified){
            admissionRepository.save(admission);
            modifiedCount += 1;
        }
    }

    public void validateAdmissionOwnership(User user, Admission admission) {
        boolean isUniversity = user.getRole().equals(Role.UNIVERSITY);
        boolean isConsultant = user.getRole().equals(Role.CONSULTANT);

        if (isUniversity && !admission.getUniversityId().equals(user.getId())) {
            throw new NotAllowedException("Bạn không có quyền thực hiện hành động này.");
        }

        if (isConsultant && !admission.getUniversityId().equals(user.getCreateBy())) {
            throw new NotAllowedException("Bạn không có quyền thực hiện hành động này.");
        }

        if (!isUniversity && !isConsultant) {
            throw new NotAllowedException("Bạn không có quyền thực hiện hành động này.");
        }
    }

    public void validateAdmissionOwnershipV2(User user, Admission admission) {
        switch (user.getRole()) {
            case UNIVERSITY:
                if (!admission.getUniversityId().equals(user.getId())) {
                    throw new NotAllowedException("Bạn không có quyền thực hiện hành động này.");
                }
                break;
            case CONSULTANT:
                if (!admission.getUniversityId().equals(user.getCreateBy())) {
                    throw new NotAllowedException("Bạn không có quyền thực hiện hành động này.");
                }
                break;
            case STAFF:
                User university = userService.findById(admission.getUniversityId());
                if (!university.getCreateBy().equals(user.getId())) {
                    throw new NotAllowedException("Bạn không có quyền thực hiện hành động này.");
                }
                break;
            default:
                throw new NotAllowedException("Bạn không có quyền thực hiện hành động này.");
        }
    }

    public AdmissionDetailDTO getAdmissionScoreDetail(Integer year, String universityCode) {
        Admission admission = admissionRepository.findByYearAndUniversityCode(year, universityCode).orElseThrow(() -> new ResourceNotFoundException("Hiện không có đề án phù hợp."));

        List<AdmissionMethod> admissionMethods = admissionMethodService.findByAdmissionId(admission.getId());

        List<AdmissionTrainingProgram> admissionTrainingPrograms = admissionTrainingProgramService.findByAdmissionId(admission.getId());

        List<AdmissionTrainingProgramSubjectGroup> admissionTrainingProgramSubjectGroups = admissionTrainingProgramSubjectGroupService.findByAdmissionTrainingProgramId(admissionTrainingPrograms.stream().map(AdmissionTrainingProgram::getId).collect(Collectors.toList()));

        List<AdmissionTrainingProgramMethod> admissionTrainingProgramMethods = admissionTrainingProgramMethodService.findByAdmissionTrainingProgramIds(admissionTrainingPrograms.stream().map(AdmissionTrainingProgram::getId).collect(Collectors.toList()));

        UniversityInfo universityInfos = universityInfoServiceImpl.findById(admission.getUniversityId());

        return this.mappingFullAdmissionDetail(admission, admissionMethods, admissionTrainingPrograms, admissionTrainingProgramSubjectGroups, universityInfos, admissionTrainingProgramMethods);
    }

    public AdmissionDetailDTO getAdmissionScoreDetail(Admission admission) {
        List<AdmissionMethod> admissionMethods = admissionMethodService.findByAdmissionId(admission.getId());

        List<AdmissionTrainingProgram> admissionTrainingPrograms = admissionTrainingProgramService.findByAdmissionId(admission.getId());

        List<AdmissionTrainingProgramSubjectGroup> admissionTrainingProgramSubjectGroups = admissionTrainingProgramSubjectGroupService.findByAdmissionTrainingProgramId(admissionTrainingPrograms.stream().map(AdmissionTrainingProgram::getId).collect(Collectors.toList()));

        List<AdmissionTrainingProgramMethod> admissionTrainingProgramMethods = admissionTrainingProgramMethodService.findByAdmissionTrainingProgramIds(admissionTrainingPrograms.stream().map(AdmissionTrainingProgram::getId).collect(Collectors.toList()));

        UniversityInfo universityInfos = universityInfoServiceImpl.findById(admission.getUniversityId());

        return this.mappingFullAdmissionDetail(admission, admissionMethods, admissionTrainingPrograms, admissionTrainingProgramSubjectGroups, universityInfos, admissionTrainingProgramMethods);
    }

    public GetLatestTrainingProgramResponse getLatestTrainingProgramByUniversityId(Integer universityId) {
        User user = userService.findById(universityId);
        if (!user.getRole().equals(Role.UNIVERSITY)) {
            throw new ResourceNotFoundException("Không có trường học.", Map.of("universityId", universityId.toString()));
        }
        Admission admission = admissionRepository.findFirstByUniversityIdAndAdmissionStatusOrderByYearDesc(universityId, AdmissionStatus.ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException("Trường bạn tìm hiện tại chưa có đề án nào."));
        List<Major> majors = majorService.findByAdmissionId(admission.getId());
        return new GetLatestTrainingProgramResponse(admission.getYear(), majors.stream().map((element) -> modelMapper.map(element, InfoMajorDTO.class)).toList());
    }

    public ResponseData adviceSchool(SchoolAdviceRequest request) {
        Integer year = request.getYear();
        List<AdmissionTrainingProgramMethod> admissionTrainingProgramMethods = admissionTrainingProgramMethodService.findAdmissionData(
                request.getSubjectGroupId(),
                request.getScore(),
                request.getOffset(),
                request.getMethodId(),
                request.getMajorId(),
                request.getProvinceId(),
                year);

        if (admissionTrainingProgramMethods.isEmpty())
            throw new ResourceNotFoundException("Không tìm thấy chương trình đào tạo phù hợp.");
        log.info(request.toString());
        List<Integer> admissionTrainingProgramIds = admissionTrainingProgramMethods
                .stream()
                .map(AdmissionTrainingProgramMethod::getId)
                .map(AdmissionTrainingProgramMethodId::getAdmissionTrainingProgramId)
                .distinct().toList();
        List<Integer> admissionMethodIds = admissionTrainingProgramMethods
                .stream()
                .map(AdmissionTrainingProgramMethod::getId)
                .map(AdmissionTrainingProgramMethodId::getAdmissionMethodId)
                .distinct().toList();


        List<AdmissionTrainingProgram> admissionTrainingPrograms = admissionTrainingProgramService.findByIds(admissionTrainingProgramIds);
        List<AdmissionMethod> admissionMethods = admissionMethodService.findByIds(admissionMethodIds);
        List<Integer> methodIds = admissionMethods.stream().map(AdmissionMethod::getMethodId).distinct().toList();
        List<Method> methods = methodService.findByIds(methodIds);
        List<Admission> admissions = admissionRepository.findAllById(admissionTrainingPrograms.stream().map(AdmissionTrainingProgram::getAdmissionId).distinct().toList());
        List<UniversityInfo> universityInfos = universityInfoServiceImpl.findByIds(admissions.stream().map(Admission::getUniversityId).distinct().toList());
        List<UniversityTrainingProgram> universityTrainingPrograms = universityTrainingProgramService.findByUniversityIds(universityInfos.stream().map(UniversityInfo::getId).distinct().toList());
        List<AdmissionTrainingProgramSubjectGroup> admissionTrainingProgramSubjectGroups = admissionTrainingProgramSubjectGroupService.findByAdmissionTrainingProgramId(admissionTrainingProgramIds);
        List<SubjectGroup> subjectGroups = subjectGroupService.findAllByIds(admissionTrainingProgramSubjectGroups
                .stream()
                .map(AdmissionTrainingProgramSubjectGroup::getId)
                .map(AdmissionTrainingProgramSubjectGroupId::getSubjectGroupId)
                .distinct().toList());
        List<Major> majors = majorService.findByIds(admissionTrainingPrograms.stream().map(AdmissionTrainingProgram::getMajorId).distinct().toList());

        List<Integer> universityIds = admissions.stream().map(Admission::getUniversityId).distinct().toList();
//        List<InfoMajorDTO> infoMajors = majors.stream().map((element) -> modelMapper.map(element, InfoMajorDTO.class)).toList();
//        List<InfoUniversityResponseDTO> infoUniversityResponses = universityInfos
//                .stream()
//                .map((element) -> modelMapper.map(element, InfoUniversityResponseDTO.class))
//                .toList();
//        List<SubjectGroupResponseDTO2> subjectGroupResponses = subjectGroups.stream().map((element) -> modelMapper.map(element, SubjectGroupResponseDTO2.class)).toList();
        List<SchoolAdviceDTO> schoolAdviceDTOs = new ArrayList<>();


        for (Integer universityId : universityIds) {

            UniversityInfo universityInfo = universityInfos
                    .stream()
                    .filter((element) -> element.getId().equals(universityId))
                    .findFirst()
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy thông tin trường đại học."));

            List<Admission> admissions1 = admissions
                    .stream()
                    .filter((element) -> element.getUniversityId().equals(universityId))
                    .sorted(Comparator.comparing(Admission::getId).reversed())
                    .toList();

            List<AdmissionTrainingProgram> admissionTrainingPrograms1 = admissionTrainingPrograms
                    .stream()
                    .filter((element) -> admissions1.stream().map(Admission::getId).toList().contains(element.getAdmissionId()))
                    .toList();

            List<Integer> admissionTrainingProgramIds1 = admissionTrainingPrograms1.stream().map(AdmissionTrainingProgram::getId).toList();

            List<Major> majors1 = majors.stream().filter((ele) -> admissionTrainingPrograms.stream().map(AdmissionTrainingProgram::getMajorId).distinct().toList().contains(ele.getId())).distinct().toList();

            List<Integer> subjectGroupIds = admissionTrainingProgramSubjectGroups
                    .stream()
                    .map(AdmissionTrainingProgramSubjectGroup::getId)
                    .filter(id -> admissionTrainingProgramIds1.contains(id.getAdmissionTrainingProgramId()))
                    .map(AdmissionTrainingProgramSubjectGroupId::getSubjectGroupId)
                    .toList();

            List<SubjectGroup> subjectGroups1 = subjectGroups.stream().filter((element) -> subjectGroupIds.contains(element.getId())).toList();

            List<UniversityTrainingProgram> universityTrainingPrograms1 = universityTrainingPrograms.stream().filter((element) -> element.getUniversityId().equals(universityId)).toList();

            List<AdmissionTrainingProgramSubjectGroup> admissionTrainingProgramSubjectGroups1 = admissionTrainingProgramSubjectGroups
                    .stream()
                    .filter((element) -> admissionTrainingProgramIds1.contains(element.getId().getAdmissionTrainingProgramId()))
                    .toList();

            List<AdmissionTrainingProgramMethod> admissionTrainingProgramMethods1 = admissionTrainingProgramMethods
                    .stream()
                    .filter((element) -> admissionTrainingProgramIds1.contains(element.getId().getAdmissionTrainingProgramId()))
                    .toList();

            List<AdmissionTrainingProgramDTOV2> admissionTrainingProgramDTOV2s = getSchoolAdviceMajorDetails(universityId,
                    majors1,
                    admissions1,
                    admissionTrainingPrograms1,
                    admissionTrainingProgramSubjectGroups1,
                    admissionTrainingProgramMethods1,
                    subjectGroups1,
                    year,
                    admissionMethods,
                    methods, null,
                    universityTrainingPrograms1);

            Admission admission = admissions1.stream()
                    .max(Comparator.comparing(Admission::getYear).reversed()).get();

            Set<String> seen = new LinkedHashSet<>();

            Integer count = admissionTrainingPrograms1.stream()
                    .filter(program -> seen.add(
                            program.getMajorId() + "-" +
                                    (program.getMainSubjectId() != null ? program.getMainSubjectId() : "null") + "-" +
                                    (program.getLanguage() != null ? program.getLanguage() : "null") + "-" +
                                    (program.getTrainingSpecific() != null ? program.getTrainingSpecific() : "null")
                    ))
                    .toList().size();
            if (admission != null){
                schoolAdviceDTOs.add(
                        new SchoolAdviceDTO(modelMapper.map(universityInfo, InfoUniversityResponseDTO.class),
                                admission.getSource(),
                                count,
                                admissionTrainingProgramDTOV2s,
                                year));
            }

        }

        if (schoolAdviceDTOs.isEmpty()) {
            throw new ResourceNotFoundException("Không tìm thấy chương trình đào tạo phù hợp.");
        }

        return ResponseData.ok("Tìm kiếm các chương trình đào tạo phù hợp thành công", schoolAdviceDTOs);
    }

    public ResponseData<Page<SchoolAdviceDTO>> adviceSchoolV2(SchoolAdviceRequestV2 request) {
        Integer year = request.getYear();
        List<AdmissionTrainingProgramMethod> admissionTrainingProgramMethods = admissionTrainingProgramMethodService.findAdmissionTrainingPrograms(
                request.getSubjectId(),
                request.getFromScore(),
                request.getToScore(),
                request.getMethodId(),
                request.getMajorId(),
                request.getProvinceId(),
                year,
                request.getRegion(),
                request.getPageNumber(),
                request.getRowsPerPage()
        );

        Integer countAll = admissionTrainingProgramMethodService.count(
                request.getSubjectId(),
                request.getFromScore(),
                request.getToScore(),
                request.getMethodId(),
                request.getMajorId(),
                request.getProvinceId(),
                year,
                request.getRegion(),
                null,
                null
        );

        if (admissionTrainingProgramMethods.isEmpty())
            throw new ResourceNotFoundException("Không tìm thấy chương trình đào tạo phù hợp.");
        log.info(request.toString());
        List<Integer> admissionTrainingProgramIds = admissionTrainingProgramMethods
                .stream()
                .map(AdmissionTrainingProgramMethod::getId)
                .map(AdmissionTrainingProgramMethodId::getAdmissionTrainingProgramId)
                .distinct().toList();

        List<Integer> admissionMethodIds = admissionTrainingProgramMethods
                .stream()
                .map(AdmissionTrainingProgramMethod::getId)
                .map(AdmissionTrainingProgramMethodId::getAdmissionMethodId)
                .distinct().toList();


        List<AdmissionTrainingProgram> admissionTrainingPrograms = admissionTrainingProgramService.findByIds(admissionTrainingProgramIds);
        List<AdmissionMethod> admissionMethods = admissionMethodService.findByIds(admissionMethodIds);
        List<Integer> methodIds = admissionMethods.stream().map(AdmissionMethod::getMethodId).distinct().toList();
        List<Method> methods = methodService.findByIds(methodIds);
        List<Admission> admissions = admissionRepository.findAllById(admissionTrainingPrograms.stream().map(AdmissionTrainingProgram::getAdmissionId).distinct().toList());
        List<UniversityInfo> universityInfos = universityInfoServiceImpl.findByIds(admissions.stream().map(Admission::getUniversityId).distinct().toList());
        List<AdmissionTrainingProgramSubjectGroup> admissionTrainingProgramSubjectGroups = admissionTrainingProgramSubjectGroupService.findByAdmissionTrainingProgramId(admissionTrainingProgramIds);
        List<UniversityTrainingProgram> universityTrainingPrograms = universityTrainingProgramService.findByUniversityIds(universityInfos.stream().map(UniversityInfo::getId).distinct().toList());
        List<SubjectGroup> subjectGroups = subjectGroupService.findSubjectGroup(request.getSubjectId(), admissionTrainingProgramIds);
        List<Major> majors = majorService.findByIds(admissionTrainingPrograms.stream().map(AdmissionTrainingProgram::getMajorId).distinct().toList());
        List<UniversityCampus> universityCampuses = universityCampusService.findHeadQuarterByUniversityIds(universityInfos.stream().map(UniversityInfo::getId).distinct().toList());
        List<Province> provinces = addressServiceImpl.findProvinceByIds(universityCampuses.stream().map(UniversityCampus::getProvinceId).distinct().toList());

        List<Integer> universityIds = admissions.stream().map(Admission::getUniversityId).distinct().toList();
//        List<InfoMajorDTO> infoMajors = majors.stream().map((element) -> modelMapper.map(element, InfoMajorDTO.class)).toList();
//        List<InfoUniversityResponseDTO> infoUniversityResponses = universityInfos
//                .stream()
//                .map((element) -> modelMapper.map(element, InfoUniversityResponseDTO.class))
//                .toList();
//        List<SubjectGroupResponseDTO2> subjectGroupResponses = subjectGroups.stream().map((element) -> modelMapper.map(element, SubjectGroupResponseDTO2.class)).toList();
        List<SchoolAdviceDTO> schoolAdviceDTOs = new ArrayList<>();


        List<Integer> majorIds = admissionTrainingPrograms.stream().map(AdmissionTrainingProgram::getMajorId).distinct().toList();
        List<Integer> provinceIds = provinces.stream().map(Province::getId).distinct().toList();

        for (Integer universityId : universityIds) {

            UniversityInfo universityInfo = universityInfos
                    .stream()
                    .filter((element) -> element.getId().equals(universityId))
                    .findFirst()
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy thông tin trường đại học."));

            UniversityCampus universityCampus = universityCampuses
                    .stream()
                    .filter((element) -> element.getUniversityId().equals(universityId))
                    .findFirst()
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy cơ sở chính của trường đại học."));

            Province province = provinces
                    .stream()
                    .filter((element) -> element.getId().equals(universityCampus.getProvinceId()))
                    .findFirst()
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy tỉnh thành của trường đại học."));

            List<Admission> admissions1 = admissions
                    .stream()
                    .filter((element) -> element.getUniversityId().equals(universityId))
                    .sorted(Comparator.comparing(Admission::getId).reversed())
                    .toList();

            List<AdmissionTrainingProgram> admissionTrainingPrograms1 = admissionTrainingPrograms
                    .stream()
                    .filter((element) -> admissions1.stream().map(Admission::getId).toList().contains(element.getAdmissionId()))
                    .toList();

            List<Integer> admissionTrainingProgramIds1 = admissionTrainingPrograms1.stream().map(AdmissionTrainingProgram::getId).toList();

            List<Major> majors1 = majors
                    .stream()
                    .filter((ele) -> admissionTrainingPrograms1
                            .stream()
                            .map(AdmissionTrainingProgram::getMajorId)
                            .distinct()
                            .toList()
                            .contains(ele.getId()))
                    .distinct()
                    .toList();

            List<AdmissionTrainingProgramSubjectGroup> admissionTrainingProgramSubjectGroups1 = admissionTrainingProgramSubjectGroups
                    .stream()
                    .filter((element) -> admissionTrainingProgramIds1.contains(element.getId().getAdmissionTrainingProgramId()))
                    .toList();

            List<UniversityTrainingProgram> universityTrainingPrograms1 = universityTrainingPrograms.stream().filter((element) -> element.getUniversityId().equals(universityId)).toList();

            List<Integer> subjectGroupIds = admissionTrainingProgramSubjectGroups1
                    .stream()
                    .map(AdmissionTrainingProgramSubjectGroup::getId)
                    .filter(id -> admissionTrainingProgramIds1.contains(id.getAdmissionTrainingProgramId()))
                    .map(AdmissionTrainingProgramSubjectGroupId::getSubjectGroupId)
                    .toList();

            List<SubjectGroup> subjectGroups1 = subjectGroups.stream().filter((element) -> subjectGroupIds.contains(element.getId())).toList();

            List<AdmissionTrainingProgramMethod> admissionTrainingProgramMethods1 = admissionTrainingProgramMethods
                    .stream()
                    .filter((element) -> admissionTrainingProgramIds1.contains(element.getId().getAdmissionTrainingProgramId()))
                    .toList();

            Set<String> seen = new LinkedHashSet<>();
            admissionTrainingPrograms1.forEach(program -> seen.add(
                    program.getMajorId() + "-" +
                            (program.getMainSubjectId() != null ? program.getMainSubjectId() : "null") + "-" +
                            (program.getLanguage() != null ? program.getLanguage() : "null") + "-" +
                            (program.getTrainingSpecific() != null ? program.getTrainingSpecific() : "null")
            ));

            List<AdmissionTrainingProgramDTOV2> admissionTrainingProgramDTOV2s = getSchoolAdviceMajorDetails(universityId,
                    majors1,
                    admissions1,
                    admissionTrainingPrograms1,
                    admissionTrainingProgramSubjectGroups1,
                    admissionTrainingProgramMethods1,
                    subjectGroups1,
                    year,
                    admissionMethods,
                    methods, seen,
                    universityTrainingPrograms1);

            Admission admission = admissions1.stream()
                    .max(Comparator.comparing(Admission::getYear).reversed()).get();

            if (admission != null){
                schoolAdviceDTOs.add(
                        new SchoolAdviceDTO(InfoUniversityResponseDTO.fromEntity(universityInfo, province),
                                admission.getSource(),
                                seen.size(),
                                admissionTrainingProgramDTOV2s,
                                year));
            }
        }

        if (schoolAdviceDTOs.size() < 1) {
            throw new ResourceNotFoundException("Không tìm thấy chương trình đào tạo phù hợp.");
        }

        Page<SchoolAdviceDTO> result = new PageImpl<>(schoolAdviceDTOs, PageRequest.of(request.getPageNumber(), request.getRowsPerPage()), countAll);

        return ResponseData.ok("Tìm kiếm các chương trình đào tạo phù hợp thành công", result);
    }



    public ResponseData<Page<SchoolAdviceDTO>> adviceSchoolV3(SchoolAdviceRequestV3 request) {
        Integer year = request.getYear();
        List<AdmissionTrainingProgramMethod> admissionTrainingProgramMethods = admissionTrainingProgramMethodService.findAdmissionTrainingProgramsV2(
                request.getSubjectId(),
                request.getFromScore(),
                request.getToScore(),
                request.getMethodId(),
                request.getMajorId(),
                request.getProvinceId(),
                year,
                request.getRegion(),
                request.getPageNumber(),
                request.getRowsPerPage()
        );

        Integer countAll = admissionTrainingProgramMethodService.countV2(
                request.getSubjectId(),
                request.getFromScore(),
                request.getToScore(),
                request.getMethodId(),
                request.getMajorId(),
                request.getProvinceId(),
                year,
                request.getRegion(),
                null,
                null
        );

        if (admissionTrainingProgramMethods.isEmpty())
            throw new ResourceNotFoundException("Không tìm thấy chương trình đào tạo phù hợp.");
        log.info(request.toString());
        List<Integer> admissionTrainingProgramIds = admissionTrainingProgramMethods
                .stream()
                .map(AdmissionTrainingProgramMethod::getId)
                .map(AdmissionTrainingProgramMethodId::getAdmissionTrainingProgramId)
                .distinct().toList();

        List<Integer> admissionMethodIds = admissionTrainingProgramMethods
                .stream()
                .map(AdmissionTrainingProgramMethod::getId)
                .map(AdmissionTrainingProgramMethodId::getAdmissionMethodId)
                .distinct().toList();


        List<AdmissionTrainingProgram> admissionTrainingPrograms = admissionTrainingProgramService.findByIds(admissionTrainingProgramIds);
        List<AdmissionMethod> admissionMethods = admissionMethodService.findByIds(admissionMethodIds);
        List<Integer> methodIds = admissionMethods.stream().map(AdmissionMethod::getMethodId).distinct().toList();
        List<Method> methods = methodService.findByIds(methodIds);
        List<Admission> admissions = admissionRepository.findAllById(admissionTrainingPrograms.stream().map(AdmissionTrainingProgram::getAdmissionId).distinct().toList());
        List<UniversityInfo> universityInfos = universityInfoServiceImpl.findByIds(admissions.stream().map(Admission::getUniversityId).distinct().toList());
        List<AdmissionTrainingProgramSubjectGroup> admissionTrainingProgramSubjectGroups = admissionTrainingProgramSubjectGroupService.findByAdmissionTrainingProgramId(admissionTrainingProgramIds);
        List<UniversityTrainingProgram> universityTrainingPrograms = universityTrainingProgramService.findByUniversityIds(universityInfos.stream().map(UniversityInfo::getId).distinct().toList());
        List<SubjectGroup> subjectGroups = subjectGroupService.findSubjectGroup(request.getSubjectId(), admissionTrainingProgramIds);
        List<Major> majors = majorService.findByIds(admissionTrainingPrograms.stream().map(AdmissionTrainingProgram::getMajorId).distinct().toList());
        List<UniversityCampus> universityCampuses = universityCampusService.findHeadQuarterByUniversityIds(universityInfos.stream().map(UniversityInfo::getId).distinct().toList());
        List<Province> provinces = addressServiceImpl.findProvinceByIds(universityCampuses.stream().map(UniversityCampus::getProvinceId).distinct().toList());
        List<District> districts = districtServiceImpl.findByIds(universityCampuses.stream().map(UniversityCampus::getDistrictId).distinct().toList());
        List<Ward> wards = wardServiceImpl.findByIds(universityCampuses.stream().map(UniversityCampus::getWardId).distinct().toList());

        List<Integer> universityIds = admissions.stream().map(Admission::getUniversityId).distinct().toList();
//        List<InfoMajorDTO> infoMajors = majors.stream().map((element) -> modelMapper.map(element, InfoMajorDTO.class)).toList();
//        List<InfoUniversityResponseDTO> infoUniversityResponses = universityInfos
//                .stream()
//                .map((element) -> modelMapper.map(element, InfoUniversityResponseDTO.class))
//                .toList();
//        List<SubjectGroupResponseDTO2> subjectGroupResponses = subjectGroups.stream().map((element) -> modelMapper.map(element, SubjectGroupResponseDTO2.class)).toList();
        List<SchoolAdviceDTO> schoolAdviceDTOs = new ArrayList<>();


        List<Integer> majorIds = admissionTrainingPrograms.stream().map(AdmissionTrainingProgram::getMajorId).distinct().toList();
        List<Integer> provinceIds = provinces.stream().map(Province::getId).distinct().toList();

        for (Integer universityId : universityIds) {

            UniversityInfo universityInfo = universityInfos
                    .stream()
                    .filter((element) -> element.getId().equals(universityId))
                    .findFirst()
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy thông tin trường đại học."));

            UniversityCampus universityCampus = universityCampuses
                    .stream()
                    .filter((element) -> element.getUniversityId().equals(universityId))
                    .findFirst()
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy cơ sở chính của trường đại học."));

            Province province = provinces
                    .stream()
                    .filter((element) -> element.getId().equals(universityCampus.getProvinceId()))
                    .findFirst()
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy tỉnh thành của trường đại học."));

            District district = districts
                    .stream()
                    .filter((element) -> element.getId().equals(universityCampus.getDistrictId()))
                    .findFirst()
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy quận huyện của trường đại học."));

            Ward ward = wards
                    .stream()
                    .filter((element) -> element.getId().equals(universityCampus.getWardId()))
                    .findFirst()
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy phường xã của trường đại học."));

            List<Admission> admissions1 = admissions
                    .stream()
                    .filter((element) -> element.getUniversityId().equals(universityId))
                    .sorted(Comparator.comparing(Admission::getId).reversed())
                    .toList();

            List<AdmissionTrainingProgram> admissionTrainingPrograms1 = admissionTrainingPrograms
                    .stream()
                    .filter((element) -> admissions1.stream().map(Admission::getId).toList().contains(element.getAdmissionId()))
                    .toList();

            List<Integer> admissionTrainingProgramIds1 = admissionTrainingPrograms1.stream().map(AdmissionTrainingProgram::getId).toList();

            List<Major> majors1 = majors
                    .stream()
                    .filter((ele) -> admissionTrainingPrograms1
                            .stream()
                            .map(AdmissionTrainingProgram::getMajorId)
                            .distinct()
                            .toList()
                            .contains(ele.getId()))
                    .distinct()
                    .toList();

            List<AdmissionTrainingProgramSubjectGroup> admissionTrainingProgramSubjectGroups1 = admissionTrainingProgramSubjectGroups
                    .stream()
                    .filter((element) -> admissionTrainingProgramIds1.contains(element.getId().getAdmissionTrainingProgramId()))
                    .toList();

            List<UniversityTrainingProgram> universityTrainingPrograms1 = universityTrainingPrograms.stream().filter((element) -> element.getUniversityId().equals(universityId)).toList();

            List<Integer> subjectGroupIds = admissionTrainingProgramSubjectGroups1
                    .stream()
                    .map(AdmissionTrainingProgramSubjectGroup::getId)
                    .filter(id -> admissionTrainingProgramIds1.contains(id.getAdmissionTrainingProgramId()))
                    .map(AdmissionTrainingProgramSubjectGroupId::getSubjectGroupId)
                    .toList();

            List<SubjectGroup> subjectGroups1 = subjectGroups.stream().filter((element) -> subjectGroupIds.contains(element.getId())).toList();

            List<AdmissionTrainingProgramMethod> admissionTrainingProgramMethods1 = admissionTrainingProgramMethods
                    .stream()
                    .filter((element) -> admissionTrainingProgramIds1.contains(element.getId().getAdmissionTrainingProgramId()))
                    .toList();

            Set<String> seen = new LinkedHashSet<>();
            admissionTrainingPrograms1.forEach(program -> seen.add(
                    program.getMajorId() + "-" +
                            (program.getMainSubjectId() != null ? program.getMainSubjectId() : "null") + "-" +
                            (program.getLanguage() != null ? program.getLanguage() : "null") + "-" +
                            (program.getTrainingSpecific() != null ? program.getTrainingSpecific() : "null")
            ));

            List<AdmissionTrainingProgramDTOV2> admissionTrainingProgramDTOV2s = getSchoolAdviceMajorDetails(universityId,
                    majors1,
                    admissions1,
                    admissionTrainingPrograms1,
                    admissionTrainingProgramSubjectGroups1,
                    admissionTrainingProgramMethods1,
                    subjectGroups1,
                    year,
                    admissionMethods,
                    methods, seen,
                    universityTrainingPrograms1);

            Admission admission = admissions1.stream()
                    .max(Comparator.comparing(Admission::getYear).reversed()).get();

            if (admission != null){
                schoolAdviceDTOs.add(
                        new SchoolAdviceDTO(InfoUniversityResponseDTO.fromEntity(universityInfo, province, district, ward, universityCampus),
                                admission.getSource(),
                                seen.size(),
                                admissionTrainingProgramDTOV2s,
                                year));
            }
        }

        if (schoolAdviceDTOs.size() < 1) {
            throw new ResourceNotFoundException("Không tìm thấy chương trình đào tạo phù hợp.");
        }

        Page<SchoolAdviceDTO> result = new PageImpl<>(schoolAdviceDTOs, PageRequest.of(request.getPageNumber(), request.getRowsPerPage()), countAll);

        return ResponseData.ok("Tìm kiếm các chương trình đào tạo phù hợp thành công", result);
    }

    public List<AdmissionTrainingProgramDTOV2> getSchoolAdviceMajorDetails(Integer universityId,
                                                                           List<Major> majors,
                                                                           List<Admission> admissions,
                                                                           List<AdmissionTrainingProgram> admissionTrainingPrograms,
                                                                           List<AdmissionTrainingProgramSubjectGroup> admissionTrainingProgramSubjectGroups,
                                                                           List<AdmissionTrainingProgramMethod> admissionTrainingProgramMethods,
                                                                           List<SubjectGroup> subjectGroups,
                                                                           Integer year,
                                                                           List<AdmissionMethod> admissionMethods,
                                                                           List<Method> methods, Set<String> seen,
                                                                           List<UniversityTrainingProgram> universityTrainingPrograms) {

        List<AdmissionTrainingProgramDTOV2> admissionTrainingProgramDTOV2s = new ArrayList<>();

        for (String se : seen) {

            List<AdmissionTrainingProgram> filteredList = admissionTrainingPrograms.stream().filter((element) -> {
                String[] fields = se.split("-");
                Integer majorId = Integer.parseInt(fields[0]);
                Integer mainSubjectId = "null".equals(fields[1]) ? null : Integer.parseInt(fields[1]);
                String language = "null".equals(fields[2]) ? null : fields[2];
                String trainingSpecific = "null".equals(fields[3]) ? null : fields[3];
                return element.getMajorId().equals(majorId) &&
                        (element.getMainSubjectId() == null ? mainSubjectId == null : element.getMainSubjectId().equals(mainSubjectId)) &&
                        (element.getLanguage() == null ? language == null : element.getLanguage().equals(language)) &&
                        (element.getTrainingSpecific() == null ? trainingSpecific == null : element.getTrainingSpecific().equals(trainingSpecific));
            }).toList();

            Integer majorId = filteredList.stream().map(AdmissionTrainingProgram::getMajorId).findFirst().orElse(null);

            Major major = majors.stream().filter((element) -> element.getId().equals(majorId)).findFirst().orElse(null);

            InfoMajorDTO majorDTO = modelMapper.map(major, InfoMajorDTO.class);
            SchoolAdviceMajorDetailDTO mapping = new SchoolAdviceMajorDetailDTO();
            mapping.setMajor(modelMapper.map(major, InfoMajorDTO.class));

            for (AdmissionTrainingProgram admissionTrainingProgram : filteredList) {
                Admission admission = admissions.stream().filter((element) -> element.getId().equals(admissionTrainingProgram.getAdmissionId())).findFirst().orElse(null);

                if (admissionTrainingProgram == null) break;

                List<AdmissionTrainingProgramMethod> admissionTrainingProgramMethods2 = admissionTrainingProgramMethods
                        .stream()
                        .filter((ele) -> ele.getId().getAdmissionTrainingProgramId().equals(admissionTrainingProgram.getId()))
                        .toList();

                for (AdmissionTrainingProgramMethod admissionTrainingProgramMethod: admissionTrainingProgramMethods2){
                    List<Integer> subjectGroupIds = admissionTrainingProgramSubjectGroups
                            .stream()
                            .map(AdmissionTrainingProgramSubjectGroup::getId)
                            .filter(id -> id.getAdmissionTrainingProgramId().equals(admissionTrainingProgram.getId()))
                            .map(AdmissionTrainingProgramSubjectGroupId::getSubjectGroupId)
                            .toList();

                    List<SubjectGroup> subjectGroups1 = subjectGroups.stream().filter((a) -> subjectGroupIds.contains(a.getId())).toList();

                    AdmissionMethod admissionMethod = admissionMethods.stream().filter((ele) -> ele.getId().equals(admissionTrainingProgramMethod.getId().getAdmissionMethodId())).findFirst().orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Admission Method."));

                    Method foundMethod = methods.stream().filter((ele) -> ele.getId().equals(admissionMethod.getMethodId())).findFirst().orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy phương thức tuyển sinh."));

                    AdmissionTrainingProgramScoreDTO admissionTrainingProgramScoreDTO = new AdmissionTrainingProgramScoreDTO(
                            admission.getYear(),
                            foundMethod.getName(),
                            foundMethod.getId(),
                            subjectGroups1.stream().map((element) -> modelMapper.map(element, SubjectGroupResponseDTO2.class)).collect(Collectors.toList()),
                            admissionTrainingProgramMethod.getAdmissionScore(),
                            admissionTrainingProgramMethod.getQuota()
                    );

                    AdmissionTrainingProgramDTOV2 admissionTrainingProgramDTOV2 = null;
                    for (AdmissionTrainingProgramDTOV2 element : admissionTrainingProgramDTOV2s) {

                        boolean isLanguageEqual = (element.getLanguage() == null)
                                ? admissionTrainingProgram.getLanguage() == null
                                : element.getLanguage().equals(admissionTrainingProgram.getLanguage());

                        boolean isTrainingSpecificEqual = (element.getTrainingSpecific() == null)
                                ? admissionTrainingProgram.getTrainingSpecific() == null
                                : element.getTrainingSpecific().equals(admissionTrainingProgram.getTrainingSpecific());

                        if (element.getUniversityId().equals(admission.getUniversityId())
                                && element.getMajor().getId().equals(majorDTO.getId())
                                && isLanguageEqual
                                && isTrainingSpecificEqual) {
                            admissionTrainingProgramDTOV2 = element;
                            break;
                        }
                    }


                    if (admissionTrainingProgramDTOV2 == null) {
                        admissionTrainingProgramDTOV2s.add(new AdmissionTrainingProgramDTOV2(admission,
                                modelMapper.map(majorDTO, InfoMajorDTO.class),
                                new ArrayList<>(List.of(admissionTrainingProgramScoreDTO)),
                                admissionTrainingProgram.getTrainingSpecific(),
                                admissionTrainingProgram.getLanguage(),
                                universityTrainingPrograms));
                    } else {
                        Integer index = admissionTrainingProgramDTOV2s.indexOf(admissionTrainingProgramDTOV2);
                        if (index == -1 ) throw new ResourceNotFoundException("Không tìm thấy chương trình đào tạo.");
                        List<AdmissionTrainingProgramScoreDTO> list = admissionTrainingProgramDTOV2.getScore();
                        list.add(admissionTrainingProgramScoreDTO);
                        admissionTrainingProgramDTOV2.setScore(list);
                        admissionTrainingProgramDTOV2s.set(index, admissionTrainingProgramDTOV2);
                    }
                }
            }
        }

        return admissionTrainingProgramDTOV2s;
    }

    public SchoolAdviceMajorDetailDTO mappingToSchoolAdviceMajorDetailDTO(Major major, List<SubjectGroup> subjectGroups1, List<SubjectGroup> subjectGroups2, Float score1, Float score2) {
        return new SchoolAdviceMajorDetailDTO(
                modelMapper.map(major, InfoMajorDTO.class),
                subjectGroups1.stream().map((element) -> modelMapper.map(element, SubjectGroupResponseDTO2.class)).toList(),
                subjectGroups2.stream().map((element) -> modelMapper.map(element, SubjectGroupResponseDTO2.class)).toList(),
                score1,
                score2);
    }

    protected Integer getAdmissionYearPlace(List<Admission> admissions, Admission admission) {
        return (int) admissions.stream().takeWhile((ele) -> !ele.getYear().equals(admission.getYear())).count();
    }

    public void updateAdmissionScoreStatuses() {
        List<Admission> admissions = admissionRepository.findAll();

        for (Admission admission : admissions) {
            updateScoreStatus(admission);
        }

        admissionRepository.saveAll(admissions);
    }

    public void updateAdmissionScoreStatuses(Integer id) {
        Admission admission = this.findById(id);

        updateScoreStatus(admission);

        admissionRepository.save(admission);
    }

    private void updateScoreStatus(Admission admission) {
        List<AdmissionTrainingProgramMethod> trainingProgramMethods =
                admissionTrainingProgramMethodService.findByAdmissionId(admission.getId());

        if (trainingProgramMethods.isEmpty()) {
            admission.setScoreStatus(AdmissionScoreStatus.EMPTY);
            return; // Exit early for efficiency
        }

        long incomplete = trainingProgramMethods.stream()
                .filter(element -> element.getAdmissionScore() == null)
                .count();

        if (incomplete == 0) {
            admission.setScoreStatus(AdmissionScoreStatus.COMPLETE);
        } else if (incomplete == trainingProgramMethods.size()) {
            admission.setScoreStatus(AdmissionScoreStatus.EMPTY);
        } else {
            admission.setScoreStatus(AdmissionScoreStatus.PARTIAL);
        }
    }

    public GetAdmissionScoreResponse getAdmissionScoreResponse(Pageable pageable, List<Integer> year, List<String> universityId) throws SQLException {
        List<Admission> admissions = null;
        if (universityId != null)
            universityId = universityId.stream().distinct().toList();

        if (year == null && universityId == null) {
            try {
                admissions = admissionRepository.find(pageable, null).getContent();
            } catch (Exception e) {
                throw new QueryException("Lỗi", Map.of("queryError", e.getCause().getMessage()));
            }
            if (admissions.isEmpty())
                throw new ResourceNotFoundException("Hiện đang không có đề án nào");
        } else if (universityId == null) {
            try {
                admissions = admissionRepository.findAllByListYear(pageable, year);
            } catch (Exception e) {
                throw new QueryException("Lỗi", Map.of("queryError", e.getCause().getMessage()));
            }
            if (admissions.isEmpty()){
                StringJoiner nam = new StringJoiner(",");
                for (Integer y : year) {
                    nam.add(y.toString());
                }
                throw new ResourceNotFoundException("Hiện đang không có đề án nào cho năm " + nam.toString());
            }
        } else if (year == null) {
            try {
                admissions = admissionRepository.findAllByListUniversityCode(pageable, universityId);
            } catch (Exception e) {
                throw new QueryException("Lỗi", Map.of("queryError", e.getCause().getMessage()));
            }
            if (admissions.isEmpty()){
                List<UniversityInfo> universityInfos = universityInfoServiceImpl.findByCodes(universityId);
                StringJoiner uniCode = new StringJoiner(",");
                if (universityInfos.size() > 1) {
                    for (UniversityInfo y : universityInfos) {
                        uniCode.add(y.getCode());
                    }
                }
                throw new ResourceNotFoundException("Hiện đang không có đề án nào cho trường với mã " + uniCode.toString());

            }
        } else {
            try {
                admissions = admissionRepository.findAllByListYearAndListUniversityCode(pageable, year, universityId);
            } catch (Exception e) {
                throw new QueryException("Lỗi", Map.of("queryError", e.getCause().getMessage()));
            }
            if (admissions.isEmpty()){
                throw new ResourceNotFoundException("Hiện đang không có đề án nào");
            }
        }

        if (admissions.size() > 1 && !admissions.isEmpty()) {
            List<UniversityInfo> universityInfos = universityInfoServiceImpl.findByIds(admissions.stream().map(Admission::getUniversityId).distinct().toList());
            return new GetAdmissionScoreResponse(admissions.stream().map(element -> new AdmissionWithUniversityInfoDTO(element, universityInfos)).toList());
        }
        return new GetAdmissionScoreResponse(getAdmissionScoreDetail(admissions.get(0)));
    }

    public void consultantUpdateAdmission(Integer id) {
        Admission admission = findById(id);
        admission.setConfirmStatus(AdmissionConfirmStatus.PENDING);
        admission.setUpdateTime(new Date());
        admission.setUpdateBy(ServiceUtils.getId());
    }

    public SchoolDirectoryInfoResponse schoolDirectory(SchoolDirectoryRequest schoolDirectoryRequest) {
        List<Admission> admissions = schoolDirectoryGetAdmission(schoolDirectoryRequest);

        List<Integer> admissionIds = admissions.stream().map(Admission::getId).toList();

        if (admissions.isEmpty()) {
            throw new ResourceNotFoundException("Không có trường đại học phù hợp.");
        }

        Integer countAll = countSchoolDirectoryGetAdmission(schoolDirectoryRequest);

        List<Integer> universityIds = admissions.stream().map(Admission::getUniversityId).toList();

        List<AdmissionTrainingProgram> admissionTrainingPrograms = null;

        if (schoolDirectoryRequest.getMajorIds() == null || schoolDirectoryRequest.getMajorIds().isEmpty()) {
            admissionTrainingPrograms = admissionTrainingProgramService.findByAdmissionIds(admissionIds);
        } else {
            admissionTrainingPrograms = admissionTrainingProgramService.findByAdmissionIdsAndMajorIds(admissionIds, schoolDirectoryRequest.getMajorIds());
        }

        List<AdmissionTrainingProgramSubjectGroup> admissionTrainingProgramSubjectGroups = null;
        if (schoolDirectoryRequest.getSubjectGroupIds() != null && !schoolDirectoryRequest.getSubjectGroupIds().isEmpty()) {
            admissionTrainingProgramSubjectGroups = admissionTrainingProgramSubjectGroupService.findByAdmissionTrainingProgramIdsAndSubjectGroupIds(admissionTrainingPrograms.stream().map(AdmissionTrainingProgram::getId).toList(), schoolDirectoryRequest.getSubjectGroupIds());
        }

        if (admissionTrainingProgramSubjectGroups != null && !admissionTrainingProgramSubjectGroups.isEmpty()) {
            List<Integer> admissionTrainingProgramIds = admissionTrainingProgramSubjectGroups.stream().map(AdmissionTrainingProgramSubjectGroup::getId).map(AdmissionTrainingProgramSubjectGroupId::getAdmissionTrainingProgramId).toList();

            admissionTrainingPrograms = admissionTrainingPrograms
                    .stream()
                    .filter((element) -> admissionTrainingProgramIds.contains(element.getId()))
                    .toList();
        }

        List<AdmissionTrainingProgramMethod> admissionTrainingProgramMethods = null;

        if (schoolDirectoryRequest.getMethodIds() != null && !schoolDirectoryRequest.getMethodIds().isEmpty()) {
            admissionTrainingProgramMethods = admissionTrainingProgramMethodService.findByAdmissionTrainingProgramIdInAndMethodIds(admissionTrainingPrograms.stream().map(AdmissionTrainingProgram::getId).toList(), schoolDirectoryRequest.getMethodIds());
        } else {
            admissionTrainingProgramMethods = admissionTrainingProgramMethodService.findByAdmissionTrainingProgramIds(admissionTrainingPrograms.stream().map(AdmissionTrainingProgram::getId).toList());
        }

        List<UniversityTrainingProgram> universityTrainingPrograms = universityTrainingProgramService.findByUniversityIdsWithStatus(universityIds, UniversityTrainingProgramStatus.ACTIVE);

        List<User> users = userService.findByIds(universityIds);

        List<Integer> admissionMethodIds1 = admissionTrainingProgramMethods
                .stream()
                .map(AdmissionTrainingProgramMethod::getId)
                .map(AdmissionTrainingProgramMethodId::getAdmissionMethodId)
                .distinct()
                .toList();

        List<AdmissionMethod> admissionMethods = admissionMethodService.findByIds(admissionMethodIds1);

        List<UniversityInfo> universityInfos = universityInfoServiceImpl.findByIds(universityIds);

        List<UniversityCampus> universityCampuses = universityCampusService.findByUniversityIds(universityIds);

        List<Province> provinces = addressServiceImpl.findProvinceByIds(universityCampuses.stream().map(UniversityCampus::getProvinceId).distinct().toList());
        List<District> districts = districtServiceImpl.findByIds(universityCampuses.stream().map(UniversityCampus::getDistrictId).distinct().toList());
        List<Ward> wards = wardServiceImpl.findByIds(universityCampuses.stream().map(UniversityCampus::getWardId).distinct().toList());

        List<Major> majors = majorService.findByIds(admissionTrainingPrograms
                .stream()
                .map(AdmissionTrainingProgram::getMajorId)
                .distinct()
                .toList());

        List<Method> methods = methodService.findByIds(admissionMethods
                .stream()
                .map(AdmissionMethod::getMethodId)
                .distinct()
                .toList());

        admissionTrainingProgramSubjectGroups = admissionTrainingProgramSubjectGroupService.findByAdmissionTrainingProgramId(admissionTrainingPrograms.stream().map(AdmissionTrainingProgram::getId).toList());

        List<SubjectGroup> subjectGroups = subjectGroupService.findAllByIds(admissionTrainingProgramSubjectGroups
                .stream()
                .map(AdmissionTrainingProgramSubjectGroup::getId)
                .map(AdmissionTrainingProgramSubjectGroupId::getSubjectGroupId)
                .distinct().toList());

        List<SchoolDirectoryInfoDTO> schoolDirectoryInfoDTOS = new ArrayList<>();

        for (User user : users) {
            List<CampusProvinceDTO> campusProvinces = new ArrayList<>();
            UniversityInfo universityInfo = universityInfos.stream().filter((element) -> element.getId().equals(user.getId())).findFirst().orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy thông tin trường học."));
            List<UniversityCampus> universityCampuses1 = universityCampuses.stream().filter((element) -> element.getUniversityId().equals(user.getId())).toList();
            List<Province> provinces1 = provinces.stream().filter((element) -> universityCampuses1.stream().map(UniversityCampus::getProvinceId).toList().contains(element.getId())).toList();
            List<District> districts1 = districts.stream().filter((element) -> universityCampuses1.stream().map(UniversityCampus::getDistrictId).toList().contains(element.getId())).toList();
            List<Ward> wards1 = wards.stream().filter((element) -> universityCampuses1.stream().map(UniversityCampus::getWardId).toList().contains(element.getId())).toList();
            List<UniversityTrainingProgram> universityTrainingPrograms1 = universityTrainingPrograms
                    .stream()
                    .filter((element) -> element.getUniversityId().equals(user.getId()) && (schoolDirectoryRequest.getMajorIds() == null || schoolDirectoryRequest.getMajorIds().contains(element.getMajorId())))
                    .toList();
            Integer totalQuota = 0, totalMethod = 0, totalMajor = 0;
            Admission admission = admissions.stream().filter((element) -> element.getUniversityId().equals(user.getId())).findFirst().orElse(null);
            List<Integer> admissionTrainingProgramIds = null, admissionMethodIds = null;
            if (admission != null){
                List<AdmissionTrainingProgram> admissionTrainingPrograms1 = admissionTrainingPrograms.stream().filter((element) -> element.getAdmissionId().equals(admission.getId())).toList();
                List<AdmissionTrainingProgramMethod> admissionTrainingProgramMethods1 = admissionTrainingProgramMethods.stream().filter((element) -> admissionTrainingPrograms1.stream().map(AdmissionTrainingProgram::getId).toList().contains(element.getId().getAdmissionTrainingProgramId())).toList();

                totalQuota = admissionTrainingProgramMethods1.stream()
                        .map(AdmissionTrainingProgramMethod::getQuota)
                        .filter(Objects::nonNull)
                        .reduce(0, Integer::sum);

                totalMethod = admissionTrainingProgramMethods1.stream()
                        .map(AdmissionTrainingProgramMethod::getId)
                        .map(AdmissionTrainingProgramMethodId::getAdmissionMethodId)
                        .distinct()
                        .toList().size();

                totalMajor = admissionTrainingPrograms1.stream()
                        .map(AdmissionTrainingProgram::getMajorId)
                        .distinct()
                        .toList().size();

                admissionTrainingProgramIds = admissionTrainingPrograms1.stream().map(AdmissionTrainingProgram::getId).toList();
                admissionMethodIds = admissionTrainingProgramMethods1.stream().map(AdmissionTrainingProgramMethod::getId).map(AdmissionTrainingProgramMethodId::getAdmissionMethodId).distinct().toList();

            } else {
                continue;
            }
            for (UniversityCampus universityCampus: universityCampuses1){
                Province province = provinces1.stream().filter((element) -> element.getId().equals(universityCampus.getProvinceId())).findFirst().orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy tỉnh thành."));
                District district = districts1.stream().filter((element) -> element.getId().equals(universityCampus.getDistrictId())).findFirst().orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy quận huyện."));
                Ward ward = wards1.stream().filter((element) -> element.getId().equals(universityCampus.getWardId())).findFirst().orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy phường xã."));
                campusProvinces.add(new CampusProvinceDTO(province, district, ward, universityCampus, schoolDirectoryRequest.getProvinceIds()));
            }
            schoolDirectoryInfoDTOS.add(new SchoolDirectoryInfoDTO(user, universityInfo, universityTrainingPrograms1, campusProvinces, totalQuota, admission, totalMethod, totalMajor, admissionTrainingProgramIds, admissionMethodIds));
        }

        return new SchoolDirectoryInfoResponse(majors.stream().map(InfoMajorDTO::new).toList(),
                methods.stream().map(InfoMethodDTO::new).toList(),
                subjectGroups.stream().map(SubjectGroupResponseDTO2::new).toList(),
                new PageImpl<>(schoolDirectoryInfoDTOS, PageRequest.of(schoolDirectoryRequest.getPageNumber(), schoolDirectoryRequest.getPageSize()), countAll));
    }

    public SchoolDirectoryInfoResponse schoolDirectoryAfterSearch(SchoolDirectoryRequest schoolDirectoryRequest) {
        List<Admission> admissions = schoolDirectoryGetAdmission(schoolDirectoryRequest);

        List<Integer> admissionIds = admissions.stream().map(Admission::getId).toList();

        if (admissions.isEmpty()) {
            throw new ResourceNotFoundException("Không có trường đại học phù hợp.");
        }

        Integer countAll = countSchoolDirectoryGetAdmission(schoolDirectoryRequest);

        List<Integer> universityIds = admissions.stream().map(Admission::getUniversityId).toList();

        List<AdmissionTrainingProgram> admissionTrainingPrograms = null;

        if (schoolDirectoryRequest.getMajorIds() == null || schoolDirectoryRequest.getMajorIds().isEmpty()) {
            admissionTrainingPrograms = admissionTrainingProgramService.findByAdmissionIds(admissionIds);
        } else {
            admissionTrainingPrograms = admissionTrainingProgramService.findByAdmissionIdsAndMajorIds(admissionIds, schoolDirectoryRequest.getMajorIds());
        }

        List<AdmissionTrainingProgramSubjectGroup> admissionTrainingProgramSubjectGroups = null;
        if (schoolDirectoryRequest.getSubjectGroupIds() != null && !schoolDirectoryRequest.getSubjectGroupIds().isEmpty()) {
            admissionTrainingProgramSubjectGroups = admissionTrainingProgramSubjectGroupService.findByAdmissionTrainingProgramIdsAndSubjectGroupIds(admissionTrainingPrograms.stream().map(AdmissionTrainingProgram::getId).toList(), schoolDirectoryRequest.getSubjectGroupIds());
        }

        if (admissionTrainingProgramSubjectGroups != null && !admissionTrainingProgramSubjectGroups.isEmpty()) {
            List<Integer> admissionTrainingProgramIds = admissionTrainingProgramSubjectGroups.stream().map(AdmissionTrainingProgramSubjectGroup::getId).map(AdmissionTrainingProgramSubjectGroupId::getAdmissionTrainingProgramId).toList();

            admissionTrainingPrograms = admissionTrainingPrograms
                    .stream()
                    .filter((element) -> admissionTrainingProgramIds.contains(element.getId()))
                    .toList();
        }

        List<AdmissionTrainingProgramMethod> admissionTrainingProgramMethods = null;

        if (schoolDirectoryRequest.getMethodIds() != null && !schoolDirectoryRequest.getMethodIds().isEmpty()) {
            admissionTrainingProgramMethods = admissionTrainingProgramMethodService.findByAdmissionTrainingProgramIdInAndMethodIds(admissionTrainingPrograms.stream().map(AdmissionTrainingProgram::getId).toList(), schoolDirectoryRequest.getMethodIds());
        } else {
            admissionTrainingProgramMethods = admissionTrainingProgramMethodService.findByAdmissionTrainingProgramIds(admissionTrainingPrograms.stream().map(AdmissionTrainingProgram::getId).toList());
        }

        List<UniversityTrainingProgram> universityTrainingPrograms = universityTrainingProgramService.findByUniversityIdsWithStatus(universityIds, UniversityTrainingProgramStatus.ACTIVE);

        List<User> users = userService.findByIds(universityIds);

        List<UniversityInfo> universityInfos = universityInfoServiceImpl.findByIds(universityIds);

        List<UniversityCampus> universityCampuses = universityCampusService.findByUniversityIds(universityIds);

        List<Province> provinces = addressServiceImpl.findProvinceByIds(universityCampuses.stream().map(UniversityCampus::getProvinceId).distinct().toList());

        List<SchoolDirectoryInfoDTO> schoolDirectoryInfoDTOS = new ArrayList<>();

        for (User user : users) {
            List<CampusProvinceDTO> campusProvinces;
            UniversityInfo universityInfo = universityInfos.stream().filter((element) -> element.getId().equals(user.getId())).findFirst().orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy thông tin trường học."));
            List<UniversityCampus> universityCampuses1 = universityCampuses.stream().filter((element) -> element.getUniversityId().equals(user.getId())).toList();
            List<Province> provinces1 = provinces.stream().filter((element) -> universityCampuses1.stream().map(UniversityCampus::getProvinceId).toList().contains(element.getId())).toList();
            List<UniversityTrainingProgram> universityTrainingPrograms1 = universityTrainingPrograms
                    .stream()
                    .filter((element) -> element.getUniversityId().equals(user.getId()) && (schoolDirectoryRequest.getMajorIds() == null || schoolDirectoryRequest.getMajorIds().contains(element.getMajorId())))
                    .toList();
            Integer totalQuota = 0, totalMethod = 0, totalMajor = 0;
            Admission admission = admissions.stream().filter((element) -> element.getUniversityId().equals(user.getId())).findFirst().orElse(null);
            List<Integer> admissionTrainingProgramIds = null, admissionMethodIds = null;
            if (admission != null){
                List<AdmissionTrainingProgram> admissionTrainingPrograms1 = admissionTrainingPrograms.stream().filter((element) -> element.getAdmissionId().equals(admission.getId())).toList();
                List<AdmissionTrainingProgramMethod> admissionTrainingProgramMethods1 = admissionTrainingProgramMethods.stream().filter((element) -> admissionTrainingPrograms1.stream().map(AdmissionTrainingProgram::getId).toList().contains(element.getId().getAdmissionTrainingProgramId())).toList();

                totalQuota = admissionTrainingProgramMethods1.stream()
                        .map(AdmissionTrainingProgramMethod::getQuota)
                        .filter(Objects::nonNull)
                        .reduce(0, Integer::sum);

                totalMethod = admissionTrainingProgramMethods1.stream()
                        .map(AdmissionTrainingProgramMethod::getId)
                        .map(AdmissionTrainingProgramMethodId::getAdmissionMethodId)
                        .distinct()
                        .toList().size();

                totalMajor = admissionTrainingPrograms1.stream()
                        .map(AdmissionTrainingProgram::getMajorId)
                        .distinct()
                        .toList().size();

                admissionTrainingProgramIds = admissionTrainingPrograms1.stream().map(AdmissionTrainingProgram::getId).toList();
                admissionMethodIds = admissionTrainingProgramMethods1.stream().map(AdmissionTrainingProgramMethod::getId).map(AdmissionTrainingProgramMethodId::getAdmissionMethodId).distinct().toList();

            } else {
                continue;
            }
            campusProvinces = provinces1
                    .stream()
                    .map((element) -> new CampusProvinceDTO(
                            element,
                            universityCampuses1.stream().filter((ele) -> ele.getProvinceId().equals(element.getId())).findFirst().orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy campus trường học.")),
                            schoolDirectoryRequest.getProvinceIds()
                    ))
                    .toList();
            schoolDirectoryInfoDTOS.add(new SchoolDirectoryInfoDTO(user, universityInfo, universityTrainingPrograms1, campusProvinces, totalQuota, admission, totalMethod, totalMajor, admissionTrainingProgramIds, admissionMethodIds));
        }

        return new SchoolDirectoryInfoResponse(null, null, null, new PageImpl<>(schoolDirectoryInfoDTOS, PageRequest.of(schoolDirectoryRequest.getPageNumber(), schoolDirectoryRequest.getPageSize()), countAll));
    }

    private String buildQueryForSchoolDirectory(SchoolDirectoryRequest schoolDirectoryRequest, Map<String, Object> parameters, Integer year) {

        StringBuilder queryBuilder = new StringBuilder("select distinct a.*\n" +
                "from university_info ui\n" +
                "inner join university_campus uc on uc.university_id = ui.university_id\n" +
                "inner join admission a on a.university_id = ui.university_id\n" +
                "inner join admission_training_program atp on atp.admission_id = a.id\n" +
                "inner join admission_training_program_subject_group atpsg on atp.id = atpsg.admission_training_program_id\n" +
                "inner join admission_method am on a.id = am.admission_id\n" +
                "inner join admission_training_program_method atpm on atpm.admission_training_program_id = atp.id\n"+
                "where a.status = 'ACTIVE'\n" +
                "and a.year = :year\n");

        parameters.put("year", year);

        if (schoolDirectoryRequest.getUniversityIds() != null && !schoolDirectoryRequest.getUniversityIds().isEmpty()) {
            queryBuilder.append("and ui.university_id in (:universityIds)\n");
            parameters.put("universityIds", schoolDirectoryRequest.getUniversityIds());
        }

        if (schoolDirectoryRequest.getSubjectGroupIds() != null && !schoolDirectoryRequest.getSubjectGroupIds().isEmpty()) {
            queryBuilder.append("and atpsg.subject_group_id in (:subjectGroupIds)\n");
            parameters.put("subjectGroupIds", schoolDirectoryRequest.getSubjectGroupIds());
        }

        if (schoolDirectoryRequest.getMethodIds() != null && !schoolDirectoryRequest.getMethodIds().isEmpty()) {
            queryBuilder.append("and am.method_id in (:methodIds)\n");
            parameters.put("methodIds", schoolDirectoryRequest.getMethodIds());
        }

        if (schoolDirectoryRequest.getProvinceIds() != null && !schoolDirectoryRequest.getProvinceIds().isEmpty()) {
            queryBuilder.append("and uc.province_id in (:provinceIds)\n");
            parameters.put("provinceIds", schoolDirectoryRequest.getProvinceIds());
        }

        if (schoolDirectoryRequest.getMajorIds() != null && !schoolDirectoryRequest.getMajorIds().isEmpty()) {
            queryBuilder.append("and atp.major_id in (:majorIds)\n");
            parameters.put("majorIds", schoolDirectoryRequest.getMajorIds());
        }

        if (schoolDirectoryRequest.getPageNumber() == null || schoolDirectoryRequest.getPageSize() == null) {
            return queryBuilder.toString();
        }

        if (schoolDirectoryRequest.getPageNumber() != null && schoolDirectoryRequest.getPageSize() != null ) {
            queryBuilder.append("ORDER BY a.id desc\n")
                    .append("OFFSET :PageNumber * :PageSize ROWS\n")
                    .append("FETCH NEXT :PageSize ROWS ONLY");
            parameters.put("PageNumber", schoolDirectoryRequest.getPageNumber());
            parameters.put("PageSize", schoolDirectoryRequest.getPageSize());
        }

        return queryBuilder.toString();
    }

    private String buildCountQueryForSchoolDirectory(SchoolDirectoryRequest schoolDirectoryRequest, Map<String, Object> parameters, Integer year) {

        StringBuilder queryBuilder = new StringBuilder("select count(distinct a.id)\n" +
                "from university_info ui\n" +
                "inner join university_campus uc on uc.university_id = ui.university_id\n" +
                "inner join admission a on a.university_id = ui.university_id\n" +
                "inner join admission_training_program atp on atp.admission_id = a.id\n" +
                "inner join admission_training_program_subject_group atpsg on atp.id = atpsg.admission_training_program_id\n" +
                "inner join admission_method am on a.id = am.admission_id\n" +
                "inner join admission_training_program_method atpm on atpm.admission_training_program_id = atp.id\n"+
                "where a.status = 'ACTIVE'\n" +
                "and a.year = :year\n");

        parameters.put("year", year);

        if (schoolDirectoryRequest.getUniversityIds() != null && !schoolDirectoryRequest.getUniversityIds().isEmpty()) {
            queryBuilder.append("and ui.university_id in (:universityIds)\n");
            parameters.put("universityIds", schoolDirectoryRequest.getUniversityIds());
        }

        if (schoolDirectoryRequest.getSubjectGroupIds() != null && !schoolDirectoryRequest.getSubjectGroupIds().isEmpty()) {
            queryBuilder.append("and atpsg.subject_group_id in (:subjectGroupIds)\n");
            parameters.put("subjectGroupIds", schoolDirectoryRequest.getSubjectGroupIds());
        }

        if (schoolDirectoryRequest.getMethodIds() != null && !schoolDirectoryRequest.getMethodIds().isEmpty()) {
            queryBuilder.append("and am.method_id in (:methodIds)\n");
            parameters.put("methodIds", schoolDirectoryRequest.getMethodIds());
        }

        if (schoolDirectoryRequest.getProvinceIds() != null && !schoolDirectoryRequest.getProvinceIds().isEmpty()) {
            queryBuilder.append("and uc.province_id in (:provinceIds)\n");
            parameters.put("provinceIds", schoolDirectoryRequest.getProvinceIds());
        }

        if (schoolDirectoryRequest.getPageNumber() == null || schoolDirectoryRequest.getPageSize() == null) {
            return queryBuilder.toString();
        }

        return queryBuilder.toString();
    }

    public List<Admission> schoolDirectoryGetAdmission(SchoolDirectoryRequest schoolDirectoryRequest) {
        Map<String, Object> parameters = new HashMap<>();

        String query = buildQueryForSchoolDirectory(schoolDirectoryRequest, parameters, LocalDateTime.now().getYear());

        Query executeQuery = entityManager.createNativeQuery(query, Admission.class);

        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            executeQuery.setParameter(entry.getKey(), entry.getValue());
        }

        return executeQuery.getResultList();
    }

    public Integer countSchoolDirectoryGetAdmission(SchoolDirectoryRequest schoolDirectoryRequest) {
        Map<String, Object> parameters = new HashMap<>();

        String query = buildCountQueryForSchoolDirectory(schoolDirectoryRequest, parameters, LocalDateTime.now().getYear());

        Query executeQuery = entityManager.createNativeQuery(query, Integer.class);

        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            executeQuery.setParameter(entry.getKey(), entry.getValue());
        }

        return (Integer) executeQuery.getSingleResult();
    }

    public SchoolDirectoryResponse schoolDirectoryDetail(SchoolDirectoryDetailRequest request) {
        List<AdmissionTrainingProgramMethod> admissionTrainingProgramMethods = admissionTrainingProgramMethodService.findByAdmissionTrainingProgramIdsAndAdmissionMethodIds(request.getAdmissionTrainingProgramIds(), request.getAdmissionMethodIds());
        List<AdmissionMethod> admissionMethods = admissionMethodService.findByIds(request.getAdmissionMethodIds());
        List<Method> methods = methodService.findByIds(admissionMethods.stream().map(AdmissionMethod::getMethodId).distinct().toList());
        List<AdmissionTrainingProgram> admissionTrainingPrograms = admissionTrainingProgramService.findByIds(request.getAdmissionTrainingProgramIds());
        List<AdmissionTrainingProgramSubjectGroup> admissionTrainingProgramSubjectGroups = admissionTrainingProgramSubjectGroupService.findByAdmissionTrainingProgramId(request.getAdmissionTrainingProgramIds());
        List<SubjectGroup> subjectGroups = subjectGroupService.findAllByIds(admissionTrainingProgramSubjectGroups.stream().map(AdmissionTrainingProgramSubjectGroup::getId).map(AdmissionTrainingProgramSubjectGroupId::getSubjectGroupId).distinct().toList());
        List<Major> majors = majorService.findByIds(admissionTrainingPrograms.stream().map(AdmissionTrainingProgram::getMajorId).distinct().toList());
        List<Subject> subjects = subjectService.findByIds(admissionTrainingPrograms.stream().map(AdmissionTrainingProgram::getMainSubjectId).filter(Objects::nonNull).distinct().toList());
        Admission admission = this.findById(request.getAdmissionId());
        List<UniversityTrainingProgram> universityTrainingPrograms = universityTrainingProgramService.findByUniversityIdWithStatus(admission.getUniversityId(), UniversityTrainingProgramStatus.ACTIVE);

        List<SchoolDirectoryDetailDTO> schoolDirectoryDetailDTOS = new ArrayList<>();
        for (AdmissionTrainingProgramMethod admissionTrainingProgramMethod : admissionTrainingProgramMethods) {
            AdmissionMethod admissionMethod = admissionMethods.stream().filter((element) -> element.getId().equals(admissionTrainingProgramMethod.getId().getAdmissionMethodId())).findFirst().orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Admission Method."));
            Method method = methods.stream().filter((element) -> element.getId().equals(admissionMethod.getMethodId())).findFirst().orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy phương thức tuyển sinh."));
            AdmissionTrainingProgram admissionTrainingProgram = admissionTrainingPrograms.stream().filter((element) -> element.getId().equals(admissionTrainingProgramMethod.getId().getAdmissionTrainingProgramId())).findFirst().orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy chương trình đào tạo."));
            List<AdmissionTrainingProgramSubjectGroup> admissionTrainingProgramSubjectGroups1 = admissionTrainingProgramSubjectGroups.stream().filter((element) -> element.getId().getAdmissionTrainingProgramId().equals(admissionTrainingProgram.getId())).distinct().toList();
            Major major = majors.stream().filter((element) -> element.getId().equals(admissionTrainingProgram.getMajorId())).findFirst().orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy ngành học."));
            List<Integer> subjectGroupIds =admissionTrainingProgramSubjectGroups1 .stream().map(AdmissionTrainingProgramSubjectGroup::getId).map(AdmissionTrainingProgramSubjectGroupId::getSubjectGroupId).toList();
            List<SubjectGroup> subjectGroups1 = subjectGroups.stream().filter((element) -> subjectGroupIds.contains(element.getId())).toList();
            Subject subject = subjects.stream().filter((element) -> element.getId().equals(admissionTrainingProgram.getMainSubjectId())).findFirst().orElse(null);
            UniversityTrainingProgram universityTrainingProgram = universityTrainingPrograms
                    .stream()
                    .filter((element) -> element.compareWithAdmissionTrainingProgram(admissionTrainingProgram))
                    .findFirst()
                    .orElse(null);

            schoolDirectoryDetailDTOS.add(new SchoolDirectoryDetailDTO(admissionTrainingProgramMethod, admissionTrainingProgram, method, major, subjectGroups1, subject, universityTrainingProgram));
        }

        return new SchoolDirectoryResponse(LocalDateTime.now().getYear(), admission.getSource(), schoolDirectoryDetailDTOS);
    }

    public void getAllAdmissionTrainingProgramCode(){
        List<AdmissionTrainingProgram> admissionTrainingPrograms = admissionTrainingProgramService.findAll();
        for (AdmissionTrainingProgram admissionTrainingProgram : admissionTrainingPrograms) {
            if (admissionTrainingProgram.getTrainingProgramCode() == null)
                admissionTrainingProgram.updateCode();
        }
        admissionTrainingProgramService.saveAllAdmissionTrainingProgram(admissionTrainingPrograms);
    }

    public List<UniversityCompareMajorDTO> compareMajor(Integer majorId, List<Integer> universityIds, Integer year) {
        List<AdmissionTrainingProgram> admissionTrainingPrograms = admissionTrainingProgramService.findByMajorIdAndUniversityIdsAndYear(majorId, universityIds, year);
        List<AdmissionTrainingProgramMethod> admissionTrainingProgramMethods = admissionTrainingProgramMethodService.findByAdmissionTrainingProgramIds(admissionTrainingPrograms.stream().map(AdmissionTrainingProgram::getId).toList());
        List<Admission> admissions = this.findByUniversityIdsAndYearAndStatus(universityIds, year, AdmissionStatus.ACTIVE);
        List<UniversityTrainingProgram> universityTrainingPrograms = universityTrainingProgramService.findByUniversityIdsWithStatusWithMajorId(universityIds, UniversityTrainingProgramStatus.ACTIVE, majorId);
        List<UniversityInfo> universityInfos = universityInfoServiceImpl.findByIds(universityIds);
        List<User> accounts = userService.findByIds(universityIds);
        Major major = majorService.findById(majorId);

        List<UniversityCompareMajorDTO> universityCompareMajorDTOS = new ArrayList<>();
        for (User user: accounts) {
            UniversityInfo universityInfo = universityInfos.stream().filter((ele) -> ele.getId().equals(user.getId())).findFirst().orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy thông tin trường học."));
            Admission admission = admissions.stream().filter((ele) -> ele.getUniversityId().equals(user.getId())).findFirst().orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy thông tin trường học."));
            List<AdmissionTrainingProgram> admissionTrainingPrograms1 = admissionTrainingPrograms.stream().filter((ele) -> ele.getAdmissionId().equals(admission.getId())).toList();
            List<Integer> admissionTrainingProgramIds = admissionTrainingPrograms1.stream().map(AdmissionTrainingProgram::getId).toList();
            List<AdmissionTrainingProgramMethod> admissionTrainingProgramMethods1 = admissionTrainingProgramMethods.stream().filter((ele) -> admissionTrainingProgramIds.contains(ele.getId().getAdmissionTrainingProgramId())).toList();
            List<UniversityTrainingProgram> universityTrainingPrograms1 = universityTrainingPrograms.stream().filter((ele) -> ele.getUniversityId().equals(user.getId())).toList();

            List<CompareMajorDTO> compareMajorDTOS = new ArrayList<>();
            for (AdmissionTrainingProgram admissionTrainingProgram : admissionTrainingPrograms1) {
                AdmissionTrainingProgramMethod admissionTrainingProgramMethod = admissionTrainingProgramMethods1.stream().filter((ele) -> ele.getId().getAdmissionTrainingProgramId().equals(admissionTrainingProgram.getId())).findFirst().orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy phương thức tuyển sinh."));

                compareMajorDTOS.add(mappingCompareMajorDTO(admissionTrainingProgram, admissionTrainingProgramMethod, major, admission, universityTrainingPrograms1));
            }
            universityCompareMajorDTOS.add(new UniversityCompareMajorDTO(user, universityInfo, compareMajorDTOS, admission));
        }
        return universityCompareMajorDTOS;
    }

    public List<UniversityCompareMajorDTO> compareMajor(Integer majorId, List<Integer> universityIds, Integer year, Integer studentReportId) {
        StudentReport studentReport = studentReportService.findById(studentReportId);
        List<AdmissionTrainingProgram> admissionTrainingPrograms = admissionTrainingProgramService.findByMajorIdAndUniversityIdsAndYear(majorId, universityIds, year);
        List<AdmissionTrainingProgramMethod> admissionTrainingProgramMethods = admissionTrainingProgramMethodService.findByAdmissionTrainingProgramIds(admissionTrainingPrograms.stream().map(AdmissionTrainingProgram::getId).toList());
        List<Admission> admissions = this.findByUniversityIdsAndYearAndStatus(universityIds, year, AdmissionStatus.ACTIVE);
        List<AdmissionMethod> admissionMethods = admissionMethodService.findByAdmissionIds(admissions.stream().map(Admission::getId).distinct().toList());
        List<UniversityTrainingProgram> universityTrainingPrograms = universityTrainingProgramService.findByUniversityIdsWithStatusWithMajorId(universityIds, UniversityTrainingProgramStatus.ACTIVE, majorId);
        List<UniversityInfo> universityInfos = universityInfoServiceImpl.findByIds(universityIds);
        List<User> accounts = userService.findByIds(universityIds);
        List<Method> methods = methodService.findByIds(admissionMethods.stream().map(AdmissionMethod::getMethodId).distinct().toList());
        Major major = majorService.findById(majorId);

        List<UniversityCompareMajorDTO> universityCompareMajorDTOS = new ArrayList<>();
        for (User user: accounts) {
            UniversityInfo universityInfo = universityInfos.stream().filter((ele) -> ele.getId().equals(user.getId())).findFirst().orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy thông tin trường học."));
            Admission admission = admissions.stream().filter((ele) -> ele.getUniversityId().equals(user.getId())).findFirst().orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy thông tin trường học."));
            List<AdmissionTrainingProgram> admissionTrainingPrograms1 = admissionTrainingPrograms.stream().filter((ele) -> ele.getAdmissionId().equals(admission.getId())).toList();
            List<Integer> admissionTrainingProgramIds = admissionTrainingPrograms1.stream().map(AdmissionTrainingProgram::getId).toList();
            List<AdmissionTrainingProgramMethod> admissionTrainingProgramMethods1 = admissionTrainingProgramMethods.stream().filter((ele) -> admissionTrainingProgramIds.contains(ele.getId().getAdmissionTrainingProgramId())).toList();
            List<UniversityTrainingProgram> universityTrainingPrograms1 = universityTrainingPrograms.stream().filter((ele) -> ele.getUniversityId().equals(user.getId())).toList();
            List<AdmissionMethod> admissionMethods1 = admissionMethods.stream().filter((ele) -> ele.getAdmissionId().equals(admission.getId())).toList();
            List<CompareMajorDTO> compareMajorDTOS = new ArrayList<>();
            for (AdmissionTrainingProgram admissionTrainingProgram : admissionTrainingPrograms1) {
                List<AdmissionTrainingProgramMethod> admissionTrainingProgramMethods2 = admissionTrainingProgramMethods1.stream().filter((ele) -> ele.getId().getAdmissionTrainingProgramId().equals(admissionTrainingProgram.getId())).toList();

                compareMajorDTOS.add(mappingCompareMajorDTO(admissionTrainingProgram, admissionTrainingProgramMethods2, major, admission, universityTrainingPrograms1, admissionMethods1, studentReport, methods));
            }
            universityCompareMajorDTOS.add(new UniversityCompareMajorDTO(user, universityInfo, compareMajorDTOS, admission));
        }
        return universityCompareMajorDTOS;
    }

    private CompareMajorDTO mappingCompareMajorDTO(AdmissionTrainingProgram admissionTrainingProgram, List<AdmissionTrainingProgramMethod> admissionTrainingProgramMethods, Major major, Admission admission, List<UniversityTrainingProgram> universityTrainingProgram, List<AdmissionMethod> admissionMethods, StudentReport studentReport, List<Method> methods) {
        CompareMajorDTO result = new CompareMajorDTO(admissionTrainingProgram, majorService.mapInfo(major), admission, universityTrainingProgram);
        List<CompareMajorMethodDTO> methodAndScores = new ArrayList<>();
        for (AdmissionTrainingProgramMethod admissionTrainingProgramMethod : admissionTrainingProgramMethods) {
            AdmissionMethod admissionMethod = admissionMethods.stream().filter((element) -> element.getId().equals(admissionTrainingProgramMethod.getId().getAdmissionMethodId())).findFirst().orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Admission Method."));
            methodAndScores.add(
                    new CompareMajorMethodDTO(admissionMethod,
                            admissionTrainingProgramMethod,
                            studentReport,
                            methods.stream().filter((element) -> element.getId().equals(admissionMethod.getMethodId())).findFirst().orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy phương thức tuyển sinh.")))
            );
        }
        result.setMethodAndScores(methodAndScores);
        return result;
    }

    private CompareMajorDTO mappingCompareMajorDTO(AdmissionTrainingProgram admissionTrainingProgram, AdmissionTrainingProgramMethod admissionTrainingProgramMethod, Major major, Admission admission, List<UniversityTrainingProgram> universityTrainingProgram) {
        return new CompareMajorDTO(admissionTrainingProgram, majorService.mapInfo(major), admission, universityTrainingProgram);
    }

    private List<Admission> findByUniversityIdsAndYearAndStatus(List<Integer> universityIds, Integer year, AdmissionStatus status) {
        List<Admission> admissions = admissionRepository.findByUniversityIdInAndYearAndAdmissionStatus(universityIds, year, status);
        if (admissions.isEmpty()) {
            throw new ResourceNotFoundException("Không tìm thấy dữ liệu tuyển sinh.");
        }
        if (admissions.size() != universityIds.size()) {
            throw new ResourceNotFoundException("Dữ liệu tuyển sinh không đầy đủ.");
        }
        return admissions;
    }

    public List<UniversityInfoResponseDTO> getUniversitiesHaveMajorAtYear(Integer majorId, Integer year) {
        List<AdmissionTrainingProgram> admissionTrainingPrograms = admissionTrainingProgramService.findByMajorIdAndYear(majorId, year);

        if (admissionTrainingPrograms == null || admissionTrainingPrograms.isEmpty()) {
            throw new ResourceNotFoundException("Không tìm thấy trường nào có ngành học này");
        }
        List<Admission> admissions = this.findByIds(admissionTrainingPrograms.stream().map(AdmissionTrainingProgram::getAdmissionId).distinct().toList());
        List<Integer> universityIds = admissions.stream().map(Admission::getUniversityId).distinct().toList();
        List<UniversityInfo> universityInfos = universityInfoServiceImpl.findByIds(universityIds).stream().toList();
        List<User> users = userService.findByIds(universityInfos.stream().map(UniversityInfo::getId).distinct().toList());
        List<UniversityCampus> universityCampuses = universityCampusService.findByUniversityIds(users.stream().map(User::getId).distinct().toList());

        List<UniversityInfoResponseDTO> result = new ArrayList<>();

        for (User user: users) {
            UniversityInfo universityInfo = universityInfos.stream().filter(info -> info.getId().equals(user.getId())).findFirst().orElse(null);
            List<UniversityCampus> universityCampus = universityCampuses.stream().filter(campus -> campus.getUniversityId().equals(user.getId())).toList();

            if (universityInfo != null && universityCampus != null) {
                result.add(new UniversityInfoResponseDTO(
                        modelMapper.map(user, InfoUserResponseDTO.class),
                        modelMapper.map(universityInfo, InfoUniversityResponseDTO.class),
                        universityCampusService.mapToListCampusV2(user.getId())));
            }
        }

        return result;
    }

    private List<Admission> findByIds(List<Integer> list) {
        return admissionRepository.findAllById(list);
    }

    public List<UniversityCompareMajorDTO> compareMajorsFromUniversities(List<CompareMajorsFromUniversitiesRequest> request, Integer year, Integer studentReportId) {
        StudentReport studentReport = studentReportService.findById(studentReportId);
        List<Integer> majorIds = request.stream().map(CompareMajorsFromUniversitiesRequest::getMajorId).distinct().toList();
        List<Integer> universityIds = request.stream().map(CompareMajorsFromUniversitiesRequest::getUniversityId).distinct().toList();
        List<AdmissionTrainingProgram> admissionTrainingPrograms = admissionTrainingProgramService.findByMajorIdWithUniversityIdAndYear(request, year);
        if (admissionTrainingPrograms.stream().map(AdmissionTrainingProgram::getMajorId).distinct().toList().size() != majorIds.size())
            throw new ResourceNotFoundException("Không tìm thấy đủ chương trình đào tạo cho " + majorIds.size() + " ngành.");
        List<Admission> admissions = findByIds(admissionTrainingPrograms.stream().map(AdmissionTrainingProgram::getAdmissionId).distinct().toList());
        if (admissions.stream().map(Admission::getUniversityId).distinct().toList().size() != universityIds.size())
            throw new ResourceNotFoundException("Không tìm thấy đủ chương trình đào tạo cho " + universityIds.size() + " ngành.");
        List<AdmissionTrainingProgramMethod> admissionTrainingProgramMethods = admissionTrainingProgramMethodService.findByAdmissionTrainingProgramIds(admissionTrainingPrograms.stream().map(AdmissionTrainingProgram::getId).toList());
        List<AdmissionMethod> admissionMethods = admissionMethodService.findByAdmissionIds(admissions.stream().map(Admission::getId).distinct().toList());
        List<UniversityTrainingProgram> universityTrainingPrograms = universityTrainingProgramService.findByUniversityIdsWithStatusWithMajorIds(universityIds, UniversityTrainingProgramStatus.ACTIVE, majorIds);
        List<UniversityInfo> universityInfos = universityInfoServiceImpl.findByIds(universityIds);
        List<User> accounts = userService.findByIds(universityIds);
        List<Method> methods = methodService.findByIds(admissionMethods.stream().map(AdmissionMethod::getMethodId).distinct().toList());
        List<Major> majors = majorService.findByIds(majorIds);

        List<UniversityCompareMajorDTO> universityCompareMajorDTOS = new ArrayList<>();
        for (User user: accounts) {
            UniversityInfo universityInfo = universityInfos.stream().filter((ele) -> ele.getId().equals(user.getId())).findFirst().orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy thông tin trường học."));
            Admission admission = admissions.stream().filter((ele) -> ele.getUniversityId().equals(user.getId())).findFirst().orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy thông tin trường học."));
            List<AdmissionTrainingProgram> admissionTrainingPrograms1 = admissionTrainingPrograms.stream().filter((ele) -> ele.getAdmissionId().equals(admission.getId())).toList();
            List<Integer> admissionTrainingProgramIds = admissionTrainingPrograms1.stream().map(AdmissionTrainingProgram::getId).toList();
            List<AdmissionTrainingProgramMethod> admissionTrainingProgramMethods1 = admissionTrainingProgramMethods.stream().filter((ele) -> admissionTrainingProgramIds.contains(ele.getId().getAdmissionTrainingProgramId())).toList();
            List<UniversityTrainingProgram> universityTrainingPrograms1 = universityTrainingPrograms.stream().filter((ele) -> ele.getUniversityId().equals(user.getId())).toList();
            List<AdmissionMethod> admissionMethods1 = admissionMethods.stream().filter((ele) -> ele.getAdmissionId().equals(admission.getId())).toList();
            List<CompareMajorDTO> compareMajorDTOS = new ArrayList<>();
            for (AdmissionTrainingProgram admissionTrainingProgram : admissionTrainingPrograms1) {
                List<AdmissionTrainingProgramMethod> admissionTrainingProgramMethods2 = admissionTrainingProgramMethods1.stream().filter((ele) -> ele.getId().getAdmissionTrainingProgramId().equals(admissionTrainingProgram.getId())).toList();
                Major major = majors.stream().filter((ele) -> ele.getId().equals(admissionTrainingProgram.getMajorId())).findFirst().orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy ngành."));
                compareMajorDTOS.add(mappingCompareMajorDTO(admissionTrainingProgram, admissionTrainingProgramMethods2, major, admission, universityTrainingPrograms1, admissionMethods1, studentReport, methods));
            }
            universityCompareMajorDTOS.add(new UniversityCompareMajorDTO(user, universityInfo, compareMajorDTOS, admission));
        }
        return universityCompareMajorDTOS;
    }
}
