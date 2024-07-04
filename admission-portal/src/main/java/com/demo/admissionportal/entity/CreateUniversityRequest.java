package com.demo.admissionportal.entity;

import com.demo.admissionportal.constants.CreateUniversityRequestStatus;
import com.demo.admissionportal.constants.UniversityType;
import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.Date;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "create_university_request")
@Builder
public class CreateUniversityRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Integer id;

    @Column(length = 250)
    private String universityName;

    @Column(length = 10)
    private String universityCode;

    @Column(length = 250)
    private String universityEmail;

    @Column(length = 30)
    private String universityUsername;

    @Column(length = 30)
    @Enumerated(EnumType.STRING)
    private UniversityType universityType;

    @Column(length = 700)
    private String note;

    @Column(length = 500)
    private String documents;

    @Column(nullable = false)
    private Integer createBy;

    @Column(nullable = false)
    private Date createTime;

    @Column
    private Integer updateBy;

    @Column
    private Date updateTime;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CreateUniversityRequestStatus status;

    @Column
    private Integer confirmBy;

}
