package com.demo.admissionportal.service.impl;

import com.demo.admissionportal.constants.ResponseCode;
import com.demo.admissionportal.constants.SubjectStatus;
import com.demo.admissionportal.dto.request.CreateSubjectGroupRequestDTO;
import com.demo.admissionportal.dto.request.UpdateSubjectGroupRequestDTO;
import com.demo.admissionportal.dto.response.CreateSubjectGroupResponseDTO;
import com.demo.admissionportal.dto.response.CreateSubjectResponseDTO;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.dto.response.SubjectGroupResponseDTO;
import com.demo.admissionportal.dto.response.sub_entity.SubjectResponseDTO;
import com.demo.admissionportal.entity.Subject;
import com.demo.admissionportal.entity.SubjectGroup;
import com.demo.admissionportal.entity.User;
import com.demo.admissionportal.entity.sub_entity.SubjectGroupSubject;
import com.demo.admissionportal.repository.SubjectGroupRepository;
import com.demo.admissionportal.repository.SubjectRepository;
import com.demo.admissionportal.repository.sub_repository.SubjectGroupSubjectRepository;
import com.demo.admissionportal.service.SubjectGroupService;
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
    private ModelMapper modelMapper;

    @Override
    @Transactional
    public ResponseData<?> createSubjectGroup(CreateSubjectGroupRequestDTO request) {
        SubjectGroup existSubjectGroup = subjectGroupRepository.findByName(request.getName());
        // check subject_group already exists
        if (existSubjectGroup != null) {
            log.warn("Subject group with name {} already exists", request.getName());
            return new ResponseData<>(ResponseCode.C203.getCode(), "Tổ hợp môn học đã tồn tại !");
        }
        // Check exist and active/inactive subjects
        List<Integer> subjectIds = request.getSubjectIds();
        if (subjectIds.size() < 3) {
            return new ResponseData<>(ResponseCode.C204.getCode(), "Phải có ít nhất 3 môn học để tạo thành tổ hợp !");
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
        //check exist three subjects that have been create a subject_group before
        List<SubjectGroup> allSubjectGroups = subjectGroupRepository.findAll();
        List<SubjectGroupResponseDTO> matchingSubjectGroups = new ArrayList<>();
        for (SubjectGroup group : allSubjectGroups) {
            List<Integer> groupSubjectIds = subjectGroupSubjectRepository.findBySubjectGroupId(group.getId()).stream().map(SubjectGroupSubject::getSubjectId).toList();
            if (new HashSet<>(groupSubjectIds).containsAll(subjectIds) && new HashSet<>(subjectIds).containsAll(groupSubjectIds)) {
                List<SubjectResponseDTO> subjectDetails = subjectGroupSubjectRepository.findBySubjectGroupId(group.getId()).stream().map(sgs -> {
                    Subject subject = subjectRepository.findById(sgs.getSubjectId()).orElse(null);
                    if (subject != null) {
                        return new SubjectResponseDTO(subject.getId(), subject.getName(), subject.getStatus().name());
                    } else {
                        return null;
                    }
                }).filter(Objects::nonNull).collect(Collectors.toList());
                // add to matching list
                matchingSubjectGroups.add(new SubjectGroupResponseDTO(group.getId(), group.getName(), group.getStatus(), subjectDetails));
            }
        }
        // check if matching list not null then throw message and show the subject_group contain three subjects that has a subject_group before
        if (!matchingSubjectGroups.isEmpty()) {
            return new ResponseData<>(ResponseCode.C204.getCode(), "Đã tồn tại tổ hợp môn học với các môn học này !", matchingSubjectGroups);
        }
        try {
            // Create principal
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Object principal = authentication.getPrincipal();
            log.debug("Principal type: {}", principal.getClass().getName());
            log.info("Principal type: {}", principal.getClass().getName());
            if (!(principal instanceof User staff)) {
                return new ResponseData<>(ResponseCode.C205.getCode(), "Người tham chiếu không hợp lệ !");
            }
            Integer staffId = staff.getId();
            // create a null aray list to store the data processing before
            List<SubjectGroupSubject> subjectGroupSubjects = new ArrayList<>();
            List<CreateSubjectResponseDTO> subjectResponses = new ArrayList<>();
            for (Subject subject : subjects) {
                subjectResponses.add(new CreateSubjectResponseDTO(subject.getId(), subject.getName(), subject.getCreateTime(), subject.getCreateBy(), subject.getUpdateBy(), subject.getStatus().name()));
                // save to subject_group_subject
                SubjectGroupSubject subjectGroupSubject = new SubjectGroupSubject(subject.getId(), null);
                subjectGroupSubject.setStatus(SubjectStatus.ACTIVE.name());
                subjectGroupSubjects.add(subjectGroupSubject);
                log.info("Subjects are valid and accepted {}", subject.getId());
            }
            // create new object subject_group and save new subject_group
            SubjectGroup subjectGroup = new SubjectGroup();
            subjectGroup.setName(request.getName());
            subjectGroup.setCreateTime(new Date());
            subjectGroup.setCreateBy(staffId);
            subjectGroup.setStatus(SubjectStatus.ACTIVE.name());
            subjectGroup = subjectGroupRepository.save(subjectGroup);
            log.info("New subject group created with ID {}", subjectGroup.getId());
            // add to table subject_group_subject
            for (SubjectGroupSubject subjectGroupSubject : subjectGroupSubjects) {
                subjectGroupSubject.setSubjectGroupId(subjectGroup.getId());
                subjectGroupSubject.setCreateTime(new Date());
                subjectGroupSubject.setCreateBy(staffId);
                subjectGroupSubject.setStatus(SubjectStatus.ACTIVE.name());
                subjectGroupSubjectRepository.save(subjectGroupSubject);
                log.info("Saved mapping of subject {} with subject_group {} in subject_group_subject table", subjectGroupSubject.getSubjectId(), subjectGroup.getId());
            }
            CreateSubjectGroupResponseDTO createSubjectGroupResponseDTO = new CreateSubjectGroupResponseDTO(subjectGroup.getId(), subjectGroup.getName(), subjectGroup.getStatus(), subjectGroup.getCreateTime(), subjectGroup.getCreateBy(), subjectGroup.getUpdateTime(), subjectGroup.getUpdateBy(), subjectResponses);
            return new ResponseData<>(ResponseCode.C200.getCode(), "Tạo tổ hợp môn học thành công!", createSubjectGroupResponseDTO);
        } catch (Exception ex) {
            log.error("Error when create subject group", ex);
            return new ResponseData<>(ResponseCode.C201.getCode(), ResponseCode.C201.getMessage());
        }
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
            //check if update name
            log.info("Starting update process for Subject group with ID: {}", existSubjectGroup.getId());
            if (request.getName() != null && !request.getName().isEmpty()) {
                existSubjectGroup.setName(request.getName());
                log.info("Updated SubjectGroup name to {}", request.getName());
            }
//            //check if update status
//            if (request.getStatus() != null && !request.getStatus().isEmpty()) {
//                existSubjectGroup.setStatus(request.getStatus());
//                log.info("Updated SubjectGroup status to {}", request.getStatus());
//            }
            //save to database
            subjectGroupRepository.save(existSubjectGroup);
            //when update with new subject, delete current relationship of that subject group id
            if (request.getSubjectIds() != null && !request.getSubjectIds().isEmpty()) {
                List<SubjectGroupSubject> existingSubjects = subjectGroupSubjectRepository.findBySubjectGroupId(existSubjectGroup.getId());
                for (SubjectGroupSubject existingSubject : existingSubjects) {
                    if (!request.getSubjectIds().contains(existingSubject.getSubjectId())) {
                        subjectGroupSubjectRepository.delete(existingSubject);
                        log.info("Deleted relationship for subject ID {} from subject group ID {}", existingSubject.getSubjectId(), existSubjectGroup.getId());
                    }
                }
                //Check subject exist with typed subjectId
                for (Integer subjectId : request.getSubjectIds()) {
                    //check relationship between subject and subjectGroupId
                    boolean exists = subjectGroupSubjectRepository.existsBySubjectIdAndSubjectGroupId(subjectId, existSubjectGroup.getId());
                    if (!exists) {
                        //if not exist, find the subject by id
                        Subject subject = subjectRepository.findById(subjectId).orElse(null);
                        if (subject != null) {
                            //if subject is exist, add into subject_group_subject table
                            subjectGroupSubjectRepository.save(new SubjectGroupSubject(subjectId, existSubjectGroup.getId()));
                            log.info("Add new map list for subject ID {} to subject group ID {}", subjectId, existSubjectGroup.getId());
                        } else {
                            //if not exist return message
                            log.warn("Subject with id: {} not found !", subjectId);
                            return new ResponseData<>(ResponseCode.C203.getCode(), ResponseCode.C203.getMessage());
                        }
                    }
                }
            }
            //map subject with subject group
            log.info("Subject Group update successfully with ID: {}", existSubjectGroup.getId());
            List<SubjectResponseDTO> subjectResponse = subjectGroupSubjectRepository.findBySubjectGroupId(existSubjectGroup.getId()).stream().map(subjectGroupSubject -> {
                Optional<Subject> subject = subjectRepository.findById(subjectGroupSubject.getSubjectId());
                if (subject.isPresent()) {
                    return new SubjectResponseDTO(subject.get().getId(), subject.get().getName(), subject.get().getStatus().name());
                }
                return null;
            }).toList();
            //add subject group
            SubjectGroupResponseDTO result = modelMapper.map(existSubjectGroup, SubjectGroupResponseDTO.class);
            //the subject_group will include subject in response
            result.setSubjects(subjectResponse);
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
            //set subject data to add in subject response
            SubjectGroup getSubjectGroup = subjectGroup.get();
            List<SubjectResponseDTO> subjectResponse = subjectGroupSubjectRepository.findBySubjectGroupId(getSubjectGroup.getId()).stream().map(subjectGroupSubject -> {
                Subject subject = subjectRepository.findById(subjectGroupSubject.getSubjectId()).orElse(null);
                if (subject != null) {
                    return new SubjectResponseDTO(subject.getId(), subject.getName(), subject.getStatus().name());
                }
                return null;
            }).toList();
            SubjectGroupResponseDTO result = modelMapper.map(getSubjectGroup, SubjectGroupResponseDTO.class);
            //the update subject group response will include subject data
            result.setSubjects(subjectResponse);
            return new ResponseData<>(ResponseCode.C200.getCode(), ResponseCode.C200.getMessage(), result);
        }
    }

    @Override
    public ResponseData<Page<SubjectGroupResponseDTO>> findAll(String groupName, String subjectName, String status, Pageable pageable) {
        log.info("Get all subject groups");

        Page<SubjectGroup> subjectGroupPage = subjectGroupRepository.findAll(groupName, subjectName, status, pageable);

        List<SubjectGroupResponseDTO> subjectGroupResponses = subjectGroupPage.getContent().stream().map(subjectGroup -> {
            List<SubjectResponseDTO> subjectDetails = subjectGroupSubjectRepository.findBySubjectGroupId(subjectGroup.getId()).stream().map(sgs -> {
                Subject subject = subjectRepository.findById(sgs.getSubjectId()).orElse(null);
                if (subject != null) {
                    return new SubjectResponseDTO(subject.getId(), subject.getName(), subject.getStatus().name());
                } else {
                    return null;
                }
            }).filter(Objects::nonNull).collect(Collectors.toList());

            return new SubjectGroupResponseDTO(subjectGroup.getId(), subjectGroup.getName(), subjectGroup.getStatus(), subjectDetails);
        }).collect(Collectors.toList());

        Page<SubjectGroupResponseDTO> subjectGroupResponsePage = new PageImpl<>(subjectGroupResponses, pageable, subjectGroupPage.getTotalElements());

        return new ResponseData<>(ResponseCode.C200.getCode(), ResponseCode.C200.getMessage(), subjectGroupResponsePage);
    }

    @Override
    public ResponseData<?> deleteSubjectGroup(Integer id) {
        try {
            SubjectGroup subjectGroup = subjectGroupRepository.findById(id).orElse(null);
            if (subjectGroup == null || subjectGroup.getStatus().equals(SubjectStatus.INACTIVE.name())) {
                return new ResponseData<>(ResponseCode.C203.getCode(), "Tổ hợp môn học không được tìm thấy !");
            }
            subjectGroup.setStatus(SubjectStatus.INACTIVE.name());
            subjectGroupRepository.save(subjectGroup);
            return new ResponseData<>(ResponseCode.C203.getCode(), "Tổ hợp môn học được xóa thành công !");
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

            subjectGroup.setStatus(SubjectStatus.ACTIVE.name());
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
}
