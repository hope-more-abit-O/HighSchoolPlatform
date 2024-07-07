package com.demo.admissionportal.service.impl;

import com.demo.admissionportal.dto.request.authen.CodeVerifyAccountRequestDTO;
import com.demo.admissionportal.entity.User;
import com.demo.admissionportal.entity.UserInfo;
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

    @Override
    public void saveOTP(String email, String otp, LocalDateTime timeGenerate) {
        OTPRedisCache account = new OTPRedisCache(email, otp, timeGenerate, 10);
        redisTemplate.opsForValue().set(email + "_otp", account, 3, TimeUnit.MINUTES);
    }


    @Override
    public void saveUser(String email, User user, UserInfo userInfo) {
        redisTemplate.opsForValue().set(email, user);
        redisTemplate.opsForValue().set(email + "_info", userInfo);
    }

    @Override
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

    @Override
    public User getUser(String email) {
        log.info("Retrieving user by email: {}", email);
        User user = (User) redisTemplate.opsForValue().get(email);
        if (user == null) {
            log.warn("No user found: {}", (Object) null);
            return null;
        }
        return user;
    }

    @Override
    public UserInfo getUserInfo(String email) {
        log.info("Retrieving user_info by email: {}", email);
        UserInfo userInfo = (UserInfo) redisTemplate.opsForValue().get(email + "_info");
        if (userInfo == null) {
            log.warn("No user_info found: {}", (Object) null);
            return null;
        }
        return userInfo;
    }

    @Override
    public void deleteOTP(String email) {
        redisTemplate.delete(email + "_otp");
    }

    @Override
    public void savesUID(String email, CodeVerifyAccountRequestDTO codeVerifyAccountRequestDTO) {
        redisTemplate.opsForValue().set(email + "_sUID", codeVerifyAccountRequestDTO);
    }

    @Override
    public String getsUID(String email) {
        log.info("Retrieving sUID by email: {}", email);
        CodeVerifyAccountRequestDTO codeVerifyAccountRequestDTO = (CodeVerifyAccountRequestDTO) redisTemplate.opsForValue().get(email + "_sUID");
        if (codeVerifyAccountRequestDTO == null) {
            log.warn("No sUID found: {}", (Object) null);
            return null;
        }
        return codeVerifyAccountRequestDTO.getSUID();
    }
}