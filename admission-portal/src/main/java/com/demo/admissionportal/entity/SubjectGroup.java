package com.demo.admissionportal.entity;

import com.demo.admissionportal.constants.SubjectStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;

import java.util.Date;

/**
 * The type Subject group.
 */
@Data
@Entity
@Table(name = "subject_group")
public class SubjectGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Size(max = 3)
    @NotBlank
    @Column(name = "name")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private SubjectStatus status;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "create_by")
    private Integer createBy;

    @Column(name = "update_time")
    private Date updateTime;

    @Column(name = "update_by")
    private Integer updateBy;

}