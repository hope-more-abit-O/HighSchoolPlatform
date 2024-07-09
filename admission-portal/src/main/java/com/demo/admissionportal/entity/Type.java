package com.demo.admissionportal.entity;

import com.demo.admissionportal.constants.PostPropertiesStatus;
import com.demo.admissionportal.entity.sub_entity.PostType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * The entity Type.
 */
@Getter
@Setter
@Entity
@Table(name = "type")
public class Type {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "create_by")
    private Integer createBy;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;

    @Column(name = "update_by")
    private Integer updateBy;


    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private PostPropertiesStatus status;

    @OneToOne(mappedBy = "type")
    private PostType postTypes;

}