package com.demo.admissionportal.service.impl;

import com.demo.admissionportal.config.ModelMapperConfig;
import com.demo.admissionportal.constants.ResponseCode;
import com.demo.admissionportal.constants.SubjectStatus;
import com.demo.admissionportal.dto.entity.ActionerDTO;
import com.demo.admissionportal.dto.request.RequestSubjectDTO;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.dto.response.sub_entity.SubjectResponseDTO;
import com.demo.admissionportal.entity.StaffInfo;
import com.demo.admissionportal.entity.Subject;
import com.demo.admissionportal.entity.SubjectGroup;
import com.demo.admissionportal.entity.User;
import com.demo.admissionportal.entity.sub_entity.SubjectGroupSubject;
import com.demo.admissionportal.exception.ResourceNotFoundException;
import com.demo.admissionportal.repository.StaffInfoRepository;
import com.demo.admissionportal.repository.SubjectRepository;
import com.demo.admissionportal.repository.UserRepository;
import com.demo.admissionportal.repository.sub_repository.SubjectGroupSubjectRepository;
import com.demo.admissionportal.service.SubjectService;
import com.demo.admissionportal.util.impl.NameUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class SubjectServiceImpl implements SubjectService {
    private final SubjectRepository subjectRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final SubjectGroupSubjectRepository subjectGroupSubjectRepository;
    private final StaffInfoRepository staffInfoRepository;

    @Override
    public ResponseData<Page<SubjectResponseDTO>> findAll(String name, SubjectStatus status, Pageable pageable) {
        try {
            String statusString = status != null ? status.name() : null;
            Page<Subject> subjects = subjectRepository.findAll(name, statusString, pageable);
            Page<SubjectResponseDTO> subjectResponseDTOs = subjects.map(subject -> {
                SubjectResponseDTO subjectResponseDTO = modelMapper.map(subject, SubjectResponseDTO.class);
                subjectResponseDTO.setCreateBy(getUserDetails(subject.getCreateBy()));
                return subjectResponseDTO;
            });
            return new ResponseData<>(ResponseCode.C200.getCode(), "Danh sách môn học đã được hiển thị !", subjectResponseDTOs);
        } catch (Exception ex) {
            log.error("Error occurred while fetching subjects: {}", ex.getMessage());
            return new ResponseData<>(ResponseCode.C201.getCode(), "Có lỗi trong quá trình lấy ra danh sách môn học !");
        }
    }


    @Override
    public ResponseData<SubjectResponseDTO> getSubjectById(Integer id) {
        try {
            Optional<Subject> subject = subjectRepository.findById(id);
            if (subject.isPresent()) {
                Subject getSubject = subject.get();
                SubjectResponseDTO responseDTO = modelMapper.map(getSubject, SubjectResponseDTO.class);
                responseDTO.setCreateBy(getUserDetails(getSubject.getCreateBy()));
                responseDTO.setCreateTime(getSubject.getCreateTime());
                return new ResponseData<>(ResponseCode.C200.getCode(), "Môn học được tìm thấy", responseDTO);
            } else {
                return new ResponseData<>(ResponseCode.C203.getCode(), "Môn học không được tìm thấy");
            }
        } catch (Exception ex) {
            log.error("Error occurred while fetching subject by ID: {}", ex.getMessage());
            return new ResponseData<>(ResponseCode.C201.getCode(), "Đã xảy ra lỗi trong quá trình lấy môn học");
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
//
//    @Override
//    public ResponseData<?> deleteSubject(Integer id) {
//        try {
//            Subject subject = subjectRepository.findById(id).orElse(null);
//            if (subject == null || subject.getStatus().equals(SubjectStatus.INACTIVE)) {
//                return new ResponseData<>(ResponseCode.C203.getCode(), "Môn học không được tìm thấy !");
//            }
//            List<SubjectGroupSubject> subjectGroupSubjects = subjectGroupSubjectRepository.findBySubjectId(id);
//            List<SubjectGroup> activeSubjectGroups = new ArrayList<>();
//
//            for (SubjectGroupSubject sgs : subjectGroupSubjects) {
//                SubjectGroup subjectGroup = subjectGroupRepository.findById(sgs.getSubjectGroupId()).orElse(null);
//                if (subjectGroup != null && subjectGroup.getStatus().equals(SubjectStatus.ACTIVE.name())) {
//                    activeSubjectGroups.add(subjectGroup);
//                }
//            }
//            if (!activeSubjectGroups.isEmpty()) {
//                return new ResponseData<>(ResponseCode.C205.getCode(), "Môn học tồn tại trong tổ hợp môn học đang hoạt động và không thể xóa !", activeSubjectGroups);
//            }
//            subject.setStatus(SubjectStatus.INACTIVE);
//            subjectRepository.save(subject);
//            log.info("Subject with ID {} has been deleted", id);
//
//            return new ResponseData<>(ResponseCode.C200.getCode(), "Môn học đã được xóa thành công !");
//        } catch (Exception ex) {
//            log.error("Failed to delete subject: {}", ex.getMessage());
//            return new ResponseData<>(ResponseCode.C201.getCode(), "Đã xảy ra lỗi trong quá trình xóa môn học !");
//        }
//    }
//
//    @Override
//    public ResponseData<?> activateSubject(Integer id) {
//        try {
//            Subject subject = subjectRepository.findById(id).orElse(null);
//            if (subject == null) {
//                return new ResponseData<>(ResponseCode.C203.getCode(), "Môn học không được tìm thấy !");
//            }
//            if (subject.getStatus().equals(SubjectStatus.ACTIVE)){
//                return new ResponseData<>(ResponseCode.C205.getCode(), "Môn học đang hoạt động !");
//            }
//            subject.setStatus(SubjectStatus.ACTIVE);
//            subjectRepository.save(subject);
//            log.info("Subject with ID {} has been activated", id);
//            return new ResponseData<>(ResponseCode.C200.getCode(), "Môn học đã được kích hoạt thành công !");
//        } catch (Exception ex) {
//            log.error("Error occurred while activating subject: {}", ex.getMessage());
//            return new ResponseData<>(ResponseCode.C201.getCode(), "Đã xảy ra lỗi trong quá trình kích hoạt môn học !");
//        }
//    }

    public Subject findById(Integer id) throws ResourceNotFoundException{
        log.info("Find subject by ID {}", id);
        return subjectRepository.findById(id).orElseThrow(() ->{
            log.error("Subject with ID {} not found", id);
            return new ResourceNotFoundException("Môn học với Id: " + id + " không tìm thấy");
        });
    }

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
}
