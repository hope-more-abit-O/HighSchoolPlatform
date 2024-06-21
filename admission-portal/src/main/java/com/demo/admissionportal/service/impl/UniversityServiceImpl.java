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
import com.demo.admissionportal.entity.address.Address;
import com.demo.admissionportal.entity.sub_entity.StaffUniversity;
import com.demo.admissionportal.exception.DataExistedException;
import com.demo.admissionportal.exception.UnSupportedException;
import com.demo.admissionportal.repository.*;
import com.demo.admissionportal.repository.sub_repository.StaffUniversityRepository;
import com.demo.admissionportal.service.*;
import com.demo.admissionportal.util.EmailUtil;
import com.demo.admissionportal.util.OTPUtil;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class UniversityServiceImpl implements UniversityService {
    private final UniversityRepository universityRepository;
    private final StaffUniversityRepository staffUniversityRepository;
    private final StaffService staffService;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final OTPService otpService;
    private final OTPUtil otpUtil;
    private final EmailUtil emailUtil;
    private final ValidationService validationService;
    private final AddressService addressService;

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
    /**
     * Updates an existing university's information.
     *
     * This method handles the update process for a university, including validation
     * of unique fields (email, username, phone), sending an OTP for verification,
     * and saving updates to both the database and cache.
     *
     * @param updateUniversityRequestDTO An object containing the updated information for the university.
     * @return A {@link ResponseData} object indicating the result of the operation: <br>
     *         - {@link ResponseCode#C206} (): If the OTP is successfully sent to the university's email. <br>
     *         - {@link ResponseCode#C203} (Not Found): If the university with the given ID is not found. <br>
     *         - {@link ResponseCode#C208} (Unsupported): If an unsupported operation is attempted. <br>
     *         - {@link ResponseCode#C204} (Conflict): If a data conflict occurs, such as duplicate email, username, or phone. <br>
     *         - {@link ResponseCode#C203} (Internal Server Error): If any other error occurs during the update process.
     */
    public ResponseData<University> updateUniversity(UpdateUniversityRequestDTO updateUniversityRequestDTO) {
        try {
            boolean resetEmail = false;
            log.info("Start to find university with id: {} ", updateUniversityRequestDTO.getId());
            Optional<University> optionalUniversity = universityRepository.findById(updateUniversityRequestDTO.getId());
            if (optionalUniversity.isEmpty()) {
                return new ResponseData<>(ResponseCode.C203.getCode(), "Không tìm thấy trường đại học");
            }
            University university = optionalUniversity.get();
            String newEmail = updateUniversityRequestDTO.getEmail().trim();
            String newUsername = updateUniversityRequestDTO.getUsername().trim();
            String newPhone = updateUniversityRequestDTO.getPhone().trim();

            // Case 1 : Existed By Email
            if (university.getEmail() == null || !university.getEmail().equals(newEmail)){
                validationService.validateEmail(newEmail, university);
                resetEmail = true;
            }

            // Case 2: Existed By UserName
            if (university.getUsername() == null || !university.getUsername().equals(newUsername))
                validationService.validateUsername(newUsername, university);

            // Case 3: Existed By Phone
            if (university.getPhone() == null || !university.getPhone().equals(newPhone))
                validationService.validatePhoneNumber(university.getPhone(), university);

            Address address = addressService.createAddressReturnAddress(updateUniversityRequestDTO.getSpecificAddress(),
                    updateUniversityRequestDTO.getProvinceId(),
                    updateUniversityRequestDTO.getDistrictId(),
                    updateUniversityRequestDTO.getWardId());

            university.setCode(updateUniversityRequestDTO.getCode());
            university.setName(updateUniversityRequestDTO.getName());
            university.setUsername(newUsername);
            university.setEmail(newEmail);
            university.setDescription(updateUniversityRequestDTO.getDescription());
            university.setAvatar(updateUniversityRequestDTO.getAvatar());
            university.setPhone(newPhone);
            university.setType(university.getType());
            university.setAddressId(address.getId());


            if (!resetEmail) {
                universityRepository.save(university);
                return new ResponseData<>(ResponseCode.C200.getCode(), "Đã cập nhập trường đại học.");
            }
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
            log.error(ex.getMessage());
            if (ex.getClass().equals(UnSupportedException.class))
                return new ResponseData<>(ResponseCode.C208.getCode(), "Không hỗ trợ phương thức.");
            if (ex.getClass().equals(DataExistedException.class))
                return new ResponseData<>(ResponseCode.C204.getCode(), ex.getMessage());
            return new ResponseData<>(ResponseCode.C203.getCode(), "Cập nhật trường đại học thất bại");
        }
    }

    /**
     * Verifies a university account update using a provided OTP (One-Time Password).
     *
     * This method checks the validity of the provided OTP against a stored OTP associated
     * with the university's email address. If the OTP is valid and within the time limit,
     * the university's information is updated in the database with the data
     * previously stored in the cache during the update process.
     *
     * @param verifyUpdateUniversityRequestDTO An object containing the university ID and the OTP provided by the user.
     * @return A {@link ResponseData} object indicating the result of the verification: <br>
     *         - {@link ResponseCode#C200} (SUCCESSFULLY): If the OTP is valid, not expired, and the university update is successful. <br>
     *         - {@link ResponseCode#C201} (FAILED): If the OTP is invalid, expired, or there are issues retrieving it. <br>
     *         - {@link ResponseCode#C203} (NOT FOUND): If the university with the provided ID is not found in the database. <br>
     */
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
            Optional<University> university = universityRepository.findById(verifyUpdateUniversityRequestDTO.getUniversityId());
            if (university.isEmpty())
                return new ResponseData<>(ResponseCode.C203.getCode(), "Không tìm thấy trường đại học");

            university.get().setCode(universityFromRedis.getCode());
            university.get().setName(universityFromRedis.getName());
            university.get().setUsername(universityFromRedis.getUsername());
            university.get().setEmail(universityFromRedis.getEmail());
            university.get().setDescription(universityFromRedis.getDescription());
            university.get().setAvatar(universityFromRedis.getAvatar());
            university.get().setPassword(universityFromRedis.getPassword());
            university.get().setPhone(universityFromRedis.getPhone());
            university.get().setStatus(universityFromRedis.getStatus());

            // Update data in DB
            var createStudent = universityRepository.save(university.get());
            log.info("University has been updated: {}", createStudent);
            return new ResponseData<>(ResponseCode.C200.getCode(), "Tài khoản đã được cập nhật");
        } else {
            return new ResponseData<>(ResponseCode.C201.getCode(), "OTP đã hết hạn");
        }
    }

}
