package com.demo.admissionportal.dto.request.redis;

import com.demo.admissionportal.entity.Staff;
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
    private String email;
}
