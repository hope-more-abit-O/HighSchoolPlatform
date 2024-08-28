package com.demo.admissionportal.entity;

import com.demo.admissionportal.constants.FavoriteStatus;
import com.demo.admissionportal.entity.sub_entity.id.UserFollowUniversityMajorId;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "user_follow_university_major")
public class UserFollowUniversityMajor {
    @EmbeddedId
    private UserFollowUniversityMajorId id;

    @MapsId("userId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @MapsId("universityMajor")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "university_major")
    private UniversityTrainingProgram universityMajor;

    @NotBlank
    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private FavoriteStatus status;

}