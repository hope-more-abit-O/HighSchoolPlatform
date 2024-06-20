package com.demo.admissionportal.dto.request.redis;

import com.demo.admissionportal.constants.Role;
import com.demo.admissionportal.entity.Staff;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResetPasswordAccountRedisCacheDTO implements Serializable {
    private Role role;
    private Integer id;

}
