package com.demo.admissionportal.dto.entity;

import com.demo.admissionportal.constants.Role;
import com.demo.admissionportal.entity.UniversityInfo;
import com.demo.admissionportal.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ActionerDTO {
    private Integer id;
    private String name;
    private Role role;

    public ActionerDTO(User account, UniversityInfo universityInfo){
        this.id = account.getId();
        this.name = universityInfo.getName();
        this.role = account.getRole();
    }
}
