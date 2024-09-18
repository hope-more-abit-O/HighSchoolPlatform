package com.demo.admissionportal.dto.entity.admission;

import com.demo.admissionportal.dto.entity.university_campus.CampusProvinceDTO;
import com.demo.admissionportal.dto.response.admission.CompareMajorDTO;
import com.demo.admissionportal.entity.UniversityInfo;
import com.demo.admissionportal.entity.User;
import com.demo.admissionportal.entity.admission.Admission;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UniversityCompareMajorDTO {
    private Integer universityId;
    private Integer admissionId;
    private String admissionSource;
    private Integer year;
    private String avatar;
    private String name;
    private String code;
    private String type;
    private String description;
    private String coverImage;
    private List<CompareMajorDTO> compareMajorDTOS;

    public UniversityCompareMajorDTO(User user, UniversityInfo universityInfo, List<CompareMajorDTO> compareMajorDTOS, Admission admission) {
        this.universityId = universityInfo.getId();
        this.admissionId = admission.getId();
        this.admissionSource = admission.getSource();
        this.year = admission.getYear();
        this.avatar = user.getAvatar();
        this.name = universityInfo.getName();
        this.code = universityInfo.getCode();
        this.type = universityInfo.getType().name;
        this.description = universityInfo.getDescription();
        this.coverImage = universityInfo.getCoverImage();
        this.compareMajorDTOS = compareMajorDTOS;
    }
}
