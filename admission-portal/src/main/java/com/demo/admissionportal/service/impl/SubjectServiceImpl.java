package com.demo.admissionportal.service.impl;

import com.demo.admissionportal.dto.request.RequestSubjectDTO;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.entity.Subject;
import com.demo.admissionportal.repository.SubjectRepository;
import com.demo.admissionportal.service.SubjectService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * The type Subject service.
 */
@Service
@AllArgsConstructor
@Slf4j
public class SubjectServiceImpl implements SubjectService {
    private final SubjectRepository subjectRepository;
    private final ModelMapper modelMapper;

    @Override
    public ResponseData<RequestSubjectDTO> createSubject(RequestSubjectDTO requestSubjectDTO) {
        try {
            if (!Objects.isNull(requestSubjectDTO)) {
                Subject checkExisted = subjectRepository.findSubjectByName(requestSubjectDTO.getName().trim());
                if (checkExisted != null) {
                    log.error("Subject {} is already existed", requestSubjectDTO.getName());
                    return new ResponseData<>(HttpStatus.CONFLICT.value(), "Môn học " + requestSubjectDTO.getName() + " đã tồn tại", null);
                }
                Subject isSuccess = subjectRepository.save(modelMapper.map(requestSubjectDTO, Subject.class));
                RequestSubjectDTO createdSubject = modelMapper.map(isSuccess, RequestSubjectDTO.class);
                log.info("Subject {} is successfully added", requestSubjectDTO.getName());
                return new ResponseData<>(HttpStatus.CREATED.value(), "Tạo môn học thành công", createdSubject);
            }
        } catch (Exception ex) {
            log.error("Error occurred while creating subject: {}", ex.getMessage());
        }
        return new ResponseData<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Xuất hiện lỗi khi tạo môn học", null);
    }
}
