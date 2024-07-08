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
        // Check if subject group already exists
        if (existSubjectGroup != null) {
            log.warn("Subject group with name {} already exists", request.getName());
            return new ResponseData<>(ResponseCode.C203.getCode(), "Tổ hợp môn học đã tồn tại !");
        }
        // Check for active subjects
        List<Subject> activeSubjects = subjectRepository.findByStatus(SubjectStatus.ACTIVE);
        if (activeSubjects.isEmpty()) {
            return new ResponseData<>(ResponseCode.C203.getCode(), "Không có môn học hoạt động nào tồn tại !");
        }
        try {
            //create principle
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Object principal = authentication.getPrincipal();

            log.debug("Principal type: {}", principal.getClass().getName());
            log.info("Principal type: {}", principal.getClass().getName());
            if (!(principal instanceof User)) {
                return new ResponseData<>(ResponseCode.C205.getCode(), "Người tham chiếu không hợp lệ !");
            }
            User staff = (User) principal;
            Integer staffId = staff.getId();

            // Create lists to hold data
            List<SubjectGroupSubject> subjectGroupSubjects = new ArrayList<>();
            List<CreateSubjectResponseDTO> subjectResponses = new ArrayList<>();
            // Check for existing subjects
            for (Integer subjectId : request.getSubjectIds()) {
                Subject subject = subjectRepository.findById(subjectId).orElse(null);
                log.info("Check valid subjects {}", subjectId);
                // If subject exists, add its data to the response DTO
                if (subject != null) {
                    subjectResponses.add(new CreateSubjectResponseDTO(subject.getId(), subject.getName(), subject.getCreateTime(), subject.getCreateBy(), subject.getUpdateBy(), subject.getStatus().name()));
                    // Add to SubjectGroupSubject
                    SubjectGroupSubject subjectGroupSubject = new SubjectGroupSubject(subjectId, null);
                    subjectGroupSubject.setStatus(SubjectStatus.ACTIVE.name());
                    subjectGroupSubjects.add(subjectGroupSubject);
                    log.info("Subjects are valid and accepted {}", subjectId);
                } else {
                    log.error("Subjects not found {}", subjectId);
                    return new ResponseData<>(ResponseCode.C203.getCode(), "Môn học không tìm thấy !");
                }
            }
            // Create and save new SubjectGroup
            SubjectGroup subjectGroup = new SubjectGroup();
            subjectGroup.setName(request.getName());
            subjectGroup.setCreateTime(new Date());
            subjectGroup.setCreateBy(staffId);
            subjectGroup.setStatus(SubjectStatus.ACTIVE.name());
            subjectGroup = subjectGroupRepository.save(subjectGroup);
            log.info("New subject group created with ID {}", subjectGroup.getId());


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
            //check if update status
            if (request.getStatus() != null && !request.getStatus().isEmpty()) {
                existSubjectGroup.setStatus(request.getStatus());
                log.info("Updated SubjectGroup status to {}", request.getStatus());
            }
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
            List<SubjectResponseDTO> subjectResponse = subjectGroupSubjectRepository.findBySubjectGroupId(existSubjectGroup.getId())
                    .stream()
                    .map(subjectGroupSubject -> {
                        Optional<Subject> subject = subjectRepository.findById(subjectGroupSubject.getSubjectId());
                        if (subject.isPresent()){
                            return new SubjectResponseDTO(subject.get().getId(), subject.get().getName(), subject.get().getStatus().name());
                        }
                        return null;
                    })
                    .toList();
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
        if(subjectGroup.isEmpty()){
            return new ResponseData<>(ResponseCode.C203.getCode(), "Nhóm môn học không được tìm thấy !");
        } else {
            //set subject data to add in subject response
            SubjectGroup getSubjectGroup = subjectGroup.get();
            List<SubjectResponseDTO> subjectResponse = subjectGroupSubjectRepository.findBySubjectGroupId(getSubjectGroup.getId())
                    .stream()
                    .map(subjectGroupSubject -> {
                        Subject subject = subjectRepository.findById(subjectGroupSubject.getSubjectId()).orElse(null);
                        if (subject != null){
                            return new SubjectResponseDTO(subject.getId(), subject.getName(), subject.getStatus().name());
                        }
                        return null;
                    })
                    .toList();
            SubjectGroupResponseDTO result = modelMapper.map(getSubjectGroup, SubjectGroupResponseDTO.class);
            //the update subject group response will include subject data
            result.setSubjects(subjectResponse);
            return new ResponseData<>(ResponseCode.C200.getCode(), ResponseCode.C200.getMessage(), result);
        }
    }

    @Override
    public ResponseData<Page<SubjectGroupResponseDTO>> findAll(Pageable pageable) {
        log.info("Get all subject groups");

        Page<SubjectGroup> subjectGroupPage = subjectGroupRepository.findAll(pageable);

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
        } catch (Exception e){
            return new ResponseData<>(ResponseCode.C201.getCode(), "Đã xảy ra lỗi trong quá trình xóa tổ hợp môn học !");
        }
    }
}
