package com.demo.admissionportal.entity;

import com.demo.admissionportal.constants.FavoriteStatus;
import com.demo.admissionportal.entity.sub_entity.id.UserFollowUniversityMajorId;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "university_major")
    private UniversityTrainingProgram universityMajor;

    @NotNull
    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private FavoriteStatus status;

    @Column(name = "index_of_follow")
    private Integer indexOfFollow;

    @Column(name = "fcm_token")
    private String fcmToken;
}