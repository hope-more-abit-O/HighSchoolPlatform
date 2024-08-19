package com.demo.admissionportal.entity;

import com.demo.admissionportal.constants.UniversityMajorStatus;
import com.demo.admissionportal.entity.sub_entity.id.UniversityMajorId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "university_major")
public class UniversityMajor {
    @EmbeddedId
    private UniversityMajorId id;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private UniversityMajorStatus status;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "create_by")
    private Integer createBy;

    @Column(name = "update_time")
    private Date updateTime;

    @Column(name = "update_by")
    private Integer updateBy;

    public UniversityMajor(Integer universityId, Integer majorId, Integer createBy){
        this.id = new UniversityMajorId(universityId, majorId);
        this.createBy = createBy;
        this.createTime = new Date();
        this.status = UniversityMajorStatus.ACTIVE;
    }

    public void updateStatus(UniversityMajorStatus status, Integer updateBy){
        this.status = status;
        this.updateBy = updateBy;
        this.updateTime = new Date();
    }

}
