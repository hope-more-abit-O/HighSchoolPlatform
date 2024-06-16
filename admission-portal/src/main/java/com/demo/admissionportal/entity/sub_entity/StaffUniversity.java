package com.demo.admissionportal.entity.sub_entity;

import com.demo.admissionportal.entity.sub_entity.id.StaffUniversityId;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@IdClass(StaffUniversityId.class)
@Table(name = "[staff_university]")
public class StaffUniversity {
    @Id
    @Column(name = "staff_id")
    private Integer staffId;

    @Id
    @Column(name = "university_id")
    private Integer universityId;

    @Column(name = "create_time")
    private LocalDateTime createTime;

    public StaffUniversity(Integer staffId, Integer universityId) {
        this.staffId = staffId;
        this.universityId = universityId;
        createTime = LocalDateTime.now();
    }
}
