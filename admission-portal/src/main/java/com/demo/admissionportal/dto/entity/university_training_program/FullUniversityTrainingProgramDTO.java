package com.demo.admissionportal.dto.entity.university_training_program;

import com.demo.admissionportal.dto.entity.major.InfoMajorDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FullUniversityTrainingProgramDTO {
    private Integer id;
    private Integer universityId;
    private InfoMajorDTO major;
    private String trainingSpecific;
    private String language;
    private String status;
    private Date createTime;
    private Integer createBy;
    private Date updateTime;
    private Integer updateBy;
}
