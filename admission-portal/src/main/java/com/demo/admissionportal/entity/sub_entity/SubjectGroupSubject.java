package com.demo.admissionportal.entity.sub_entity;

import com.demo.admissionportal.entity.sub_entity.id.SubjectGroupSubjectId;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@IdClass(SubjectGroupSubjectId.class)
@Table(name = "subject_group_subject")
public class SubjectGroupSubject implements Serializable {

    @Id
    @Column(name = "subject_id")
    private Integer subjectId;

    @Id
    @Column(name = "subject_group_id")
    private Integer subjectGroupId;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "create_by")
    private Integer createBy;

    @Column(name = "update_time")
    private Date updateTime;

    @Column(name = "update_by")
    private Integer updateBy;



    @Size(max = 255)
    @NotBlank
    @Nationalized
    @ColumnDefault("ACTIVE")
    @Column(name = "status")
    private String status;

    public SubjectGroupSubject(Integer subjectId, Integer subjectGroupId) {
        this.subjectId = subjectId;
        this.subjectGroupId = null;
    }
}
