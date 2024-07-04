package com.demo.admissionportal.service.resetPassword.impl;

import com.demo.admissionportal.constants.AccountStatus;
import com.demo.admissionportal.constants.ResponseCode;
import com.demo.admissionportal.constants.Role;
import com.demo.admissionportal.dto.request.redis.ResetPasswordAccountRedisCacheDTO;
import com.demo.admissionportal.dto.request.resetPass.request.ConfirmResetPasswordRequest;
import com.demo.admissionportal.dto.request.resetPass.request.ResetPasswordRequest;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.entity.User;
import com.demo.admissionportal.entity.resetPassword.ResetPassword;
import com.demo.admissionportal.repository.UserRepository;
import com.demo.admissionportal.service.resetPassword.ResetPasswordService;
import com.demo.admissionportal.util.impl.EmailUtil;
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
    private UserRepository userRepository;
    @Autowired
    private EmailUtil emailUtil;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

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
            log.warn("User with email: {} not found", request.email());
            return responseData;
        }
        ResetPassword existingAccount = responseData.getData();

        if (existingAccount instanceof User) {
            User user = (User) existingAccount;
            if (user.getStatus().equals(AccountStatus.INACTIVE.name())){
                return new ResponseData<>(ResponseCode.C203.getCode(), "Người dùng không tồn tại !");
            }
            if (user.getRole() == Role.ADMIN) {
                return new ResponseData<>(ResponseCode.C201.getCode(), "Email không hợp lệ");
            }
            UUID resetToken = UUID.randomUUID();
            existingAccount.setResetPassToken(resetToken.toString());
            saveObject(existingAccount.getRole(), existingAccount.getId(), resetToken);

            String resetPassLink = "https://localhost:3000/reset-password/" + resetToken;
            String message = "Bạn hãy nhập vào đường link để tạo lại mật khẩu: " + resetPassLink;
            String subject = "Cổng thông tin tuyển sinh - Tạo lại mật khẩu cho tài khoản: " + existingAccount.getId();

            emailUtil.sendHtmlEmail(request.email(), subject, message);
        }
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

        ResetPassword foundUser = responseData.getData();

        if (foundUser instanceof User) {
            User user = (User) foundUser;

            if (user.getRole() == Role.STAFF || user.getRole() == Role.USER || user.getRole() == Role.CONSULTANT || user.getRole() == Role.UNIVERSITY) {
                if (!request.getNewPassword().trim().equals(request.getConfirmNewPassword().trim())) {
                    return new ResponseData<>(ResponseCode.C201.getCode(), "Mật khẩu không khớp !");
                } else if (request.getNewPassword().trim().isBlank()) {
                    return new ResponseData<>(ResponseCode.C201.getCode(), "Mật khẩu không được để trống !");
                } else if (request.getConfirmNewPassword().trim().isBlank()) {
                    return new ResponseData<>(ResponseCode.C201.getCode(), "Mật khẩu không được để trống !");
                }
                log.info("Password reset for account: {}", user.getId());
            }

            foundUser.setPassword(passwordEncoder.encode(request.getNewPassword().trim()));
            foundUser.setResetPassToken(null);

            switch (user.getRole()) {
                case STAFF:
                    log.info("Password reset for staff: {}", user.getId());
                    userRepository.save(user);
                    break;
                case CONSULTANT:
                    log.info("Password reset for consultant: {}", user.getId());
                    userRepository.save(user);
                    break;
                case UNIVERSITY:
                    log.info("Password reset for university: {}", user.getId());
                    userRepository.save(user);
                    break;
                case USER:
                    log.info("Password reset for user: {}", user.getId());
                    userRepository.save(user);
                    break;
                default:
                    log.warn("Unknown role for user: {}", user.getRole());
                    break;
            }
        }
        return new ResponseData<>(ResponseCode.C200.getCode(), ResponseCode.C200.getMessage(), foundUser);
    }

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
     * Finds an account by email.
     *
     * @param email the email
     * @return the response data containing the found account
     */
    private ResponseData<ResetPassword> findByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            return new ResponseData<>(ResponseCode.C200.getCode(), ResponseCode.C200.getMessage(),user.get());
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
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            return new ResponseData<>(ResponseCode.C200.getCode(), ResponseCode.C200.getMessage(),user.get());
        }
        return new ResponseData<>(ResponseCode.C203.getCode(), "Không tìm thấy ID này trong hệ thống");
    }
}
