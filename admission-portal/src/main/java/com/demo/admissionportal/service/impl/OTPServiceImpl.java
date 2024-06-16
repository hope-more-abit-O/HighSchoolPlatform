package com.demo.admissionportal.service.impl;

import com.demo.admissionportal.constants.Role;
import com.demo.admissionportal.dto.request.AccountRedisCacheDTO;
import com.demo.admissionportal.entity.Student;
import com.demo.admissionportal.entity.redis.OTPRedisCache;
import com.demo.admissionportal.service.OTPService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

/**
 * The type Otp service.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class OTPServiceImpl implements OTPService {
    private final RedisTemplate<String, Object> redisTemplate;

    // Save OTP in Redis Cache
    @Override
    public void saveOTP(String email, String otp, LocalDateTime timeGenerate) {
        // Save OTP in Redis with 3 minutes
        OTPRedisCache account = new OTPRedisCache(email, otp, timeGenerate, 10);
        redisTemplate.opsForValue().set(email + "_otp", account, 3, TimeUnit.MINUTES);
    }

    /**
     * Gets otp.
     *
     * @param email the email
     * @return the otp
     */
    // Get OTP by key from Redis Cache
    public String getOTP(String email) {
        log.info("Retrieving OTP for email: {}", email);
        OTPRedisCache account = (OTPRedisCache) redisTemplate.opsForValue().get(email + "_otp");
        if (account == null) {
            log.warn("No account found for email: {}", email);
            return null;
        }
        log.info("Retrieved account: {}", account);
        return account.getOtp();
    }

    /**
     * Delete otp.
     *
     * @param email the email
     */
    public void deleteOTP(String email) {
        redisTemplate.delete(email + "_otp");
    }

    // Get OTP Time in Redis Cache
    @Override
    public LocalDateTime getOTPDateTime(String email) {
        try {
            log.info("Retrieving OTPDateTime for email: {}", email);
            OTPRedisCache account = (OTPRedisCache) redisTemplate.opsForValue().get(email + "_otp");
            if (account == null) {
                log.warn("No OTPDateTime found for email: {}", email);
                return null;
            }
            return account.getOtpGeneratedTime();
        } catch (Exception ex) {
            log.error("Get OTP time error: {}", ex.getMessage());
            return null;
        }
    }

    // Save student in Redis Cache
    @Override
    public void saveStudent(String email, Student student) {
        AccountRedisCacheDTO account = convertRedisCacheDTO(student);
        redisTemplate.opsForValue().set(email + "_info", account);
    }

    // Get student in Redis Cache
    @Override
    public Student getStudent(String email) {
        log.info("Retrieving Student by email: {}", email);
        AccountRedisCacheDTO student = (AccountRedisCacheDTO) redisTemplate.opsForValue().get(email + "_info");
        if (student == null) {
            log.warn("No Student found: {}", (Object) null);
            return null;
        }
        return converToStudent(student);
    }

    // Convert Student to AccountRedisCacheDTO
    private AccountRedisCacheDTO convertRedisCacheDTO(Student student) {
        return AccountRedisCacheDTO.builder()
                .username(student.getUsername())
                .firstname(student.getFirstname())
                .middleName(student.getMiddleName())
                .lastName(student.getLastName())
                .email(student.getEmail())
                .password(student.getPassword())
                .addressId(student.getAddressId())
                .birthday(student.getBirthday())
                .educationLevel(student.getEducationLevel())
                .avatar(student.getAvatar())
                .phone(student.getPhone())
                .gender(student.getGender())
                .status(student.getStatus())
                .role(Role.STUDENT)
                .build();
    }

    // Convert Student to AccountRedisCacheDTO
    private Student converToStudent(AccountRedisCacheDTO accountRedisCacheDTO) {
        return Student.builder()
                .username(accountRedisCacheDTO.getUsername())
                .firstname(accountRedisCacheDTO.getFirstname())
                .middleName(accountRedisCacheDTO.getMiddleName())
                .lastName(accountRedisCacheDTO.getLastName())
                .email(accountRedisCacheDTO.getEmail())
                .password(accountRedisCacheDTO.getPassword())
                .addressId(accountRedisCacheDTO.getAddressId())
                .birthday(accountRedisCacheDTO.getBirthday())
                .educationLevel(accountRedisCacheDTO.getEducationLevel())
                .avatar(accountRedisCacheDTO.getAvatar())
                .phone(accountRedisCacheDTO.getPhone())
                .gender(accountRedisCacheDTO.getGender())
                .status(accountRedisCacheDTO.getStatus())
                .role(Role.STUDENT)
                .build();
    }

}
