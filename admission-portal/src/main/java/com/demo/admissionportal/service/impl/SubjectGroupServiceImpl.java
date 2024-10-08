package com.demo.admissionportal.service.impl;

import com.demo.admissionportal.constants.ResponseCode;
import com.demo.admissionportal.constants.SubjectStatus;
import com.demo.admissionportal.controller.SubjectController;
import com.demo.admissionportal.dto.entity.ActionerDTO;
import com.demo.admissionportal.dto.request.CreateSubjectGroupRequestDTO;
import com.demo.admissionportal.dto.request.UpdateSubjectGroupRequestDTO;
import com.demo.admissionportal.dto.response.sub_entity.*;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.entity.StaffInfo;
import com.demo.admissionportal.entity.Subject;
import com.demo.admissionportal.entity.SubjectGroup;
import com.demo.admissionportal.entity.User;
import com.demo.admissionportal.entity.admission.AdmissionTrainingProgramMethod;
import com.demo.admissionportal.entity.sub_entity.SubjectGroupSubject;
import com.demo.admissionportal.repository.StaffInfoRepository;
import com.demo.admissionportal.repository.SubjectGroupRepository;
import com.demo.admissionportal.repository.SubjectRepository;
import com.demo.admissionportal.repository.UserRepository;
import com.demo.admissionportal.repository.sub_repository.SubjectGroupSubjectRepository;
import com.demo.admissionportal.service.SubjectGroupService;
import com.demo.admissionportal.util.impl.NameUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SubjectGroupServiceImpl implements SubjectGroupService {
    @Autowired
    private SubjectGroupRepository subjectGroupRepository;
    @Autowired
    private SubjectRepository subjectRepository;
    @Autowired
    private SubjectGroupSubjectRepository subjectGroupSubjectRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private StaffInfoRepository staffInfoRepository;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private SubjectServiceImpl subjectServiceImpl;

    @Override
    @Transactional
    public ResponseData<?> createSubjectGroup(CreateSubjectGroupRequestDTO request) {
        SubjectGroup existSubjectGroup = subjectGroupRepository.findByName(request.getName());
        if (existSubjectGroup != null) {
            log.warn("Subject group with name {} already exists", request.getName());
            return new ResponseData<>(ResponseCode.C204.getCode(), "Tổ hợp môn học đã tồn tại !");
        }

        List<Integer> subjectIds = request.getSubjectIds();
        if (subjectIds.size() < 3) {
            return new ResponseData<>(ResponseCode.C205.getCode(), "Phải có ít nhất 3 môn học để tạo thành tổ hợp !");
        }
        if (!validateSubjectIds(subjectIds)){
            return new ResponseData<>(ResponseCode.C205.getCode(),"Tổ hợp môn học không hợp lệ !");
        }
        List<Subject> subjects = subjectRepository.findAllById(subjectIds);
        if (subjects.size() != subjectIds.size()) {
            return new ResponseData<>(ResponseCode.C203.getCode(), "Tổ hợp môn học có chứa môn học không được tìm thấy !");
        }

        for (Subject subject : subjects) {
            if (!subject.getStatus().equals(SubjectStatus.ACTIVE)) {
                return new ResponseData<>(ResponseCode.C203.getCode(), "Tổ hợp môn học có chứa môn học không được tìm thấy !");
            }
        }
        List<SubjectGroup> allSubjectGroups = subjectGroupRepository.findAll();
        List<SubjectGroupResponseDTO> matchingSubjectGroups = new ArrayList<>();
        for (SubjectGroup group : allSubjectGroups) {
            List<Integer> groupSubjectIds = subjectGroupSubjectRepository.findBySubjectGroupId(group.getId()).stream().map(SubjectGroupSubject::getSubjectId).toList();
            if (new HashSet<>(groupSubjectIds).containsAll(subjectIds) && new HashSet<>(subjectIds).containsAll(groupSubjectIds)) {
                List<SubjectResponseDTO2> subjectDetails = subjectGroupSubjectRepository.findBySubjectGroupId(group.getId()).stream().map(sgs -> {
                    Subject subject = subjectRepository.findById(sgs.getSubjectId()).orElse(null);
                    if (subject != null) {
                        return modelMapper.map(subject, SubjectResponseDTO2.class);
                    } else {
                        return null;
                    }
                }).filter(Objects::nonNull).collect(Collectors.toList());
                SubjectGroupResponseDTO responseDTO = modelMapper.map(group, SubjectGroupResponseDTO.class);
                responseDTO.setSubjects(subjectDetails);
                responseDTO.setCreateBy(getUserDetails(group.getCreateBy()));
                matchingSubjectGroups.add(responseDTO);
            }
        }
        if (!matchingSubjectGroups.isEmpty()) {
            return new ResponseData<>(ResponseCode.C204.getCode(), "Đã tồn tại tổ hợp môn học với các môn học này !", matchingSubjectGroups);
        }
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Object principal = authentication.getPrincipal();
            log.debug("Principal type: {}", principal.getClass().getName());
            log.info("Principal type: {}", principal.getClass().getName());
            if (!(principal instanceof User staff)) {
                return new ResponseData<>(ResponseCode.C209.getCode(), "Người tham chiếu không hợp lệ !");
            }
            Integer staffId = staff.getId();
            List<SubjectGroupSubject> subjectGroupSubjects = new ArrayList<>();
            List<CreateSubjectResponseDTO> subjectResponses = new ArrayList<>();
            for (Subject subject : subjects) {
                subjectResponses.add(modelMapper.map(subject, CreateSubjectResponseDTO.class));
                SubjectGroupSubject subjectGroupSubject = new SubjectGroupSubject(subject.getId(), null);
                subjectGroupSubject.setStatus(SubjectStatus.ACTIVE.name());
                subjectGroupSubjects.add(subjectGroupSubject);
                log.info("Subjects are valid and accepted {}", subject.getId());
            }
            SubjectGroup subjectGroup = new SubjectGroup();
            subjectGroup.setName(request.getName());
            subjectGroup.setCreateTime(new Date());
            subjectGroup.setCreateBy(staffId);
            subjectGroup.setStatus(SubjectStatus.ACTIVE);
            subjectGroup = subjectGroupRepository.save(subjectGroup);
            log.info("New subject group created with ID {}", subjectGroup.getId());
            for (SubjectGroupSubject subjectGroupSubject : subjectGroupSubjects) {
                subjectGroupSubject.setSubjectGroupId(subjectGroup.getId());
                subjectGroupSubject.setCreateTime(new Date());
                subjectGroupSubject.setCreateBy(staffId);
                subjectGroupSubjectRepository.save(subjectGroupSubject);
                log.info("Saved mapping of subject {} with subject_group {} in subject_group_subject table", subjectGroupSubject.getSubjectId(), subjectGroup.getId());
            }
            CreateSubjectGroupResponseDTO createSubjectGroupResponseDTO = new CreateSubjectGroupResponseDTO(subjectGroup.getId(), subjectGroup.getName(), subjectGroup.getStatus().name(), subjectGroup.getCreateTime(), subjectGroup.getCreateBy(), subjectGroup.getUpdateTime(), subjectGroup.getUpdateBy(), subjectResponses);
            return new ResponseData<>(ResponseCode.C200.getCode(), "Tạo tổ hợp môn học thành công!", createSubjectGroupResponseDTO);
        } catch (Exception ex) {
            log.error("Error when create subject group", ex);
            return new ResponseData<>(ResponseCode.C201.getCode(), ResponseCode.C201.getMessage());
        }
    }

    private boolean validateSubjectIds(List<Integer> subjectIds) {
        if (subjectIds == null || subjectIds.isEmpty()) {
            return false;
        }
        Set<Integer> uniqueIds = new HashSet<>();
        for (Integer subjectId : subjectIds) {
            if (subjectId == null ) {
                return false;
            }
            if (!uniqueIds.add(subjectId)) {
                return false;
            }
        }
        return true;
    }


    @Override
    public ResponseData<?> updateSubjectGroup(Integer id, UpdateSubjectGroupRequestDTO request) {
        SubjectGroup existSubjectGroup = subjectGroupRepository.findById(id).orElse(null);
        if (existSubjectGroup == null) {
            log.warn("Subject group with id: {} not found", id);
            return new ResponseData<>(ResponseCode.C204.getCode(), "Tổ hợp môn học không tồn tại !");
        }
        if (existSubjectGroup.getStatus().equals(SubjectStatus.INACTIVE.name())) {
            log.warn("Subject group with id: {} not found", id);
            return new ResponseData<>(ResponseCode.C204.getCode(), "Tổ hợp môn học không tồn tại !");
        }
        try {
            log.info("Starting update process for Subject group with ID: {}", existSubjectGroup.getId());
            if (request.getName() != null && !request.getName().isEmpty()) {
                existSubjectGroup.setName(request.getName());
                log.info("Updated SubjectGroup name to {}", request.getName());
            }
            subjectGroupRepository.save(existSubjectGroup);
            if (request.getSubjectIds() != null && !request.getSubjectIds().isEmpty()) {
                List<SubjectGroupSubject> existingSubjects = subjectGroupSubjectRepository.findBySubjectGroupId(existSubjectGroup.getId());
                for (SubjectGroupSubject existingSubject : existingSubjects) {
                    if (!request.getSubjectIds().contains(existingSubject.getSubjectId())) {
                        subjectGroupSubjectRepository.delete(existingSubject);
                        log.info("Deleted relationship for subject ID {} from subject group ID {}", existingSubject.getSubjectId(), existSubjectGroup.getId());
                    }
                }
                for (Integer subjectId : request.getSubjectIds()) {
                    boolean exists = subjectGroupSubjectRepository.existsBySubjectIdAndSubjectGroupId(subjectId, existSubjectGroup.getId());
                    if (!exists) {
                        Subject subject = subjectRepository.findById(subjectId).orElse(null);
                        if (subject != null) {
                            SubjectGroupSubject sgs = new SubjectGroupSubject(subjectId, existSubjectGroup.getId());
                            sgs.setStatus(SubjectStatus.ACTIVE.name());
                            subjectGroupSubjectRepository.save(sgs);
                            log.info("Add new map list for subject ID {} to subject group ID {}", subjectId, existSubjectGroup.getId());
                        } else {
                            log.warn("Subject with id: {} not found !", subjectId);
                            return new ResponseData<>(ResponseCode.C203.getCode(), ResponseCode.C203.getMessage());
                        }
                    }
                }
            }
            log.info("Subject Group update successfully with ID: {}", existSubjectGroup.getId());
            List<SubjectResponseDTO2> subjectResponse = subjectGroupSubjectRepository.findBySubjectGroupId(existSubjectGroup.getId()).stream().map(subjectGroupSubject -> {
                Subject subject = subjectRepository.findById(subjectGroupSubject.getSubjectId()).orElse(null);
                if (subject != null) {
                    return modelMapper.map(subject, SubjectResponseDTO2.class);
                }
                return null;
            }).filter(Objects::nonNull).collect(Collectors.toList());
            SubjectGroupResponseDTO result = modelMapper.map(existSubjectGroup, SubjectGroupResponseDTO.class);
            result.setSubjects(subjectResponse);
            result.setCreateBy(getUserDetails(existSubjectGroup.getCreateBy()));
            return new ResponseData<>(ResponseCode.C200.getCode(), "Cập nhật tổ hợp môn học thành công !", result);
        } catch (Exception e) {
            log.error("Error occurred while updating SubjectGroup: {}", e.getMessage());
            return new ResponseData<>(ResponseCode.C201.getCode(), "Cập nhật tổ hợp môn học thất bại !");
        }
    }


    @Override
    public ResponseData<?> getSubjectGroupById(Integer id) {
        Optional<SubjectGroup> subjectGroup = subjectGroupRepository.findById(id);
        if (subjectGroup.isEmpty()) {
            return new ResponseData<>(ResponseCode.C203.getCode(), "Nhóm môn học không được tìm thấy !");
        } else {
            SubjectGroup getSubjectGroup = subjectGroup.get();
            List<SubjectResponseDTO2> subjectResponse = subjectGroupSubjectRepository.findBySubjectGroupId(getSubjectGroup.getId()).stream().map(subjectGroupSubject -> {
                Subject subject = subjectRepository.findById(subjectGroupSubject.getSubjectId()).orElse(null);
                if (subject != null) {
                    return modelMapper.map(subject, SubjectResponseDTO2.class);
                }
                return null;
            }).filter(Objects::nonNull).collect(Collectors.toList());
            SubjectGroupResponseDTO result = modelMapper.map(getSubjectGroup, SubjectGroupResponseDTO.class);
            result.setSubjects(subjectResponse);
            result.setCreateBy(getUserDetails(getSubjectGroup.getCreateBy()));
            return new ResponseData<>(ResponseCode.C200.getCode(), ResponseCode.C200.getMessage(), result);
        }
    }


    @Override
    public ResponseData<Page<SubjectGroupResponseDTO>> findAll(String groupName, String subjectName, SubjectStatus status, Pageable pageable) {
        log.info("Get all subject groups");
        String statusString = status != null ? status.name() : null;
        Page<SubjectGroup> subjectGroupPage = subjectGroupRepository.findAll(groupName, subjectName, statusString, pageable);

        List<SubjectGroupResponseDTO> subjectGroupResponses = subjectGroupPage.getContent().stream().map(subjectGroup -> {
            List<SubjectResponseDTO2> subjectDetails = subjectGroupSubjectRepository.findBySubjectGroupId(subjectGroup.getId()).stream().map(sgs -> {
                Subject subject = subjectRepository.findById(sgs.getSubjectId()).orElse(null);
                if (subject != null) {
                    return modelMapper.map(subject, SubjectResponseDTO2.class);
                } else {
                    return null;
                }
            }).filter(Objects::nonNull).collect(Collectors.toList());
            SubjectGroupResponseDTO responseDTO = modelMapper.map(subjectGroup, SubjectGroupResponseDTO.class);
            responseDTO.setSubjects(subjectDetails);
            responseDTO.setCreateBy(getUserDetails(subjectGroup.getCreateBy()));
            return responseDTO;
        }).collect(Collectors.toList());

        Page<SubjectGroupResponseDTO> subjectGroupResponsePage = new PageImpl<>(subjectGroupResponses, pageable, subjectGroupPage.getTotalElements());

        return new ResponseData<>(ResponseCode.C200.getCode(), ResponseCode.C200.getMessage(), subjectGroupResponsePage);
    }


    @Override
    public ResponseData<?> deleteSubjectGroup(Integer id) {
        try {
            SubjectGroup subjectGroup = subjectGroupRepository.findById(id).orElse(null);
            if (subjectGroup == null || subjectGroup.getStatus().equals(SubjectStatus.INACTIVE)) {
                return new ResponseData<>(ResponseCode.C203.getCode(), "Tổ hợp môn học không được tìm thấy !");
            }
            subjectGroup.setStatus(SubjectStatus.INACTIVE);
            subjectGroupRepository.save(subjectGroup);
            return new ResponseData<>(ResponseCode.C200.getCode(), "Tổ hợp môn học được xóa thành công !");
        } catch (Exception e) {
            return new ResponseData<>(ResponseCode.C201.getCode(), "Đã xảy ra lỗi trong quá trình xóa tổ hợp môn học !");
        }
    }

    @Override
    public ResponseData<?> activateSubjectGroup(Integer id) {
        try {
            Optional<SubjectGroup> optionalSubjectGroup = subjectGroupRepository.findById(id);
            if (optionalSubjectGroup.isEmpty()) {
                return new ResponseData<>(ResponseCode.C203.getCode(), "Tổ hợp môn học không được tìm thấy !");
            }

            SubjectGroup subjectGroup = optionalSubjectGroup.get();
            List<SubjectGroupSubject> subjectGroupSubjects = subjectGroupSubjectRepository.findBySubjectGroupId(subjectGroup.getId());

            for (SubjectGroupSubject sgs : subjectGroupSubjects) {
                Subject subject = subjectRepository.findById(sgs.getSubjectId()).orElse(null);
                if (subject == null || subject.getStatus() == SubjectStatus.INACTIVE) {
                    return new ResponseData<>(ResponseCode.C204.getCode(), "Không thể kích hoạt tổ hợp môn học khi có môn học không hoạt động !");
                }
            }

            subjectGroup.setStatus(SubjectStatus.ACTIVE);
            subjectGroupRepository.save(subjectGroup);

            return new ResponseData<>(ResponseCode.C200.getCode(), "Kích hoạt tổ hợp môn học thành công !");
        } catch (Exception e) {
            log.error("Error occurred while activating SubjectGroup: {}", e.getMessage());
            return new ResponseData<>(ResponseCode.C201.getCode(), "Đã xảy ra lỗi trong quá trình kích hoạt tổ hợp môn học !");
        }
    }

    public List<SubjectGroup> findAllByIds(List<Integer> subjectGroupIds) {
        List<SubjectGroup> result = null;
        try {
            result = subjectGroupRepository.findAllById(subjectGroupIds);
        } catch (Exception e) {
            //TODO: throw exception
        }
        return result;
    }

