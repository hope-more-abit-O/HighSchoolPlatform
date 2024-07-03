package com.demo.admissionportal.dto.entity.consultant;

import com.demo.admissionportal.constants.AccountStatus;
import com.demo.admissionportal.constants.Gender;
import com.demo.admissionportal.dto.entity.university.InfoUniversityResponseDTO;
import com.demo.admissionportal.entity.ConsultantInfo;
import com.demo.admissionportal.entity.User;
import lombok.*;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InfoConsultantDTO {
    private Integer id;
    private InfoUniversityResponseDTO university;
    private String name;
    private String phone;
    private String address;
    private Gender gender;
    private AccountStatus status;

    public InfoConsultantDTO(User account, ConsultantInfo consultantInfo, InfoUniversityResponseDTO university) {
        this.id = account.getId();
        this.university = university;
        this.name = consultantInfo.getFirstname() + " " + consultantInfo.getMiddleName() + " "  + consultantInfo.getLastName();
        this.phone = consultantInfo.getPhone();
        this.address = consultantInfo.getSpecificAddress() + ", " + consultantInfo.getWard().getName() + ", " + consultantInfo.getDistrict().getName() + ", " + consultantInfo.getProvince().getName();
        this.gender = consultantInfo.getGender();
        this.status = account.getStatus();
    }
}
