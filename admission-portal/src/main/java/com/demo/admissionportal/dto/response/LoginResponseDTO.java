package com.demo.admissionportal.dto.response;

import jakarta.persistence.Column;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Nationalized;

import java.io.Serializable;
import java.util.Date;

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

    /**
     * The type User info response dto.
     */
    @Data
    public static class UserInfoResponseDTO implements Serializable {
        private String firstName;
        private String middleName;
        private String lastName;
        private String phone;
        private String gender;
        private String specificAddress;
        private String educationLevel;
        private Integer province;
        private Integer district;
        private Integer ward;
        private Date birthday;
    }
}
