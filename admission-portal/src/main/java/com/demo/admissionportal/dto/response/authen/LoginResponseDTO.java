package com.demo.admissionportal.dto.response.authen;

import com.demo.admissionportal.constants.Role;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoginResponseDTO implements Serializable {
    private String accessToken;
    private UserLoginResponseDTO user;
    private UserInfoResponseDTO userInfo;
    private AdminInfoResponseDTO adminInfo;
    private ConsultantInfoResponseDTO consultantInfo;
    private StaffInfoResponseDTO staffInfo;
    private UniversityInfoResponseDTO universityInfo;

    /**
     * The type User login response dto.
     */
    @Data
    public static class UserLoginResponseDTO implements Serializable {
        private Integer id;
        private String email;
        private String username;
        private String avatar;
        private Role role;
    }
}
