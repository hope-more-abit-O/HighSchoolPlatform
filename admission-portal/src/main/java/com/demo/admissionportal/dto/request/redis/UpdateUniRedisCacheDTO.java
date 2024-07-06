package com.demo.admissionportal.dto.request.redis;

import com.demo.admissionportal.constants.AccountStatus;
import com.demo.admissionportal.constants.UniversityType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * The type Update uni redis cache dto.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateUniRedisCacheDTO implements Serializable {
    private String code;
    private String username;
    private String name;
    private String email;
    private String description;
    private String password;
    private String phone;
    @Enumerated(EnumType.STRING)
    private UniversityType type;
    private String avatar;
    @Enumerated(EnumType.STRING)
    private AccountStatus status;
}