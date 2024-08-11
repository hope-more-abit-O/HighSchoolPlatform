package com.demo.admissionportal.entity;

import com.demo.admissionportal.constants.AccountStatus;
import com.demo.admissionportal.constants.HollandCharacteristicType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.C;

import java.util.Date;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "question")
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "content")
    private String content;
    @Column(name = "create_by")
    private Integer createBy;
    @Column(name = "create_time")
    private Date createTime;
    @Column(name = "update_by")
    private Integer updateBy;
    @Column(name = "update_time")
    private Date updateTime;
    @Column(name = "type")
    private Integer type;
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private AccountStatus status;
}
