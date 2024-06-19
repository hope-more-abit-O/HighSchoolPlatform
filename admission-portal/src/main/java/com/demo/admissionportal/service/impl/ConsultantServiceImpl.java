package com.demo.admissionportal.service.impl;

import com.demo.admissionportal.constants.ResponseCode;
import com.demo.admissionportal.dto.request.consultant.UniversityRegisterConsultantRequestDTO;
import com.demo.admissionportal.dto.response.CreateConsultantResponse;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.dto.response.entity.ConsultantResponseDTO;
import com.demo.admissionportal.dto.response.entity.sub_entity.UniversityConsultantResponseDTO;
import com.demo.admissionportal.entity.Consultant;
import com.demo.admissionportal.entity.sub_entity.UniversityConsultant;
import com.demo.admissionportal.repository.ConsultantRepository;
import com.demo.admissionportal.repository.sub_repository.UniversityConsultantRepository;
import com.demo.admissionportal.service.ConsultantService;
import com.demo.admissionportal.service.UniversityService;
import com.demo.admissionportal.service.ValidationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Implement of Consultant Service.
 *
 * @author hopeless
 * @version 1.0
 * @since 16/06/2024
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ConsultantServiceImpl implements ConsultantService {
    private final ConsultantRepository consultantRepository;
    private final UniversityService universityService;
    private final UniversityConsultantRepository universityConsultantRepository;
    private final ValidationService validationService;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    /**
     * Registers a new consultant for a university.
     *
     * @param request The request containing consultant information and university ID.
     * @return A response containing the registration result and consultant data.
     */
    public ResponseData<?> registerConsultant(UniversityRegisterConsultantRequestDTO request) {
        try {
            //TODO check university id
            log.info("Checking university's existent: id - {}", request.getEmail());
            if (universityService.getUniversityById(request.getUniversityId()) == null) {
                log.info("University with id:{} does not exist", request.getUniversityId());
                return ResponseData.error("Tạo nhân viên thất bại, vui lòng kiểm tra lại !", "Trường đại học với id: " + request.getUniversityId() + " không tìm thấy!");
            }

            request.trim();
            Consultant consultantRegisted = new Consultant(request.getUsername(), request.getName(), request.getEmail(), request.getPassword(), request.getPhone());
            consultantRepository.save(consultantRegisted);
            log.info("Consultant registered successfully with email: {}", request.getEmail());

            return new ResponseData<>(ResponseCode.C200.getCode(), "Nhân viên được tạo thành công !", consultantRegisted);
        } catch (Exception e) {
            log.error("Registering Consultant with email failed: {}", e.getMessage());
            return new ResponseData<>(ResponseCode.C201.getCode(), "Tạo nhân viên thất bại, vui lòng kiểm tra lại !");
        }
    }

    /**
     * Retrieves information for a consultant by their ID.
     *
     * @param id The unique identifier of the consultant to retrieve.
     * @return A response containing the consultant information or an error message if the consultant is not found.
     */
    public ResponseData<ConsultantResponseDTO> getConsultantById(Integer id){
        log.info("Getting consultant info with id: {}", id);
        Optional<Consultant> consultant = consultantRepository.findById(id);
        if (consultant.isPresent()){
            log.info("Consultant found with id: {}", id);
            return ResponseData.ok("Tìm thấy thông tin của tư vấn viên.", modelMapper.map(consultant.get(), ConsultantResponseDTO.class));
        }

        log.warn("Consultant not found with id: {}", id);
        return ResponseData.error("Tư vấn viên với id: " + id + " không tồn tại");
    }



    /**
     * Creates a new consultant and associates them with a university.
     *
     * @param request The request containing consultant information and university ID.
     * @return A response containing the creation result and consultant data.
     */
    public ResponseData<?> universityCreateConsultant(UniversityRegisterConsultantRequestDTO request) {
        try {
            request.trim();

            validateCreateConsultantRequest(request);

            Consultant consultant = createConsultant(request);

            UniversityConsultant universityConsultant = createUniversityConsultant(request.getUniversityId(), consultant.getId());

            return ResponseData.ok("Tạo tư vấn viên thành công!", new CreateConsultantResponse(modelMapper.map(consultant, ConsultantResponseDTO.class), modelMapper.map(universityConsultant, UniversityConsultantResponseDTO.class)));
        } catch (Exception e) {
            return ResponseData.error("Tạo tư vấn viên thất bại", e.getMessage());
        }
    }

    /**
     * Creates a new university-consultant association.
     *
     * @param universityId The ID of the university.
     * @param consultantId The ID of the consultant.
     * @return The newly created university-consultant association.
     * @throws Exception If the creation fails.
     */
    private UniversityConsultant createUniversityConsultant(Integer universityId, Integer consultantId) throws Exception {
        log.info("Create university-consultant");
        UniversityConsultant universityConsultant = universityConsultantRepository.save(new UniversityConsultant(universityId, consultantId));
        if (universityConsultant == null){
            log.info("Create consultant failed - {}", new UniversityConsultant(universityId, consultantId));
            throw new Exception("Tạo trường đại học - tư vấn viên thất bại");
        }
        log.info("Create university-consultant success - {}", universityConsultant);
        return universityConsultant;
    }

    /**
     * Validates the request for creating a new consultant.
     *
     * @param request The request containing consultant information.
     * @throws Exception If the validation fails.
     */
    private void validateCreateConsultantRequest(UniversityRegisterConsultantRequestDTO request) throws Exception {
        log.info("Starting checking university with id: {}", request.getUniversityId());
        if (universityService.getUniversityById(request.getUniversityId()) == null) {
            log.info("University id: {} not found!", request.getUniversityId());
            throw new Exception("Không tìm thấy trường đại học với id: " + request.getUniversityId());
        }
        log.info("University id: {} founded!", request.getUniversityId());

        validationService.validateUsernameAndEmailAvailable(request.getUsername(), request.getEmail());

        log.info("Checking existing username availability: {}", request.getUsername());
        if (consultantRepository.findByUsername(request.getUsername()).isPresent()) {
            log.warn("Username: {} not available", request.getUsername());
            throw new Exception("Tên tài khoản đã được sử dụng!");
        }
        log.info("Username: {} is available.", request.getUsername());

        log.info("Checking existing email availability: {}", request.getEmail());
        if (consultantRepository.findByPhone(request.getPhone()).isPresent()) {
            log.warn("Consultant with phone: {} already registered", request.getPhone());
            throw new Exception("Số điện thoại đã được sử dụng!");
        }
        log.info("Phone number: {} is available", request.getPhone());
        log.info("Validation passed. Ready to create consultant!");
    }

    /**
     * Creates a new consultant based on the provided request.
     *
     * @param request The request containing consultant information.
     * @return The newly created consultant.
     * @throws Exception If the creation fails.
     */
    private Consultant createConsultant(UniversityRegisterConsultantRequestDTO request) throws Exception {
        log.info("Start creating consultant.");
        Consultant consultant = consultantRepository.save(new Consultant(request.getUsername(), request.getName(), request.getEmail(), passwordEncoder.encode(request.getPassword()), request.getPhone()));
        if (consultant == null) {
            log.info("Create consultant failed - {}", new Consultant(request.getUsername(), request.getName(), request.getEmail(), passwordEncoder.encode(request.getPassword()), request.getPhone()));
            throw new Exception("Tạo tư vấn viên thất bại!");
        }
        log.info("Create consultant success - {}", consultant.toString());
        return consultant;
    }

}