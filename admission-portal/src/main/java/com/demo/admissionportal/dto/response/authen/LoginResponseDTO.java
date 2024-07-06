package com.demo.admissionportal.dto.response;

import com.demo.admissionportal.constants.Gender;
import com.demo.admissionportal.entity.District;
import com.demo.admissionportal.entity.Province;
import com.demo.admissionportal.entity.Ward;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * The type Login response dto.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseDTO implements Serializable {
    private String accessToken;
    private UserLoginResponseDTO user;
    private UserInfoResponseDTO userInfo;

    /**
     * The type User login response dto.
     */
    @Data
    public static class UserLoginResponseDTO implements Serializable {
        private String email;
        private String username;
    }
}
