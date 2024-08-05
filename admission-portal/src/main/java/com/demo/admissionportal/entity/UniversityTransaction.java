package com.demo.admissionportal.entity;

import com.demo.admissionportal.constants.UniversityTransactionStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.util.Date;

/**
 * The type University transaction.
 */
@Getter
@Setter
@Entity
@Table(name = "university_transaction")
public class UniversityTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "create_by")
    private Integer createBy;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;

    @Column(name = "update_by")
    private Integer updateBy;

    @Column(name = "complete_time")
    private Date completeTime;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private UniversityTransactionStatus status;
}