//    public List<SubjectGroup> findByAdmissionTrainingProgramIdAndSubjectId(List<Integer> subjectIds, List<Integer> admissionTrainingProgramIds) {
//        List<SubjectGroup> result = null;
//        try {
//            result = subjectGroupRepository.findByAdmissionTrainingProgramIdAndSubjectId(subjectIds, admissionTrainingProgramIds);
//        } catch (Exception e) {
//            //TODO: throw exception
//        }
//        return result;
//    }

    private ActionerDTO getUserDetails(Integer userId) {
        return userRepository.findById(userId)
                .map(user -> {
                    StaffInfo staffInfo = staffInfoRepository.findById(userId).orElse(null);
                    String fullName;
                    if (staffInfo != null) {
                        fullName = NameUtils.getFullName(staffInfo.getFirstName(), staffInfo.getMiddleName(), staffInfo.getLastName());
                    } else {
                        fullName = "Nhân viên UAP";
                    }
                    ActionerDTO actionerDTO = new ActionerDTO(user.getId(), fullName, null, null);
                    actionerDTO.setRole(modelMapper.map(user.getRole(), String.class));
                    actionerDTO.setStatus(modelMapper.map(user.getStatus(), String.class));
                    return actionerDTO;
                }).orElse(null);
    }

    public List<SubjectGroup> findByAdmissionTrainingProgramIds(List<Integer> admissionTrainingProgramIds) {
        return subjectGroupRepository.findByAdmissionTrainingProgramId(admissionTrainingProgramIds);
    }

    public List<SubjectGroupResponseDTO2> getByAdmissionTrainingProgramIds(List<Integer> admissionTrainingProgramIds) {
        List<SubjectGroup> list = findByAdmissionTrainingProgramIds(admissionTrainingProgramIds);
        List<SubjectGroupResponseDTO2> result = list.stream().map(SubjectGroupResponseDTO2::new).collect(Collectors.toList());
        return result;
    }

    public List<SubjectGroupResponseDTO2> mapInfo(List<SubjectGroup> subjectGroupForMap) {
        return subjectGroupForMap.stream().map((element) -> modelMapper.map(element, SubjectGroupResponseDTO2.class)).toList();
    }

    public List<SubjectGroupResponseDTO2> getAll(){
        List<SubjectGroup> subjectGroupList = subjectGroupRepository.findAll();
        return subjectGroupList.stream().map(SubjectGroupResponseDTO2::new).collect(Collectors.toList());
    }


    public List<SubjectGroup> findSubjectGroup(List<Integer> subjectIds, List<Integer> admissionTrainingProgramIds){
        // 1. Start building the base query
        StringBuilder queryBuilder = new StringBuilder(
                "select distinct sg.*\n" +
                        "from subject_group sg\n" +
                        "inner join subject_group_subject sgs on sgs.subject_group_id = sg.id\n" +
                        "inner join admission_training_program_subject_group atpsg on sg.id = atpsg.subject_group_id\n" +
                        "inner join subject s on s.id = sgs.subject_id\n" +
                        " where s.status = 'ACTIVE' \n"
        );

        Map<String, Object> parameters = new HashMap<>();

        if (subjectIds != null && !subjectIds.isEmpty()) {
            List<String> placeholders = new ArrayList<>();
            for (int i = 0; i < subjectIds.size(); i++) {
                placeholders.add(":subjectIds" + i);
            }

            queryBuilder.append(" AND s.id IN (")
                    .append(String.join(",", placeholders))
                    .append(") ");

            for (int i = 0; i < subjectIds.size(); i++) {
                parameters.put("subjectIds" + i, subjectIds.get(i));
            }
        }

        if (admissionTrainingProgramIds != null && !admissionTrainingProgramIds.isEmpty()) {
            List<String> placeholders = new ArrayList<>();
            for (int i = 0; i < admissionTrainingProgramIds.size(); i++) {
                placeholders.add(":admissionTrainingProgramIds" + i);
            }

            queryBuilder.append(" AND atpsg.admission_training_program_id IN (")
                    .append(String.join(",", placeholders))
                    .append(") ");

            for (int i = 0; i < admissionTrainingProgramIds.size(); i++) {
                parameters.put("admissionTrainingProgramIds" + i, admissionTrainingProgramIds.get(i));
            }
        }

        // 5. Create and execute the TypedQuery
        Query a = entityManager.createNativeQuery(queryBuilder.toString(), SubjectGroup.class);

        // 6. Set the parameters
        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            a.setParameter(entry.getKey(), entry.getValue());
        }

        // 7. Execute and return the results
        List<SubjectGroup> b = (List<SubjectGroup>) a.getResultList();
        return b;
    }

    public List<SubjectGroupResponseDTO2> getByMajorIdAndUniversityId(Integer majorId, Integer universityId, Integer year){
        List<SubjectGroup> subjectGroups = subjectGroupRepository.findByMajorIdAndUniversityIdAndYear(majorId, universityId, year);
        return mapping(subjectGroups);
    }

    public List<SubjectGroupResponseDTO2> mapping(List<SubjectGroup> subjectGroups){
        List<SubjectGroupSubject> subjectGroupSubjects = subjectGroupSubjectRepository.findBySubjectGroupIdIn(subjectGroups.stream().map(SubjectGroup::getId).collect(Collectors.toList()));
        List<Integer> subjectIds = subjectGroupSubjects.stream().map(SubjectGroupSubject::getSubjectId).distinct().toList();
        List<Subject> subjects = subjectRepository.findAllById(subjectIds);
        List<SubjectGroupResponseDTO2> result = new ArrayList<>();
        for (SubjectGroup subjectGroup: subjectGroups){
            SubjectGroupResponseDTO2 subjectGroupResponseDTO2 = new SubjectGroupResponseDTO2(subjectGroup);
            List<Integer> subjectIds2 = subjectGroupSubjects.stream().filter(sgs -> sgs.getSubjectGroupId().equals(subjectGroup.getId())).map(SubjectGroupSubject::getSubjectId).distinct().toList();

            List<Subject> subjects1 = subjects.stream().filter(subject -> subjectIds2.contains(subject.getId())).toList();
            List<SubjectResponseDTO2> subjectResponseDTO2s = subjects1.stream().map(SubjectResponseDTO2::new).toList();
            subjectGroupResponseDTO2.setSubjects(subjectResponseDTO2s);
            result.add(subjectGroupResponseDTO2);
        }
        return result;
    }

    public List<SubjectGroupResponseDTO2> getBySubjectGroupIdsAndMappingToSubjectGroupResponseDTO2(List<Integer> subjectGroupIds){
        List<SubjectGroup> subjectGroups = subjectGroupRepository.findAllById(subjectGroupIds);
        return mapping(subjectGroups);
    }

    public List<Subject> getSubjectsBySubjectGroupIds(List<SubjectGroup> subjectGroups){
        List<SubjectGroupSubject> subjectGroupSubjects = subjectGroupSubjectRepository.findBySubjectGroupIdIn(subjectGroups.stream().map(SubjectGroup::getId).collect(Collectors.toList()));
        List<Integer> subjectIds = subjectGroupSubjects.stream().map(SubjectGroupSubject::getSubjectId).distinct().toList();
        return subjectRepository.findAllById(subjectIds);
    }
}
