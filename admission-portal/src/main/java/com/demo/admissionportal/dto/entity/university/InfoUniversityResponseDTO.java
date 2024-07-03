package com.demo.admissionportal.dto.entity.university;

import com.demo.admissionportal.constants.AccountStatus;
import com.demo.admissionportal.constants.UniversityType;
import com.demo.admissionportal.entity.UniversityInfo;
import com.demo.admissionportal.entity.User;
import lombok.*;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InfoUniversityResponseDTO {
    private Integer id;

    private String name;

    private String code;

    private String email;

    private String username;

    private UniversityType type;

    private AccountStatus status;

    public InfoUniversityResponseDTO(User account, UniversityInfo universityInfo) {
        this.id = account.getId();
        this.name = universityInfo.getName();
        this.code = universityInfo.getCode();
        this.email = account.getEmail();
        this.username = account.getUsername();
        this.type = universityInfo.getType();
        this.status = account.getStatus();
    }
}
