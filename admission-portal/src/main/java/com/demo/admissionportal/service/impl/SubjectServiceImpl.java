package com.demo.admissionportal.service.impl;

import com.demo.admissionportal.constants.ResponseCode;
import com.demo.admissionportal.constants.SubjectStatus;
import com.demo.admissionportal.dto.request.RequestSubjectDTO;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.dto.response.sub_entity.SubjectResponseDTO;
import com.demo.admissionportal.entity.Subject;
import com.demo.admissionportal.entity.SubjectGroup;
import com.demo.admissionportal.entity.User;
import com.demo.admissionportal.entity.sub_entity.SubjectGroupSubject;
import com.demo.admissionportal.exception.ResourceNotFoundException;
import com.demo.admissionportal.repository.SubjectGroupRepository;
import com.demo.admissionportal.repository.SubjectRepository;
import com.demo.admissionportal.repository.sub_repository.SubjectGroupSubjectRepository;
import com.demo.admissionportal.service.SubjectService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * The type Subject service.
 */
@Service
@AllArgsConstructor
@Slf4j
public class SubjectServiceImpl implements SubjectService {
    private final SubjectRepository subjectRepository;
    private final SubjectGroupRepository subjectGroupRepository;
    private final ModelMapper modelMapper;
    private final SubjectGroupSubjectRepository subjectGroupSubjectRepository;

    @Override
    public ResponseData<Page<SubjectResponseDTO>> findAll(String name, SubjectStatus status, Pageable pageable) {
        try {
            String statusString = status != null ? status.name() : null;
            Page<Subject> subjects = subjectRepository.findAll(name, statusString, pageable);
            Page<SubjectResponseDTO> subjectResponseDTOs = subjects.map(subject -> new SubjectResponseDTO(
                    subject.getId(),
                    subject.getName(),
                    subject.getStatus().name()
            ));
            return new ResponseData<>(ResponseCode.C200.getCode(), "Danh sách môn học đã được hiển thị !", subjectResponseDTOs);
        } catch (Exception ex) {
            log.error("Error occurred while fetching subjects: {}", ex.getMessage());
            return new ResponseData<>(ResponseCode.C201.getCode(), "Có lỗi trong quá trình lấy ra danh sách môn học !");
        }
    }
    @Override
    public ResponseData<Subject> getSubjectById(Integer id) {
        try {
            Optional<Subject> subject = subjectRepository.findById(id);
            if (subject.isPresent()) {
                return new ResponseData<>(ResponseCode.C200.getCode(), "Môn học được tìm thấy", subject.get());
            } else {
                return new ResponseData<>(ResponseCode.C203.getCode(), "Môn học không được tìm thấy", null);
            }
        } catch (Exception ex) {
            log.error("Error occurred while fetching subject by ID: {}", ex.getMessage());
            return new ResponseData<>(ResponseCode.C201.getCode(), "Đã xảy ra lỗi trong quá trình lấy môn học", null);
        }
    }

    @Override
    public ResponseData<Subject> createSubject(RequestSubjectDTO requestSubjectDTO) {
        try {
            if (!Objects.isNull(requestSubjectDTO)) {
                Subject checkExisted = subjectRepository.findSubjectByName(requestSubjectDTO.getName().trim());
                if (checkExisted != null) {
                    log.error("Subject {} is already existed", requestSubjectDTO.getName());
                    return new ResponseData<>(ResponseCode.C204.getCode(), "Môn học " + requestSubjectDTO.getName() + " đã tồn tại", null);
                }
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                Object principal = authentication.getPrincipal();

                log.debug("Principal type: {}", principal.getClass().getName());
                log.info("Principal type: {}", principal.getClass().getName());
                if (!(principal instanceof User)) {
                    return new ResponseData<>(ResponseCode.C205.getCode(), "Người tham chiếu không hợp lệ !");
                }
                User staff = (User) principal;
                Integer staffId = staff.getId();
                Subject subject = modelMapper.map(requestSubjectDTO, Subject.class);
                subject.setCreateTime(new Date());
                subject.setCreateBy(staffId);
                subject.setStatus(SubjectStatus.ACTIVE);
                Subject createSubject = subjectRepository.save(subject);
                log.info("Subject {} is successfully added", requestSubjectDTO.getName());
                return new ResponseData<>(ResponseCode.C200.getCode(), "Tạo môn học thành công", createSubject);
            }
        } catch (Exception ex) {
            log.error("Error occurred while creating subject: {}", ex.getMessage());
        }
        return new ResponseData<>(ResponseCode.C201.getCode(), "Xuất hiện lỗi khi tạo môn học", null);
    }
    @Override
    public ResponseData<?> deleteSubject(Integer id) {
        try {
            Subject subject = subjectRepository.findById(id).orElse(null);
            if (subject == null || subject.getStatus().equals(SubjectStatus.INACTIVE)) {
                return new ResponseData<>(ResponseCode.C203.getCode(), "Môn học không được tìm thấy !");
            }
            List<SubjectGroupSubject> subjectGroupSubjects = subjectGroupSubjectRepository.findBySubjectId(id);
            List<SubjectGroup> activeSubjectGroups = new ArrayList<>();

            for (SubjectGroupSubject sgs : subjectGroupSubjects) {
                SubjectGroup subjectGroup = subjectGroupRepository.findById(sgs.getSubjectGroupId()).orElse(null);
                if (subjectGroup != null && subjectGroup.getStatus().equals(SubjectStatus.ACTIVE.name())) {
                    activeSubjectGroups.add(subjectGroup);
                }
            }
            if (!activeSubjectGroups.isEmpty()) {
                return new ResponseData<>(ResponseCode.C205.getCode(), "Môn học tồn tại trong tổ hợp môn học đang hoạt động và không thể xóa !", activeSubjectGroups);
            }
            subject.setStatus(SubjectStatus.INACTIVE);
            subjectRepository.save(subject);
            log.info("Subject with ID {} has been deleted", id);

            return new ResponseData<>(ResponseCode.C200.getCode(), "Môn học đã được xóa thành công !");
        } catch (Exception ex) {
            log.error("Failed to delete subject: {}", ex.getMessage());
            return new ResponseData<>(ResponseCode.C201.getCode(), "Đã xảy ra lỗi trong quá trình xóa môn học !");
        }
    }
    @Override
    public ResponseData<?> activateSubject(Integer id) {
        try {
            Subject subject = subjectRepository.findById(id).orElse(null);
            if (subject == null) {
                return new ResponseData<>(ResponseCode.C203.getCode(), "Môn học không được tìm thấy !");
            }
            if (subject.getStatus().equals(SubjectStatus.ACTIVE)){
                return new ResponseData<>(ResponseCode.C205.getCode(), "Môn học đang hoạt động !");
            }
            subject.setStatus(SubjectStatus.ACTIVE);
            subjectRepository.save(subject);
            log.info("Subject with ID {} has been activated", id);
            return new ResponseData<>(ResponseCode.C200.getCode(), "Môn học đã được kích hoạt thành công !");
        } catch (Exception ex) {
            log.error("Error occurred while activating subject: {}", ex.getMessage());
            return new ResponseData<>(ResponseCode.C201.getCode(), "Đã xảy ra lỗi trong quá trình kích hoạt môn học !");
        }
    }

    public Subject findById(Integer id) throws ResourceNotFoundException{
        log.info("Find subject by ID {}", id);
        return subjectRepository.findById(id).orElseThrow(() ->{
            log.error("Subject with ID {} not found", id);
            return new ResourceNotFoundException("Môn học với Id: " + id + " không tìm thấy");
        });
    }
}