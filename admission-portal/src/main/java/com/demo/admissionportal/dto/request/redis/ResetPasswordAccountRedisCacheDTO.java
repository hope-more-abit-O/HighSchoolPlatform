package com.demo.admissionportal.dto.request.redis;

import com.demo.admissionportal.constants.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResetPasswordAccountRedisCacheDTO implements Serializable {
    private Role role;
    private Integer id;
    private int resetTokenTimeout;
}
