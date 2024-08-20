package com.demo.admissionportal.service.impl.admission;

import com.demo.admissionportal.constants.AdmissionScoreStatus;
import com.demo.admissionportal.constants.AdmissionStatus;
import com.demo.admissionportal.constants.Role;
import com.demo.admissionportal.dto.entity.ActionerDTO;
import com.demo.admissionportal.dto.entity.admission.CreateTrainingProgramRequest;
import com.demo.admissionportal.dto.entity.admission.*;
import com.demo.admissionportal.dto.entity.admission.school_advice.SchoolAdviceDTO;
import com.demo.admissionportal.dto.entity.admission.school_advice.SchoolAdviceMajorDetailDTO;
import com.demo.admissionportal.dto.entity.major.InfoMajorDTO;
import com.demo.admissionportal.dto.entity.method.InfoMethodDTO;
import com.demo.admissionportal.dto.entity.university.InfoUniversityResponseDTO;
import com.demo.admissionportal.dto.request.admisison.*;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.dto.response.admission.*;
import com.demo.admissionportal.dto.response.sub_entity.SubjectGroupResponseDTO2;
import com.demo.admissionportal.entity.*;
import com.demo.admissionportal.entity.admission.*;
import com.demo.admissionportal.entity.admission.sub_entity.AdmissionTrainingProgramMethodId;
import com.demo.admissionportal.entity.admission.sub_entity.AdmissionTrainingProgramSubjectGroupId;
import com.demo.admissionportal.exception.exceptions.*;
import com.demo.admissionportal.repository.ConsultantInfoRepository;
import com.demo.admissionportal.repository.admission.AdmissionRepository;
import com.demo.admissionportal.service.AdmissionService;
import com.demo.admissionportal.service.UserService;
import com.demo.admissionportal.service.impl.*;
import com.demo.admissionportal.util.impl.ServiceUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
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

    //TODO: check if training program with all fields existed in admission
    public CreateAdmissionTrainingProgramResponse createAdmissionTrainingProgram(CreateAdmissionTrainingProgramRequest request) {
        Admission admission = this.findById(request.getAdmissionId());

        List<AdmissionTrainingProgram> admissionTrainingPrograms = admissionTrainingProgramService.saveAllAdmissionTrainingProgram(request.getAdmissionId(), request.getTrainingPrograms());

        return new CreateAdmissionTrainingProgramResponse(this.mapFullResponse(admission),
                admissionTrainingPrograms.stream()
                        .map(admissionTrainingProgram -> modelMapper.map(admissionTrainingProgram, CreateTrainingProgramRequest.class))
                        .toList());
    }

    //TODO: throw Exception
    //TODO: check if method existed in admission
    public CreateAdmissionMethodResponse createAdmissionMethod(CreateAdmissionMethodRequest request) {
        Admission admission = this.findById(request.getAdmissionId());

        List<AdmissionMethod> admissionMethods = admissionMethodService.saveAllAdmissionMethod(request.getAdmissionId(), request.getMethodIds());

        List<Method> methods = methodService.findByIds(request.getMethodIds());
        return new CreateAdmissionMethodResponse(this.mapFullResponse(admission),
                methods.stream()
                        .map(admissionTrainingProgram -> modelMapper.map(admissionTrainingProgram, InfoMethodDTO.class))
                        .toList());
    }

    public ResponseData<CreateAdmissionTrainingProgramMethodQuotaResponse> createAdmissionTrainingProgramMethodQuota(CreateAdmissionTrainingProgramMethodRequest request) {
        CreateAdmissionTrainingProgramMethodQuotaResponse response = new CreateAdmissionTrainingProgramMethodQuotaResponse();
        List<AdmissionTrainingProgramMethod> admissionTrainingProgramMethods = admissionTrainingProgramMethodService.createAdmissionTrainingProgramMethod(request);

        response.setQuotas(admissionTrainingProgramMethods
                .stream()
                .map(admissionTrainingProgramMethod -> new AdmissionTrainingProgramMethodQuotaDTO(admissionTrainingProgramMethod.getId().getAdmissionTrainingProgramId(), admissionTrainingProgramMethod.getId().getAdmissionMethodId(), admissionTrainingProgramMethod.getQuota()))
                .toList());
        return ResponseData.ok("Tạo chi tiết chỉ tiêu thành công", response);
    }

    public List<AdmissionTrainingProgramSubjectGroup> createAdmissionTrainingProgramSubjectGroup(List<CreateAdmissionQuotaRequest> request, List<AdmissionTrainingProgram> admissionTrainingPrograms, List<Major> majors) {
        List<AdmissionTrainingProgramSubjectGroup> admissionTrainingProgramSubjectGroups = new ArrayList<>();

        for (CreateAdmissionQuotaRequest quotaRequest : request) {
            Integer admissionTrainingProgramId = getAdmissionTrainingProgramId(quotaRequest, admissionTrainingPrograms, majors);

            List<AdmissionTrainingProgramSubjectGroup> admissionTrainingProgramSubjectGroupList = quotaRequest.getSubjectGroupIds().stream()
                    .map(subjectGroupId -> new AdmissionTrainingProgramSubjectGroup(admissionTrainingProgramId, subjectGroupId)).toList();

            admissionTrainingProgramSubjectGroups.addAll(admissionTrainingProgramSubjectGroupList);
        }

        return admissionTrainingProgramSubjectGroupService.saveAll(admissionTrainingProgramSubjectGroups);
    }

    public CreateAdmissionTrainingProgramSubjectGroupResponse createAdmissionTrainingProgramSubjectGroup(CreateAdmissionTrainingProgramSubjectGroupRequest request) {
        List<AdmissionTrainingProgramSubjectGroup> admissionTrainingProgramSubjectGroups = new ArrayList<>();
        request.getSubjectGroupId().forEach(subjectGroupId -> admissionTrainingProgramSubjectGroups.add(new AdmissionTrainingProgramSubjectGroup(request.getAdmissionTrainingProgramId(), subjectGroupId)));

        List<AdmissionTrainingProgramSubjectGroup> addList = admissionTrainingProgramSubjectGroupService.saveAll(admissionTrainingProgramSubjectGroups);

        List<Integer> subjectGroupIds = addList.stream()
                .map(subjectGroup -> subjectGroup.getId().getSubjectGroupId())
                .toList();

        List<SubjectGroup> subjectGroups = subjectGroupService.findAllByIds(subjectGroupIds);

        CreateAdmissionTrainingProgramSubjectGroupResponse response = CreateAdmissionTrainingProgramSubjectGroupResponse.builder()
                .admissionTrainingProgramId(request.getAdmissionTrainingProgramId())
                .subjectGroupIds(subjectGroupIds)
                .build();

        return response;
    }

    @Transactional
    public void createAdmission(CreateAdmissionRequest request) throws DataExistedException {
        List<AdmissionQuotaDTO> admissionQuotaDTOs = request.getQuotas().stream().map(AdmissionQuotaDTO::new).toList();
        User consultant = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());

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
        return findAdmissionTrainingProgramIdByMajorId(quotaRequest.getMajorId(), admissionTrainingPrograms);
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
                                                      AdmissionScoreStatus scoreStatus) {
        Page<Admission> admissions = null;

        try {
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

    public List<SearchAdmissionDTO> searchAdmission(Pageable pageable, List<Integer> year, List<String> universityId, Integer staffId, List<AdmissionStatus> status) {
        try {
            List<String> statusString = (status == null || status.isEmpty()) ? null : status.stream().map(Enum::name).toList();
            List<Admission> admissions;
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

            return admissions.stream().map(element -> new SearchAdmissionDTO(element, universityInfos)).toList();
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
                result.setUpdateBy(getCreateBy(admission, actionerDTOs));
                result.setCreateBy(getUpdateBy(admission, actionerDTOs));
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
        UniversityInfo universityInfo = universityInfoServiceImpl.findById(admission.getUniversityId());
        result.setName("ĐỀ ÁN TUYỂN SINH NĂM " + admission.getYear() + " CỦA " + universityInfo.getName().toUpperCase());
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

    protected FullAdmissionDTO mappingFullWithNoUniInfo(Admission admission, List<AdmissionMethod> admissionMethods, List<AdmissionTrainingProgram> admissionTrainingPrograms, List<AdmissionTrainingProgramSubjectGroup> admissionTrainingProgramSubjectGroups, List<AdmissionTrainingProgramMethod> admissionTrainingProgramMethods)
            throws ResourceNotFoundException {
        FullAdmissionDTO result = this.mappingInfo(admission);

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

        return ResponseData.ok("Lấy thông tin các đề án thành công.", this.mappingFullWithNoUniInfo(admission, admissionMethods, admissionTrainingPrograms, admissionTrainingProgramSubjectGroups, admissionTrainingProgramMethods));
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
        if (!admission.getUniversityId().equals(ServiceUtils.getUser().getCreateBy()))
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

    @Transactional
    public ResponseData admissionUpdateStatus(Integer id, UpdateAdmissionStatusRequest request)
            throws StoreDataFailedException, NotAllowedException {
        User user = ServiceUtils.getUser();
        Admission admission = findById(id);
        if (user.getRole().equals(Role.STAFF)){
            UniversityInfo universityInfo = universityInfoServiceImpl.findById(admission.getUniversityId());
            if (!universityInfo.getStaffId().equals(user.getId()))
                throw new NotAllowedException("Bạn không có quyền thực hiện hành động này.");

            if (request.getStatus().equals(AdmissionStatus.ACTIVE)) {
                if (admission.getAdmissionStatus().equals(AdmissionStatus.ACTIVE)) {
                    throw new BadRequestException("Đề án đã được kích hoạt.");
                }
                if (!admission.getAdmissionStatus().equals(AdmissionStatus.PENDING)) {
                    throw new BadRequestException("Đề án không ở trạng thái chờ kích hoạt.");
                }
            }

            if (request.getStatus().equals(AdmissionStatus.INACTIVE)) {
                if (admission.getAdmissionStatus().equals(AdmissionStatus.INACTIVE)) {
                    throw new BadRequestException("Đề án đã bị hủy.");
                }
                if (!admission.getAdmissionStatus().equals(AdmissionStatus.ACTIVE)) {
                    throw new BadRequestException("Đề án không ở trạng thái kích hoạt.");
                }
            }

        } else {
            if (request.getStatus().equals(AdmissionStatus.ACTIVE)) {
                throw new NotAllowedException("Bạn không có quyền thực hiện hành động này.");
            }

            if (user.getRole().equals(Role.UNIVERSITY) || !admission.getUniversityId().equals(user.getId())) {
                throw new NotAllowedException("Bạn không có quyền thực hiện hành động này.");
            }

            if (user.getRole().equals(Role.CONSULTANT) && !admission.getUniversityId().equals(consultantInfoServiceImpl.findById(user.getId()).getUniversityId())) {
                throw new NotAllowedException("Bạn không có quyền thực hiện hành động này.");
            }
        }

        admission.setAdmissionStatus(request.getStatus());
        admission.setNote(request.getNote());
        admission.setUpdateBy(user.getId());
        admission.setUpdateTime(new Date());

        if (request.getStatus().equals(AdmissionStatus.ACTIVE) && admission.getYear().equals(Calendar.getInstance().get(Calendar.YEAR))) {
            List<AdmissionTrainingProgram> admissionTrainingPrograms = admissionTrainingProgramService.findByAdmissionId(id);
            universityTrainingProgramService.createFromAdmission(admissionTrainingPrograms, id, user.getId());
        }

        if (request.getStatus().equals(AdmissionStatus.INACTIVE)) {
            List<AdmissionTrainingProgram> admissionTrainingPrograms = admissionTrainingProgramService.findByAdmissionId(id);
            Admission nearestActiveAdmission = admissionRepository.findFirstByUniversityIdAndAdmissionStatusOrderByYearAsc(id, AdmissionStatus.ACTIVE)
                    .orElse(null);
            if (nearestActiveAdmission != null) {
                List<AdmissionTrainingProgram> admissionTrainingProgramList = admissionTrainingProgramService.findByAdmissionId(nearestActiveAdmission.getId());
                universityTrainingProgramService.createFromAdmission(admissionTrainingProgramList, id, user.getId());
            } else {
                universityTrainingProgramService.inactiveAll(id);
            }
        }

        try {
            admissionRepository.save(admission);
        } catch (Exception e) {
            throw new StoreDataFailedException("Cập nhật thông tin đề án thất bại.", Map.of("error", e.getCause().getMessage()));
        }

        return ResponseData.ok("Cập nhật trạng thái đề án thành công.");
    }

    //TODO: must complete
    @Transactional
    public ResponseData updateAdmission(UpdateAdmissionRequest request) {

        return null;
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
        Integer year = 2024;
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
                    methods);

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
                    .collect(Collectors.toList()).size();
            if (admission != null){
                schoolAdviceDTOs.add(
                        new SchoolAdviceDTO(modelMapper.map(universityInfo, InfoUniversityResponseDTO.class),
                                count,
                                admissionTrainingProgramDTOV2s,
                                admission.getSource()));
            }

        }

        if (schoolAdviceDTOs.size() < 1) {
            throw new ResourceNotFoundException("Không tìm thấy chương trình đào tạo phù hợp.");
        }

        return ResponseData.ok("Tìm kiếm các chương trình đào tạo phù hợp thành công", schoolAdviceDTOs);
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
                                                                           List<Method> methods) {

        List<AdmissionTrainingProgramDTOV2> admissionTrainingProgramDTOV2s = new ArrayList<>();



        for (Major major : majors) {
            Set<String> seen = new LinkedHashSet<>();
            admissionTrainingPrograms.stream().forEach(program -> seen.add(
                    program.getMajorId() + "-" +
                            (program.getMainSubjectId() != null ? program.getMainSubjectId() : "null") + "-" +
                            (program.getLanguage() != null ? program.getLanguage() : "null") + "-" +
                            (program.getTrainingSpecific() != null ? program.getTrainingSpecific() : "null")
            ));
            
            InfoMajorDTO majorDTO = modelMapper.map(major, InfoMajorDTO.class);
            SchoolAdviceMajorDetailDTO mapping = new SchoolAdviceMajorDetailDTO();
            mapping.setMajor(modelMapper.map(major, InfoMajorDTO.class));

            for (Admission admission : admissions) {
                for (String seenStr : seen) {
                    // Split the seenStr back into its components
                    String[] fields = seenStr.split("-");

                    Integer majorId = Integer.parseInt(fields[0]);
                    Integer mainSubjectId = "null".equals(fields[1]) ? null : Integer.parseInt(fields[1]);
                    String language = "null".equals(fields[2]) ? null : fields[2];
                    String trainingSpecific = "null".equals(fields[3]) ? null : fields[3];

                    // Filter the list based on the current combination
                    List<AdmissionTrainingProgram> filteredList = admissionTrainingPrograms.stream()
                            .filter(program -> program.getMajorId().equals(majorId) &&
                                    (program.getMainSubjectId() == null ? mainSubjectId == null : program.getMainSubjectId().equals(mainSubjectId)) &&
                                    (program.getLanguage() == null ? language == null : program.getLanguage().equals(language)) &&
                                    (program.getTrainingSpecific() == null ? trainingSpecific == null : program.getTrainingSpecific().equals(trainingSpecific)))
                            .collect(Collectors.toList());

                    for (AdmissionTrainingProgram admissionTrainingProgram : filteredList) {
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

                            AdmissionTrainingProgramScoreDTO admissionTrainingProgramScoreDTO = new AdmissionTrainingProgramScoreDTO(admission.getYear(), subjectGroups1.stream().map((element) -> modelMapper.map(element, SubjectGroupResponseDTO2.class)).collect(Collectors.toList()), admissionTrainingProgramMethod.getAdmissionScore());

                            AdmissionTrainingProgramDTOV2 admissionTrainingProgramDTOV2 = admissionTrainingProgramDTOV2s
                                    .stream()
                                    .filter((element) -> element.getUniversityId().equals(admission.getUniversityId()) && element.getMajor().equals(majorDTO))
                                    .findFirst()
                                    .orElse(null);

                            AdmissionMethod admissionMethod = admissionMethods.stream().filter((ele) -> ele.getId().equals(admissionTrainingProgramMethod.getId().getAdmissionMethodId())).findFirst().orElse(null);

                            String methodName = methods.stream().filter((ele) -> ele.getId().equals(admissionMethod.getMethodId())).findFirst().get().getName();
                            if (admissionTrainingProgramDTOV2 == null) {
                                admissionTrainingProgramDTOV2s.add(new AdmissionTrainingProgramDTOV2(admission, modelMapper.map(majorDTO, InfoMajorDTO.class), new ArrayList<>(List.of(admissionTrainingProgramScoreDTO)), admissionTrainingProgram.getTrainingSpecific(), admissionTrainingProgram.getLanguage(),
                                        methodName));
                            } else {
                                Integer index = admissionTrainingProgramDTOV2s.indexOf(admissionTrainingProgramDTOV2);
                                List<AdmissionTrainingProgramScoreDTO> list = admissionTrainingProgramDTOV2.getScore();
                                list.add(admissionTrainingProgramScoreDTO);
                                admissionTrainingProgramDTOV2.setScore(list);
                                admissionTrainingProgramDTOV2s.set(index, admissionTrainingProgramDTOV2);
                            }
                        }

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
                admissions = admissionRepository.find(pageable, null);
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
}
