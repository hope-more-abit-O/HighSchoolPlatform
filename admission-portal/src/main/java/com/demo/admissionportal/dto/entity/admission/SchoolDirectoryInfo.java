package com.demo.admissionportal.dto.entity.admission;

import com.demo.admissionportal.dto.entity.university_campus.CampusProvinceDTO;
import com.demo.admissionportal.dto.response.ProvinceDTO;
import com.demo.admissionportal.entity.UniversityInfo;
import com.demo.admissionportal.entity.UniversityTrainingProgram;
import com.demo.admissionportal.entity.User;
import com.demo.admissionportal.entity.admission.Admission;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SchoolDirectoryInfo {
    private Integer id;
    private Integer admissionId;
    private Integer year;
    private String avatar;
    private String name;
    private String code;
    private String type;
    private String description;
    private String coverImage;
    private Integer universityTrainingProgramCount;
    private Integer methodCount;
    private Integer majorCount;
    private Integer campusCount;
    private Integer totalQuota;
    private List<CampusProvinceDTO> campus;

    public SchoolDirectoryInfo(User user,
                               UniversityInfo universityInfo,
                               List<UniversityTrainingProgram> universityTrainingPrograms,
                               List<CampusProvinceDTO> campus, Integer totalQuota,
                               Admission admission,
                               Integer methodCount,
                               Integer majorCount) {
        this.id = user.getId();
        this.admissionId = admission.getId();
        this.year = admission.getYear();
        this.avatar = user.getAvatar();
        this.name = universityInfo.getName();
        this.code = universityInfo.getCode();
        this.type = universityInfo.getType().name;
        this.description = universityInfo.getDescription();
        this.coverImage = universityInfo.getCoverImage();
        this.universityTrainingProgramCount = universityTrainingPrograms.size();
        this.campusCount = campus.size();
        this.totalQuota = totalQuota;
        this.campus = campus;
        this.methodCount = methodCount;
        this.majorCount = majorCount;
    }
}
