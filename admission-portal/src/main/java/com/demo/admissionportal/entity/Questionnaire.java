package com.demo.admissionportal.entity;

import com.demo.admissionportal.constants.QuestionStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "questionnaire")
public class Questionnaire {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "questionnaire_code")
    private String code;
    @Column(name = "name")
    private String name;
    @Column(name = "description")
    private String desciption;
    @Column(name = "cover_image")
    private String coverImage;
    @Column(name = "create_by")
    private Integer createBy;
    @Column(name = "create_time")
    private Date createTime;
    @Column(name = "update_by")
    private Integer updateBy;
    @Column(name = "update_time")
    private Date updateTime;
    @Enumerated(value = EnumType.STRING)
    @Column(name = "status")
    private QuestionStatus status;
}
