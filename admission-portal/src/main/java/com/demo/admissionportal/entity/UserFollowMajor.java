package com.demo.admissionportal.entity;

import com.demo.admissionportal.constants.FavoriteStatus;
import com.demo.admissionportal.entity.sub_entity.id.UserFollowMajorId;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;

import java.time.Instant;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "user_follow_major")
public class UserFollowMajor {
    @EmbeddedId
    private UserFollowMajorId id;

    @MapsId("userId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @MapsId("majorId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "major_id")
    private Major major;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private FavoriteStatus status;

}