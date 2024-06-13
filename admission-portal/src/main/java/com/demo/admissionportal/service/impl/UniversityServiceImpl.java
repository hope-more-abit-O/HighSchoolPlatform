package com.demo.admissionportal.service.impl;

import com.demo.admissionportal.constants.ResponseCode;
import com.demo.admissionportal.constants.UniversityType;
import com.demo.admissionportal.dto.request.university.StaffRegisterUniversityRequestDTO;
import com.demo.admissionportal.dto.response.CreateUniversityResponse;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.dto.response.entity.UniversityResponseDTO;
import com.demo.admissionportal.dto.response.entity.sub_entity.StaffUniversityResponseDTO;
import com.demo.admissionportal.entity.University;
import com.demo.admissionportal.entity.sub_entity.StaffUniversity;
import com.demo.admissionportal.repository.UniversityRepository;
import com.demo.admissionportal.repository.sub_repository.StaffUniversityRepository;
import com.demo.admissionportal.service.StaffService;
import com.demo.admissionportal.service.UniversityService;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implement of University Service.
 *
 * @author hopeless
 * @version 1.0
 * @since 13/06/2024
 */
@Service
@Slf4j
public class UniversityServiceImpl implements UniversityService {
    private final UniversityRepository universityRepository;
    private final StaffUniversityRepository staffUniversityRepository;
    private final StaffService staffService;
    private final ModelMapper modelMapper;

    /**
     * Constructs a new instance of {@code UniversityServiceImpl}.
     *
     * @param universityRepository The repository for managing universities.
     * @param staffUniversityRepository The repository for managing staff-university associations.
     * @param staffService The service for managing staff information.
     * @param modelMapper The model mapper for object mapping.
     */
    @Autowired
    public UniversityServiceImpl(UniversityRepository universityRepository, StaffUniversityRepository staffUniversityRepository, StaffService staffService, ModelMapper modelMapper) {
        this.universityRepository = universityRepository;
        this.staffUniversityRepository = staffUniversityRepository;
        this.staffService = staffService;
        this.modelMapper = modelMapper;
    }

    /**
     * Creates a new university and associates it with a staff member.
     *
     * @param request The request containing university information and staff id.
     * @return A response containing the created university and staff-university association.
     */
    public ResponseData<?> staffCreateUniversity(StaffRegisterUniversityRequestDTO request){
        if (staffService.getStaffById(request.getStaffId()) == null) {
            log.error("No staff was found with id: {}", request.getStaffId());
            return ResponseData.error("Tạo trường đại học thất bại, vui lòng kiểm tra lại!", "Không tìm thấy nhân viên với id: {}" + request.getStaffId());
        }

        // CREATE UNIVERSITY and STAFF-UNIVERSITY
        try{
            University university = createUniversity(request);
            StaffUniversity staffUniversity = staffUniversityRepository.save(new StaffUniversity(request.getStaffId(), university.getId()));
            return ResponseData.ok("Tạo trường đại học thành công!", new CreateUniversityResponse(modelMapper.map(university, UniversityResponseDTO.class), modelMapper.map(staffUniversity, StaffUniversityResponseDTO.class)));
        } catch (Exception e){
            return ResponseData.error("Tạo trường đại học thất bại, vui lòng kiểm tra lại!", e.getMessage());
        }
    }

    /**
     * Creates a new university based on the provided request.
     *
     * @param request The request containing university information, of type {@link StaffRegisterUniversityRequestDTO}.
     * @return The newly created university ({@link University})
     * @throws Exception If the university registration fails due to validation or other issues.
     */
    public University createUniversity(StaffRegisterUniversityRequestDTO request) throws Exception {
        request.trim();
        log.info("Starting registration process for email: {}", request.getEmail());
        List<University> oUniversity = universityRepository.findByUsernameOrEmailOrCode(request.getUsername(), request.getEmail(), request.getCode());
        if (!oUniversity.isEmpty()) {
            log.warn("Consultant with email: {} or username: {} or code {} already exists", request.getEmail(), request.getUsername(), request.getCode());
            throw new BadRequestException("Tên tài khoản, email hoặc mã của trường đại học đã tồn tại!");
        }

        University university = universityRepository.save(new University(request.getCode(), request.getName(), request.getUsername(), request.getEmail(), request.getDescription(), request.getPassword(), UniversityType.valueOf(request.getType())));
        log.info("University registered successfully with email: {}", request.getEmail());
        return university;
    }

    /**
     * Creates a new university with a predefined "fail" status.
     *
     * @param request The request containing university information.
     * @return A response indicating the result of the creation process.
     */
    public ResponseData<?> createUniversityFail(StaffRegisterUniversityRequestDTO request){
        try {
            request.trim();
            University universityRegister = University.getFailUnversity();
            universityRepository.save(universityRegister);
            log.info("Consultant registered successfully with email: {}", request.getEmail());
            return new ResponseData<>(ResponseCode.C200.getCode(), "Trường đại học được tạo thành công !", universityRegister);
        } catch (Exception e){
            log.error("Registering Consultant with email failed: {}", e.getMessage());
            return ResponseData.error("Tạo trường đại học thất bại, vui lòng kiểm tra lại!", e.getMessage());
        }
    }
}
