package com.demo.admissionportal.entity.sub_entity;

import com.demo.admissionportal.entity.sub_entity.id.StaffUniversityId;
import com.demo.admissionportal.entity.sub_entity.id.UniversityConsultantId;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString
@IdClass(UniversityConsultantId.class)
@Table(name = "[university_consultant]")
public class UniversityConsultant {
    @Id
    @Column(name = "university_id")
    private Integer universityId;

    @Id
    @Column(name = "consultant_id")
    private Integer consultantId;

    @Column(name = "create_time")
    private LocalDateTime createTime;

    public UniversityConsultant(Integer universityId, Integer consultantId) {
        this.consultantId = consultantId;
        this.universityId = universityId;
        createTime = LocalDateTime.now();
    }
}
