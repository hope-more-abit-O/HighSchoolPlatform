package com.demo.admissionportal.service.impl;

import com.demo.admissionportal.constants.ResponseCode;
import com.demo.admissionportal.constants.Role;
import com.demo.admissionportal.dto.request.ConfirmResetPasswordRequest;
import com.demo.admissionportal.dto.request.ResetPasswordRequest;
import com.demo.admissionportal.dto.request.redis.ResetPasswordAccountRedisCacheDTO;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.entity.Consultant;
import com.demo.admissionportal.entity.Staff;
import com.demo.admissionportal.entity.Student;
import com.demo.admissionportal.entity.University;
import com.demo.admissionportal.entity.resetPassword.ResetPassword;
import com.demo.admissionportal.repository.ConsultantRepository;
import com.demo.admissionportal.repository.StaffRepository;
import com.demo.admissionportal.repository.StudentRepository;
import com.demo.admissionportal.repository.UniversityRepository;
import com.demo.admissionportal.service.ResetPasswordService;
import com.demo.admissionportal.util.EmailUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Implementation of the ResetPasswordService interface.
 */
@Service
@Slf4j
public class ResetPasswordServiceImpl implements ResetPasswordService {
    @Autowired
    private StaffRepository staffRepository;
    @Autowired
    private UniversityRepository universityRepository;
    @Autowired
    private ConsultantRepository consultantRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private EmailUtil emailUtil;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * Saves the reset password account to Redis cache.
     *
     * @param role the role of the account
     * @param id the ID of the account
     * @param resetToken the reset token
     */
    @Override
    public void saveObject(Role role, Integer id, UUID resetToken) {
        ResetPasswordAccountRedisCacheDTO resetPasswordAccountRedisCacheDTO = new ResetPasswordAccountRedisCacheDTO(role, id, 10);
        redisTemplate.opsForValue().set("resetpw_" + resetToken, resetPasswordAccountRedisCacheDTO, 3, TimeUnit.MINUTES);
    }

    /**
     * Retrieves the reset password account from Redis cache using the reset token.
     *
     * @param resetToken the reset token
     * @return the reset password account Redis cache DTO
     */
    @Override
    public ResetPasswordAccountRedisCacheDTO getResetPasswordAccountRedisCacheDTO(UUID resetToken) {
        log.info("Get token: {}", resetToken);
        ResetPasswordAccountRedisCacheDTO resetPasswordAccountRedisCacheDTO = (ResetPasswordAccountRedisCacheDTO) redisTemplate.opsForValue().get("resetpw_" + resetToken);
        if (resetPasswordAccountRedisCacheDTO == null) {
            log.warn("No token found: {}", resetToken);
        }
        return resetPasswordAccountRedisCacheDTO;
    }

    /**
     * Handles the reset password request.
     *
     * @param request the reset password request
     * @return the response data
     */
    @Override
    public ResponseData<?> resetPasswordRequest(ResetPasswordRequest request) {
        ResponseData<ResetPassword> responseData = findByEmail(request.email());

        if (responseData.getStatus() != ResponseCode.C200.getCode()) {
            log.warn("Object with email: {} not found", request.email());
            return responseData;
        }

        ResetPassword existingAccount = responseData.getData();
        UUID resetToken = UUID.randomUUID();
        existingAccount.setResetPassToken(resetToken.toString());
        saveObject(existingAccount.getRole(), existingAccount.getId(), resetToken);
        String resetPassLink = "https://localhost:3000/reset-password/" + resetToken;
        String message = "Bạn hãy nhập vào đường link để tạo lại mật khẩu: " + resetPassLink;
        String subject = "Cổng thông tin tuyển sinh - Tạo lại mật khẩu cho tài khoản: " + existingAccount.getId();
        emailUtil.sendHtmlEmail(request.email(), subject, message);
        return new ResponseData<>(ResponseCode.C206.getCode(), "Đã gửi đường dẫn tạo lại mật khẩu vào Email. Xin vui lòng kiểm tra");
    }

