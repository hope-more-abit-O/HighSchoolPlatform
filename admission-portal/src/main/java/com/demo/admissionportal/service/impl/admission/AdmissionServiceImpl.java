package com.demo.admissionportal.service.impl.admission;

import com.demo.admissionportal.constants.AdmissionStatus;
import com.demo.admissionportal.constants.Role;
import com.demo.admissionportal.dto.entity.ActionerDTO;
import com.demo.admissionportal.dto.entity.admission.*;
import com.demo.admissionportal.dto.entity.admission.CreateTrainingProgramRequest;
import com.demo.admissionportal.dto.entity.admission.school_advice.SchoolAdviceDTO;
import com.demo.admissionportal.dto.entity.admission.school_advice.SchoolAdviceMajorDetailDTO;
import com.demo.admissionportal.dto.entity.major.InfoMajorDTO;
import com.demo.admissionportal.dto.entity.method.InfoMethodDTO;
import com.demo.admissionportal.dto.entity.university.InfoUniversityResponseDTO;
import com.demo.admissionportal.dto.request.admisison.*;
import com.demo.admissionportal.dto.response.admission.*;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.dto.response.sub_entity.SubjectGroupResponseDTO2;
import com.demo.admissionportal.entity.*;
import com.demo.admissionportal.entity.admission.*;
import com.demo.admissionportal.entity.admission.sub_entity.AdmissionTrainingProgramMethodId;
import com.demo.admissionportal.entity.admission.sub_entity.AdmissionTrainingProgramSubjectGroupId;
import com.demo.admissionportal.exception.exceptions.*;
import com.demo.admissionportal.repository.SubjectGroupRepository;
import com.demo.admissionportal.repository.admission.*;
import com.demo.admissionportal.service.AdmissionService;
import com.demo.admissionportal.service.UserService;
import com.demo.admissionportal.service.impl.*;
import com.demo.admissionportal.util.impl.ServiceUtils;
import jakarta.servlet.MultipartConfigElement;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdmissionServiceImpl implements AdmissionService {
    private final SubjectGroupRepository subjectGroupRepository;
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
    private final MultipartConfigElement multipartConfigElement;

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

        List<Method> methods = methodService.insertNewMethodsAndGetExistedMethods(request.getQuotas());

        List<Major> majors = majorService.insertNewMajorsAndGetExistedMajors(request.getQuotas());

        List<AdmissionMethod> admissionMethods = admissionMethodService.saveAllAdmissionMethodWithListMethods(admission.getId(), methods);

        List<AdmissionTrainingProgram> admissionTrainingPrograms = admissionTrainingProgramService.saveAdmissionTrainingProgram(admission.getId(), request.getQuotas(), majors);

        List<AdmissionTrainingProgramMethod> admissionTrainingProgramMethod = setAdmissionTrainingProgramMethod(majors, methods, admissionTrainingPrograms, admissionMethods, request.getQuotas());

        List<AdmissionTrainingProgramSubjectGroup> admissionTrainingProgramSubjectGroups = createAdmissionTrainingProgramSubjectGroup(request.getQuotas(), admissionTrainingPrograms, majors);

    }

    public List<CreateAdmissionQuotaRequest> processCreateAdmissionQuotaRequest(List<CreateAdmissionQuotaRequest> quotaRequests, List<Major> majors, List<Method> methods) {
        for (CreateAdmissionQuotaRequest quotaRequest : quotaRequests) {
            if (quotaRequest.getMajorId() == null) {
                quotaRequest.setMajorId(majors.stream()
                        .filter(major -> major.getName().equals(quotaRequest.getMajorName()) && major.getCode().equals(quotaRequest.getMethodCode()))
                        .map(Major::getId)
                        .findFirst()
                        .orElse(null));
            }

            if (quotaRequest.getMethodId() == null) {
                quotaRequest.setMethodId(methods.stream()
                        .filter(method -> method.getName().equals(quotaRequest.getMajorName()) && method.getCode().equals(quotaRequest.getMethodCode()))
                        .map(Method::getId)
                        .findFirst()
                        .orElse(null));
            }
        }

        return null;
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
        if (quotaRequest.getMethodId() != null) {
            log.info("Get admission method id by method id");
            return findAdmissionMethodIdByMethodId(quotaRequest.getMethodId(), admissionMethods);
        } else {
            log.info("Get admission method id by method name and code");
            return findAdmissionMethodIdByNameAndCode(quotaRequest.getMethodName(), quotaRequest.getMethodCode(), admissionMethods, methods);
        }
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
        if (quotaRequest.getMajorId() != null) {
            log.info("Get admission method id by method id");
            return findAdmissionTrainingProgramIdByMajorId(quotaRequest.getMajorId(), admissionTrainingPrograms);
        } else {
            log.info("Get admission method id by method name and code");
            return findAdmissionTrainingProgramIdByNameAndCode(quotaRequest.getMajorName(), quotaRequest.getMajorCode(), admissionTrainingPrograms, majors);
        }
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
                                                      Integer year,
                                                      String source,
                                                      Integer universityId,
                                                      Date createTime,
                                                      Integer createBy,
                                                      Integer updateBy,
                                                      Date updateTime,
                                                      AdmissionStatus status) {
        Page<Admission> admissions = admissionRepository.findAllBy(pageable, id, year, source, universityId, createTime, createBy, updateBy, updateTime, (status != null) ? status.name() : null);

        if (admissions.isEmpty()) {
            throw new ResourceNotFoundException("Không tìm thấy đề án thành công.");
        }
        List<ActionerDTO> actionerDTOs = this.getActioners(admissions.getContent());

        List<UniversityInfo> universityInfos = universityInfoServiceImpl.findByIds(admissions.getContent().stream().map(Admission::getUniversityId).toList());

        return ResponseData.ok("Lấy thông tin các đề án thành công.", admissions.map((element) -> this.mappingInfo(element, actionerDTOs, universityInfos)));
    }

    public ResponseData<List<String>> getSource(Integer year, String universityCode) {
        Optional<Admission> admissions = admissionRepository.findByYearAndUniversityCode(year, universityCode);

        if (admissions.isEmpty()) {
            throw new ResourceNotFoundException("Không tìm thấy đề án thành công.");
        }

        return ResponseData.ok("Lấy tài liệu thành công.", Arrays.stream(admissions.get().getSource().split(";")).toList());
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

        Admission admission = findById(admissionIds.iterator().next());
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

        return ResponseData.ok("Cập nhập điểm thành công.", admissionTrainingProgramMethodService.saveAll(admissionTrainingProgramMethods));
    }

    @Transactional
    public ResponseData universityUpdateStatus(UpdateAdmissionStatusRequest request)
            throws StoreDataFailedException, NotAllowedException {
        Integer uniId = ServiceUtils.getId();

        Admission admission = findById(request.getAdmissionId());
        if (!admission.getUniversityId().equals(uniId))
            throw new NotAllowedException("Bạn không có quyền thực hiện hành động này.");

        admission.setStatus(request.getStatus());
        admission.setNote(request.getNote());
        admission.setUpdateBy(uniId);
        admission.setUpdateTime(new Date());

        try {
            Admission admission1 = admissionRepository.save(admission);
        } catch (Exception e) {
            throw new StoreDataFailedException("Cập nhập thông tin đề án thất bại.", Map.of("error", e.getCause().getMessage()));
        }

        return ResponseData.ok("Cập nhập trạng thái đề án thành công.");
    }

    @Transactional
    public ResponseData updateAdmission(UpdateAdmissionRequest request) {

        return null;
    }

    public ResponseData getAdmissionScore(Integer year, String universityCode, Integer methodId) {
        Optional<Admission> admission = admissionRepository.findByYearAndUniversityCode(year, universityCode);

        if (admission.isEmpty()) {
            throw new ResourceNotFoundException("Không tìm thấy tài liệu nào");
        }

        List<AdmissionTrainingProgram> admissionTrainingPrograms = admissionTrainingProgramService.findByAdmissionId(admission.get().getId());

        List<AdmissionTrainingProgramSubjectGroup> admissionTrainingProgramSubjectGroups = admissionTrainingProgramSubjectGroupService.findByAdmissionTrainingProgramId(admissionTrainingPrograms.stream().map(AdmissionTrainingProgram::getId).toList());

        List<SubjectGroup> subjectGroups = subjectGroupService.findAllByIds(
                admissionTrainingProgramSubjectGroups
                        .stream()
                        .map(AdmissionTrainingProgramSubjectGroup::getId)
                        .map(AdmissionTrainingProgramSubjectGroupId::getSubjectGroupId)
                        .distinct()
                        .toList());

        List<Major> majors = majorService.findByIds(admissionTrainingPrograms.stream().map(AdmissionTrainingProgram::getMajorId).toList());

        List<AdmissionTrainingProgramMethod> admissionTrainingProgramMethods = admissionTrainingProgramMethodService.findByMethodIdAndAdmissionTrainingProgramIds(methodId, admissionTrainingPrograms.stream().map(AdmissionTrainingProgram::getId).toList());

        List<AdmissionScoreWithSubjectGroupDTO> admissionScores = new ArrayList<>();

        for (AdmissionTrainingProgram admissionTrainingProgram : admissionTrainingPrograms) {
            List<Integer> subjectGroupIds = admissionTrainingProgramSubjectGroups.stream()
                    .map(AdmissionTrainingProgramSubjectGroup::getId).filter(id -> admissionTrainingProgram.getId().equals(id.getAdmissionTrainingProgramId()))
                    .map(AdmissionTrainingProgramSubjectGroupId::getSubjectGroupId).distinct()
                    .toList();
            AdmissionTrainingProgramMethod admissionTrainingProgramMethod = admissionTrainingProgramMethods
                    .stream()
                    .filter((element) -> element.getId().getAdmissionTrainingProgramId().equals(admissionTrainingProgram.getId()))
                    .findFirst()
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy chương trình đào tạo với phương thức đào tạo."));
            Major major = majors.stream().filter((element) -> element.getId().equals(admissionTrainingProgram.getMajorId()))
                    .findFirst()
                    .orElseThrow(() -> new ResourceNotFoundException("Major not found"));
            List<SubjectGroup> subjectGroupForMap = subjectGroups.stream().filter((element) -> subjectGroupIds.contains(element.getId())).toList();

            admissionScores.add(new AdmissionScoreWithSubjectGroupDTO(majorService.mapInfo(major), subjectGroupService.mapInfo(subjectGroupForMap), admissionTrainingProgramMethod.getAdmissionScore()));
        }

        return ResponseData.ok("", new GetAdmissionScoreResponse(admissionScores));
    }

    public GetLatestTrainingProgramResponse getLatestTrainingProgramByUniversityId(Integer universityId) {
        User user = userService.findById(universityId);
        if (!user.getRole().equals(Role.UNIVERSITY)) {
            throw new ResourceNotFoundException("Không có trường học.", Map.of("universityId", universityId.toString()));
        }
        Admission admission = admissionRepository.findFirstByUniversityIdAndStatusOrderByYearDesc(universityId, AdmissionStatus.ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException("Trường bạn tìm hiện tại chưa có đề án nào."));
        List<Major> majors = majorService.findByAdmissionId(admission.getId());
        return new GetLatestTrainingProgramResponse(admission.getYear(), majors.stream().map((element) -> modelMapper.map(element, InfoMajorDTO.class)).toList());
    }

    public ResponseData adviceSchool(SchoolAdviceRequest request) {
        Integer year = 2024;
        List<AdmissionTrainingProgramMethod> admissionTrainingProgramMethods = admissionTrainingProgramMethodService.findBySubjectGroupIdAndScoreWithOffset(request.getSubjectGroupId(), request.getScore(), request.getOffset(), request.getMajorCode(), request.getProvinceId());
        log.info(request.toString());
        List<Integer> admissionTrainingProgramIds = admissionTrainingProgramMethods
                .stream()
                .map(AdmissionTrainingProgramMethod::getId)
                .map(AdmissionTrainingProgramMethodId::getAdmissionTrainingProgramId)
                .distinct().toList();

        List<AdmissionTrainingProgram> admissionTrainingPrograms = admissionTrainingProgramService.findByIds(admissionTrainingProgramIds);
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

            List<Admission> admissions1 = admissions.stream().filter((element) -> element.getUniversityId().equals(universityId)).toList();

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
                    year);

            Admission admission = admissions1.stream().filter((element) -> element.getYear().equals(Calendar.getInstance().get(Calendar.YEAR))).findFirst().orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đề án năm mới nhất"));
            schoolAdviceDTOs.add(new SchoolAdviceDTO(modelMapper.map(universityInfo, InfoUniversityResponseDTO.class), admissionTrainingProgramDTOV2s.size(), admissionTrainingProgramDTOV2s, admission.getSource()));
        }

        return ResponseData.ok("", schoolAdviceDTOs);
    }

    public List<AdmissionTrainingProgramDTOV2> getSchoolAdviceMajorDetails(Integer universityId, List<Major> majors, List<Admission> admissions, List<AdmissionTrainingProgram> admissionTrainingPrograms, List<AdmissionTrainingProgramSubjectGroup> admissionTrainingProgramSubjectGroups, List<AdmissionTrainingProgramMethod> admissionTrainingProgramMethods, List<SubjectGroup> subjectGroups, Integer year) {

        List<AdmissionTrainingProgramDTOV2> admissionTrainingProgramDTOV2s = new ArrayList<>();


        for (Major major : majors) {
            InfoMajorDTO majorDTO = modelMapper.map(major, InfoMajorDTO.class);
            SchoolAdviceMajorDetailDTO mapping = new SchoolAdviceMajorDetailDTO();
            mapping.setMajor(modelMapper.map(major, InfoMajorDTO.class));

            for (Admission admission : admissions) {
                if (!admission.getYear().equals(year)) {
                    AdmissionTrainingProgram admissionTrainingProgram = admissionTrainingPrograms
                            .stream()
                            .filter((a) -> (a.getMajorId().equals(major.getId())) && a.getAdmissionId().equals(admission.getId()) && (a.getTrainingSpecific() == null)) //Get null training specific only
                            .findAny()
                            .orElse(null);
                    if (admissionTrainingProgram == null) break;

                    AdmissionTrainingProgramMethod admissionTrainingProgramMethod = admissionTrainingProgramMethods
                            .stream()
                            .filter((ele) -> ele.getId().getAdmissionTrainingProgramId().equals(admissionTrainingProgram.getId()))
                            .findFirst()
                            .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy điểm.", Map.of("error", "Not found AdmissionTrainingProgramMethod.")));

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

                    if (admissionTrainingProgramDTOV2 == null) {
                        admissionTrainingProgramDTOV2s.add(new AdmissionTrainingProgramDTOV2(admission, modelMapper.map(majorDTO, InfoMajorDTO.class), new ArrayList<>(List.of(admissionTrainingProgramScoreDTO))));
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
}
