package com.demo.admissionportal.dto.request;

import com.demo.admissionportal.constants.Role;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.deser.std.DateDeserializers;
import com.fasterxml.jackson.databind.ser.std.DateSerializer;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;

import java.io.Serializable;
import java.util.Date;

/**
 * The type Account redis cache dto.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountRedisCacheDTO implements Serializable {
    @NotNull
    private String username;

    @NotNull
    @Nationalized
    private String firstname;

    @NotNull
    @Nationalized
    private String middleName;

    @NotNull
    @Nationalized
    private String lastName;

    @NotNull
    private String email;

    @NotNull
    private String password;

    @NotNull
    private Integer addressId;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonSerialize(using = DateSerializer.class)
    @JsonDeserialize(using = DateDeserializers.DateDeserializer.class)
    @Temporal(TemporalType.DATE)
    private Date birthday;

    @NotNull
    @Nationalized
    private int educationLevel;

    @NotNull
    private String avatar;

    @NotNull
    private String phone;

    @NotNull
    @Nationalized
    @ColumnDefault("MALE")
    private String gender;

    @NotNull
    @Nationalized
    @ColumnDefault("ACTIVE")
    private String status;

    @Enumerated(EnumType.STRING)
    private Role role;
}