    /**
     * Confirms the reset password request using the reset token.
     *
     * @param request the confirm reset password request
     * @param resetToken the reset token
     * @return the response data
     */
    @Override
    public ResponseData<?> confirmResetPassword(ConfirmResetPasswordRequest request, String resetToken) {
        ResetPasswordAccountRedisCacheDTO resetPasswordAccountRedisCacheDTO = getResetPasswordAccountRedisCacheDTO(UUID.fromString(resetToken));
        if (resetPasswordAccountRedisCacheDTO == null) {
            log.error("Reset password token {} not found in cache", resetToken);
            return new ResponseData<>(ResponseCode.C203.getCode(), "Token không hợp lệ hoặc đã hết hạn !");
        }

        ResponseData<ResetPassword> responseData = findById(resetPasswordAccountRedisCacheDTO.getId());
        if (responseData.getStatus() != ResponseCode.C200.getCode()) {
            return responseData;
        }

        ResetPassword foundAccount = responseData.getData();
        foundAccount.setPassword(passwordEncoder.encode(request.newPassword()));
        foundAccount.setResetPassToken(null);

        if (foundAccount instanceof Staff) {
            staffRepository.save((Staff) foundAccount);
        } else if (foundAccount instanceof University) {
            universityRepository.save((University) foundAccount);
        } else if (foundAccount instanceof Consultant) {
            consultantRepository.save((Consultant) foundAccount);
        } else if (foundAccount instanceof Student){
            studentRepository.save((Student) foundAccount);
        }
        return new ResponseData<>(ResponseCode.C200.getCode(), ResponseCode.C200.getMessage(), true);
    }

    /**
     * Finds an account by email.
     *
     * @param email the email
     * @return the response data containing the found account
     */
    private ResponseData<ResetPassword> findByEmail(String email) {
        Staff staff = staffRepository.findByEmail(email);
        if (staff != null) {
            return new ResponseData<>(ResponseCode.C200.getCode(), ResponseCode.C200.getMessage(), staff);
        }
        University university = universityRepository.findByEmail(email);
        if (university != null) {
            return new ResponseData<>(ResponseCode.C200.getCode(), ResponseCode.C200.getMessage(), university);
        }
        Optional<Consultant> consultant = consultantRepository.findByEmail(email);
        if (consultant.isPresent()) {
            return new ResponseData<>(ResponseCode.C200.getCode(), ResponseCode.C200.getMessage(), consultant.get());
        }
        Student student = studentRepository.findByEmail(email);
        if(student != null){
            return new ResponseData<>(ResponseCode.C200.getCode(), ResponseCode.C200.getMessage(), student);
        }
        return new ResponseData<>(ResponseCode.C203.getCode(), "Không tìm thấy email này trong hệ thống");
    }

    /**
     * Finds an account by ID.
     *
     * @param id the ID
     * @return the response data containing the found account
     */
    private ResponseData<ResetPassword> findById(Integer id) {
        Optional<Staff> staff = staffRepository.findById(id);
        if (staff.isPresent()) {
            return new ResponseData<>(ResponseCode.C200.getCode(), ResponseCode.C200.getMessage(), staff.get());
        }
        Optional<University> university = universityRepository.findById(id);
        if (university.isPresent()) {
            return new ResponseData<>(ResponseCode.C200.getCode(), ResponseCode.C200.getMessage(), university.get());
        }
        Optional<Consultant> consultant = consultantRepository.findById(id);
        if (consultant.isPresent()) {
            return new ResponseData<>(ResponseCode.C200.getCode(), ResponseCode.C200.getMessage(), consultant.get());
        }
        Optional<Student> student = studentRepository.findById(id);
        if (student.isPresent()) {
            return new ResponseData<>(ResponseCode.C200.getCode(), ResponseCode.C200.getMessage(), student.get());
        }
        return new ResponseData<>(ResponseCode.C203.getCode(), "Không tìm thấy ID này trong hệ thống");
    }
}
