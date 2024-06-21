package com.demo.admissionportal.service.impl;

import com.demo.admissionportal.constants.ResponseCode;
import com.demo.admissionportal.constants.UniversityType;
import com.demo.admissionportal.dto.request.ConfirmResetPasswordRequest;
import com.demo.admissionportal.dto.request.ResetPasswordRequest;
import com.demo.admissionportal.dto.request.VerifyUpdateUniversityRequestDTO;
import com.demo.admissionportal.dto.request.redis.ResetPasswordAccountRedisCacheDTO;
import com.demo.admissionportal.dto.request.university.StaffRegisterUniversityRequestDTO;
import com.demo.admissionportal.dto.request.university.UpdateUniversityRequestDTO;
import com.demo.admissionportal.dto.response.CreateUniversityResponse;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.dto.response.entity.UniversityResponseDTO;
import com.demo.admissionportal.dto.response.entity.sub_entity.StaffUniversityResponseDTO;
import com.demo.admissionportal.entity.*;
import com.demo.admissionportal.entity.sub_entity.StaffUniversity;
import com.demo.admissionportal.repository.*;
import com.demo.admissionportal.repository.sub_repository.StaffUniversityRepository;
import com.demo.admissionportal.service.OTPService;
import com.demo.admissionportal.service.StaffService;
import com.demo.admissionportal.service.UniversityService;
import com.demo.admissionportal.util.EmailUtil;
import com.demo.admissionportal.util.OTPUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
    private final PasswordEncoder passwordEncoder;
    private final OTPService otpService;
    private final OTPUtil otpUtil;
    private final EmailUtil emailUtil;
    private final StaffRepository staffRepository;
    private final ConsultantRepository consultantRepository;
    private final StudentRepository studentRepository;
    private final AdminRepository adminRepository;

    /**
     * Constructs a new instance of {@code UniversityServiceImpl}.
     *
     * @param universityRepository      The repository for managing universities.
     * @param staffUniversityRepository The repository for managing staff-university associations.
     * @param staffService              The service for managing staff information.
     * @param modelMapper               The model mapper for object mapping.
     */
    @Autowired
    public UniversityServiceImpl(UniversityRepository universityRepository, StaffUniversityRepository staffUniversityRepository, StaffService staffService, ModelMapper modelMapper, PasswordEncoder passwordEncoder, OTPService otpService, OTPUtil otpUtil, EmailUtil emailUtil, StaffRepository staffRepository, ConsultantRepository consultantRepository, StudentRepository studentRepository, AdminRepository adminRepository) {
        this.universityRepository = universityRepository;
        this.staffUniversityRepository = staffUniversityRepository;
        this.staffService = staffService;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
        this.otpService = otpService;
        this.otpUtil = otpUtil;
        this.emailUtil = emailUtil;
        this.staffRepository = staffRepository;
        this.consultantRepository = consultantRepository;
        this.studentRepository = studentRepository;
        this.adminRepository = adminRepository;
    }

    /**
     * Creates a new university and associates it with a staff member.
     *
     * @param request The request containing university information and staff id.
     * @return A response containing the created university and staff-university association.
     */
    public ResponseData<?> staffCreateUniversity(StaffRegisterUniversityRequestDTO request) {
        if (staffService.getStaffById(request.getStaffId()) == null) {
            log.error("No staff was found with id: {}", request.getStaffId());
            return ResponseData.error("Tạo trường đại học thất bại, vui lòng kiểm tra lại!", "Không tìm thấy nhân viên với id: {}" + request.getStaffId());
        }

        // CREATE UNIVERSITY and STAFF-UNIVERSITY
        try {
            University university = createUniversity(request);
            StaffUniversity staffUniversity = staffUniversityRepository.save(new StaffUniversity(request.getStaffId(), university.getId()));
            return ResponseData.ok("Tạo trường đại học thành công!", new CreateUniversityResponse(modelMapper.map(university, UniversityResponseDTO.class), modelMapper.map(staffUniversity, StaffUniversityResponseDTO.class)));
        } catch (Exception e) {
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

        University university = universityRepository.save(new University(request.getCode(), request.getName(), request.getUsername(), request.getEmail(), request.getDescription(), passwordEncoder.encode(request.getPassword()), UniversityType.valueOf(request.getType())));
        log.info("University registered successfully with email: {}", request.getEmail());
        return university;
    }

    /**
     * Creates a new university with a predefined "fail" status.
     *
     * @param request The request containing university information.
     * @return A response indicating the result of the creation process.
     */
    public ResponseData<?> createUniversityFail(StaffRegisterUniversityRequestDTO request) {
        try {
            request.trim();
            University universityRegister = University.getFailUniversity();
            universityRepository.save(universityRegister);
            log.info("Consultant registered successfully with email: {}", request.getEmail());
            return new ResponseData<>(ResponseCode.C200.getCode(), "Trường đại học được tạo thành công !", universityRegister);
        } catch (Exception e) {
            log.error("Registering Consultant with email failed: {}", e.getMessage());
            return ResponseData.error("Tạo trường đại học thất bại, vui lòng kiểm tra lại!", e.getMessage());
        }
    }

    /**
     * Retrieves a university by its ID.
     *
     * @param id The ID of the university to retrieve.
     * @return The university with the specified ID, or null if no such university exists.
     */
    public University getUniversityById(Integer id) {
        return universityRepository.findById(id).orElse(null);
    }

    public ResponseData<University> updateUniversity(UpdateUniversityRequestDTO updateUniversityRequestDTO) {
        try {
            log.info("Start to find university with id: {} ", updateUniversityRequestDTO.getId());
            University university = universityRepository.findUniversitiesById(updateUniversityRequestDTO.getId());
            if (university == null) {
                return new ResponseData<>(ResponseCode.C203.getCode(), "Không tìm thấy trường đại học");
            }
            String newEmail = updateUniversityRequestDTO.getEmail().trim();
            String newUsername = updateUniversityRequestDTO.getUsername().trim();
            String newPhone = updateUniversityRequestDTO.getPhone().trim();

            // Case 1 : Existed By Email
            if (university.getEmail() == null) {
                if (isEmailExisted(newEmail)) {
                    log.error("Email {} is already existed", newEmail);
                    return new ResponseData<>(ResponseCode.C204.getCode(), "Email đã được tài khoản khác sử dụng");
                }
            } else {
                if (!university.getEmail().equals(newEmail)) {
                    if (isEmailExisted(newEmail)) {
                        log.error("Email {} is already existed", newEmail);
                        return new ResponseData<>(ResponseCode.C204.getCode(), "Email đã được tài khoản khác sử dụng");
                    }
                }
            }

            // Case 2: Existed By UserName
            if (university.getUsername() == null) {
                if (isUsernameExisted(newUsername)) {
                    log.error("Username {} is already existed", newUsername);
                    return new ResponseData<>(ResponseCode.C204.getCode(), "Username đã được tài khoản khác sử dụng");
                }
            } else {
                if (!university.getUsername().equals(newUsername)) {
                    if (isUsernameExisted(newUsername)) {
                        log.error("Username {} is already existed", newUsername);
                        return new ResponseData<>(ResponseCode.C204.getCode(), "Username đã được tài khoản khác sử dụng");
                    }
                }
            }

            // Case 3: Existed By Phone
            if (university.getPhone() == null) {
                if (isPhoneExisted(newPhone)) {
                    log.error("Phone {} is already existed", newPhone);
                    return new ResponseData<>(ResponseCode.C204.getCode(), "Số điện thoại đã được tài khoản khác sử dụng");
                }
            } else {
                if (!university.getPhone().equals(newPhone)) {
                    if (isPhoneExisted(newPhone)) {
                        log.error("Phone {} is already existed", newPhone);
                        return new ResponseData<>(ResponseCode.C204.getCode(), "Số điện thoại đã được tài khoản khác sử dụng");
                    }
                }
            }
            university.setCode(updateUniversityRequestDTO.getCode());
            university.setName(updateUniversityRequestDTO.getName());
            university.setUsername(newUsername);
            university.setEmail(newEmail);
            university.setDescription(updateUniversityRequestDTO.getDescription());
            university.setAvatar(updateUniversityRequestDTO.getAvatar());
            university.setPassword(passwordEncoder.encode(updateUniversityRequestDTO.getPassword()));
            university.setPhone(newPhone);
            university.setStatus(updateUniversityRequestDTO.getStatus());

            // Sending OTP to Email
            String otp = otpUtil.generateOTP();
            if (!emailUtil.sendOtpEmailForUpdate(updateUniversityRequestDTO.getEmail(), otp)) {
                throw new RuntimeException("Không thể gửi OTP xin vui lòng thử lại");
            }

            //Save OTP in Redis Cache
            otpService.saveOTP(updateUniversityRequestDTO.getEmail(), otp, LocalDateTime.now());

            // Save University in Redis Cache
            otpService.saveUniversity(university.getEmail(), university);
            return new ResponseData<>(ResponseCode.C206.getCode(), "Đã gửi OTP vào Email. Xin vui lòng kiểm tra");

        } catch (Exception ex) {
            log.error("Update university with email: {}", ex.getMessage());
            return new ResponseData<>(ResponseCode.C203.getCode(), "Cập nhật trường đại học thất bại");
        }
    }

    @Override
    public ResponseData<?> verifyAccount(VerifyUpdateUniversityRequestDTO verifyUpdateUniversityRequestDTO) {
        String otp = otpService.getOTP(verifyUpdateUniversityRequestDTO.getEmail());
        LocalDateTime storeLocalDateTime = otpService.getOTPDateTime(verifyUpdateUniversityRequestDTO.getEmail());
        if (otp == null) {
            return new ResponseData<>(ResponseCode.C201.getCode(), "OTP đã hết hạn không tồn tại");
        }
        if (!otp.equals(verifyUpdateUniversityRequestDTO.getOtpFromEmail())) {
            return new ResponseData<>(ResponseCode.C201.getCode(), "OTP không hợp lệ");
        }
        // Setting 10 minutes for OTP
        if (Duration.between(storeLocalDateTime, LocalDateTime.now()).getSeconds() < 10 * 60) {
            // Get data from Redis Cache
            University universityFromRedis = otpService.getUniversity(verifyUpdateUniversityRequestDTO.getEmail());

            // Get data from DB
            University university = universityRepository.findUniversitiesById(verifyUpdateUniversityRequestDTO.getUniversityId());
            university.setCode(universityFromRedis.getCode());
            university.setName(universityFromRedis.getName());
            university.setUsername(universityFromRedis.getUsername());
            university.setEmail(universityFromRedis.getEmail());
            university.setDescription(universityFromRedis.getDescription());
            university.setAvatar(universityFromRedis.getAvatar());
            university.setPassword(universityFromRedis.getPassword());
            university.setPhone(universityFromRedis.getPhone());
            university.setStatus(universityFromRedis.getStatus());

            // Update data in DB
            var createStudent = universityRepository.save(university);
            log.info("University has been updated: {}", createStudent);
            return new ResponseData<>(ResponseCode.C200.getCode(), "Tài khoản đã được cập nhật");
        } else {
            return new ResponseData<>(ResponseCode.C201.getCode(), "OTP đã hết hạn");
        }
    }
    private boolean isEmailExisted(String email) {
        return studentRepository.findByEmail(email) != null ||
                staffRepository.findByEmail(email) != null ||
                consultantRepository.findByEmail(email).isPresent() ||
                universityRepository.findByEmail(email) != null ||
                adminRepository.findByEmail(email) != null;
    }

    private boolean isUsernameExisted(String username) {
        return studentRepository.findByUsername(username).isPresent() ||
                staffRepository.findByUsername(username).isPresent() ||
                consultantRepository.findByUsername(username).isPresent() ||
                universityRepository.findByUsername(username).isPresent() ||
                adminRepository.findByUsername(username).isPresent();
    }

    private boolean isPhoneExisted(String phone) {
        return studentRepository.findByPhone(phone) != null ||
                staffRepository.findByPhone(phone) != null ||
                consultantRepository.findByPhone(phone).isPresent() ||
                universityRepository.findByPhone(phone).isPresent() ||
                adminRepository.findAdminByPhone(phone) != null;
    }
}
