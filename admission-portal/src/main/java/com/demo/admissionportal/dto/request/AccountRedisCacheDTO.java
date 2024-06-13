package com.demo.admissionportal.dto.request;

import com.demo.admissionportal.constants.Role;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * The type Account redis cache dto.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountRedisCacheDTO implements Serializable {
    @Size(max = 20)
    @NotNull
    private String username;

    @Size(max = 20)
    @NotNull
    @Nationalized
    private String firstname;

    @Size(max = 20)
    @NotNull
    @Nationalized
    private String middleName;

    @Size(max = 20)
    @NotNull
    @Nationalized
    private String lastName;

    @NotNull
    private String email;

    @Size(max = 100)
    @NotNull
    private String password;

    @NotNull
    private Integer addressId;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime birthday;

    @Size(max = 10)
    @NotNull
    @Nationalized
    private String educationLevel;

    @Size(max = 20)
    @NotNull
    private String avatar;

    @Size(max = 11)
    @NotNull
    private String phone;

    @Size(max = 255)
    @NotNull
    @Nationalized
    @ColumnDefault("MALE")
    private String gender;

    @Size(max = 255)
    @NotNull
    @Nationalized
    @ColumnDefault("ACTIVE")
    private String status;

    @Enumerated(EnumType.STRING)
    private Role role;
}
