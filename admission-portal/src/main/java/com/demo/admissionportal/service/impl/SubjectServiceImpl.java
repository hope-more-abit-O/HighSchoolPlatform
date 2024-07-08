package com.demo.admissionportal.service.impl;

import com.demo.admissionportal.constants.ResponseCode;
import com.demo.admissionportal.constants.SubjectStatus;
import com.demo.admissionportal.dto.request.RequestSubjectDTO;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.entity.Subject;
import com.demo.admissionportal.entity.User;
import com.demo.admissionportal.repository.SubjectGroupRepository;
import com.demo.admissionportal.repository.SubjectRepository;
import com.demo.admissionportal.service.SubjectService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;

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
}