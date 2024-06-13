package com.demo.admissionportal.service.impl;

import com.demo.admissionportal.constants.ResponseCode;
import com.demo.admissionportal.constants.UniversityType;
import com.demo.admissionportal.dto.request.university.RegisterUniversityRequestDTO;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.entity.Consultant;
import com.demo.admissionportal.entity.University;
import com.demo.admissionportal.repository.UniversityRepository;
import com.demo.admissionportal.service.UniversityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UniversityServiceImpl implements UniversityService {
    private final UniversityRepository universityRepository;

    public ResponseData<?> registerUniversity(RegisterUniversityRequestDTO request){
        try {
            request.trim();

            log.info("Starting registration process for email: {}", request.getEmail());
            List<University> oUniversity = universityRepository.findByUsernameOrEmailOrCode(request.getUsername(), request.getEmail(), request.getCode());
            if (!oUniversity.isEmpty()) {
                log.warn("Consultant with email: {} or username: {} or code {} already exists", request.getEmail(), request.getUsername(), request.getCode());
                return new ResponseData<>(ResponseCode.C204.getCode(), "Tên tài khoản, email hoặc mã của trường đại học đã tồn tại!");
            }

            University universityRegisted = new University(request.getCode(), request.getName(), request.getUsername(), request.getEmail(), request.getDescription(), request.getPassword(), UniversityType.valueOf(request.getType()));
            universityRepository.save(universityRegisted);
            log.info("Consultant registered successfully with email: {}", request.getEmail());
            return new ResponseData<>(ResponseCode.C200.getCode(), "Trường đại học được tạo thành công !", universityRegisted);
        } catch (Exception e){
            log.error("Registering Consultant with email failed: {}", e.getMessage());
            return new ResponseData<>(ResponseCode.C201.getCode(), "Tạo trường đại học thất bại, vui lòng kiểm tra lại!", new HashMap<String, String>().put("Error detail", e.getMessage()));
        }
    }

    public ResponseData<?> registerUniversityFail(RegisterUniversityRequestDTO request){
        try {
            request.trim();
            University universityRegisted = University.getFailUnversity();
            universityRepository.save(universityRegisted);
            log.info("Consultant registered successfully with email: {}", request.getEmail());
            return new ResponseData<>(ResponseCode.C200.getCode(), "Trường đại học được tạo thành công !", universityRegisted);
        } catch (Exception e){
            log.error("Registering Consultant with email failed: {}", e.getMessage());
            Map<String, String> errors = new HashMap<>();
            errors.put("Error detail", e.getMessage());
            return ResponseData.error("Tạo trường đại học thất bại, vui lòng kiểm tra lại!", errors);
        }
    }
}
