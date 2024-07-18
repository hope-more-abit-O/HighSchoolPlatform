package com.demo.admissionportal.service.impl.admission;

import com.demo.admissionportal.dto.entity.ActionerDTO;
import com.demo.admissionportal.dto.entity.admission.AdmissionTrainingProgramMethodQuotaDTO;
import com.demo.admissionportal.dto.entity.admission.FullAdmissionDTO;
import com.demo.admissionportal.dto.entity.admission.CreateTrainingProgramRequest;
import com.demo.admissionportal.dto.entity.method.InfoMethodDTO;
import com.demo.admissionportal.dto.request.admisison.*;
import com.demo.admissionportal.dto.response.admission.CreateAdmissionMethodResponse;
import com.demo.admissionportal.dto.response.admission.CreateAdmissionTrainingProgramResponse;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.dto.response.admission.CreateAdmissionResponse;
import com.demo.admissionportal.dto.response.admission.CreateAdmissionTrainingProgramSubjectGroupResponse;
import com.demo.admissionportal.entity.Major;
import com.demo.admissionportal.entity.Method;
import com.demo.admissionportal.entity.SubjectGroup;
import com.demo.admissionportal.entity.User;
import com.demo.admissionportal.entity.admission.*;
import com.demo.admissionportal.exception.DataExistedException;
import com.demo.admissionportal.exception.ResourceNotFoundException;
import com.demo.admissionportal.exception.StoreDataFailedException;
import com.demo.admissionportal.repository.SubjectGroupRepository;
import com.demo.admissionportal.repository.admission.*;
import com.demo.admissionportal.service.AdmissionService;
import com.demo.admissionportal.service.UserService;
import com.demo.admissionportal.service.impl.MajorServiceImpl;
import com.demo.admissionportal.service.impl.MethodServiceImpl;
import com.demo.admissionportal.service.impl.SubjectGroupServiceImpl;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

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

    public Admission save(Admission admission){
        try {
            return admissionRepository.save(admission);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            throw new StoreDataFailedException("Lưu đề án thất bại", error);
        }
    }

    public Admission findById(Integer id){
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

    public FullAdmissionDTO mapFullResponse(Admission admission){
        FullAdmissionDTO fullAdmissionDTO = modelMapper.map(admission, FullAdmissionDTO.class);

        if (admission.getUpdateBy() == null){
            fullAdmissionDTO.setCreateBy(modelMapper.map(userService.findById(admission.getCreateBy()), ActionerDTO.class));
            fullAdmissionDTO.setUpdateBy(null);
        } else if (Objects.equals(admission.getCreateBy(), admission.getUpdateBy())){
            ActionerDTO actionerDTO = modelMapper.map(userService.findById(admission.getCreateBy()), ActionerDTO.class);
            fullAdmissionDTO.setUpdateBy(actionerDTO);
            fullAdmissionDTO.setCreateBy(actionerDTO);
        } else {
            List<User> actioners = userService.findByIds(Arrays.asList(admission.getCreateBy(), admission.getUpdateBy()));
            fullAdmissionDTO.setUpdateBy(modelMapper.map( actioners.stream()
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
    public CreateAdmissionTrainingProgramResponse createAdmissionTrainingProgram(CreateAdmissionTrainingProgramRequest request){
        Admission admission =  this.findById(request.getAdmissionId());

        List<AdmissionTrainingProgram> admissionTrainingPrograms = admissionTrainingProgramService.saveAllAdmissionTrainingProgram(request.getAdmissionId(), request.getTrainingPrograms());

        return new CreateAdmissionTrainingProgramResponse(this.mapFullResponse(admission),
                admissionTrainingPrograms.stream()
                        .map(admissionTrainingProgram -> modelMapper.map(admissionTrainingProgram, CreateTrainingProgramRequest.class))
                        .toList());
    }

    //TODO: throw Exception
    //TODO: check if method existed in admission
    public CreateAdmissionMethodResponse createAdmissionMethod(CreateAdmissionMethodRequest request){
        Admission admission = this.findById(request.getAdmissionId());

        List<AdmissionMethod> admissionMethods = admissionMethodService.saveAllAdmissionMethod(request.getAdmissionId(), request.getMethodIds());

        List<Method> methods = methodService.findByIds(request.getMethodIds());
        return new CreateAdmissionMethodResponse(this.mapFullResponse(admission),
                methods.stream()
                        .map(admissionTrainingProgram -> modelMapper.map(admissionTrainingProgram, InfoMethodDTO.class))
                        .toList());
    }

    public ResponseData<CreateAdmissionTrainingProgramMethodQuotaResponse> createAdmissionTrainingProgramMethodQuota(CreateAdmissionTrainingProgramMethodRequest request){
        CreateAdmissionTrainingProgramMethodQuotaResponse response = new CreateAdmissionTrainingProgramMethodQuotaResponse();
        List<AdmissionTrainingProgramMethod> admissionTrainingProgramMethods = admissionTrainingProgramMethodService.createAdmissionTrainingProgramMethod(request);

        response.setQuotas(admissionTrainingProgramMethods
                .stream()
                .map(admissionTrainingProgramMethod -> new AdmissionTrainingProgramMethodQuotaDTO(admissionTrainingProgramMethod.getId().getAdmissionTrainingProgramId(), admissionTrainingProgramMethod.getId().getAdmissionMethodId(), admissionTrainingProgramMethod.getQuota()))
                .toList());
        return ResponseData.ok("Tạo chi tiết chỉ tiêu thành công",response);
    }

    public CreateAdmissionTrainingProgramSubjectGroupResponse createAdmissionTrainingProgramSubjectGroup(CreateAdmissionTrainingProgramSubjectGroupRequest request){
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

    public void createAdmission(CreateAdmissionRequest request){
        User consultant = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        Admission admission = this.createAdmission(request.getYear(), request.getDocuments(), consultant);

        List<AdmissionMethod> admissionMethods = admissionMethodService.saveAdmissionMethod( admission.getId(), request.getQuotas());

        List<AdmissionTrainingProgram> admissionTrainingPrograms = admissionTrainingProgramService.saveAdmissionTrainingProgram(admission.getId(), request.getQuotas());

        
    }

}