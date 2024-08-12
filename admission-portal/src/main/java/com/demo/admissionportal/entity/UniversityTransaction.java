package com.demo.admissionportal.entity;

import com.demo.admissionportal.constants.UniversityTransactionStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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

    @Column(name = "university_id")
    private Integer universityId;

    @Column(name = "ads_package_id")
    private Integer packageId;

    @Column(name = "create_by")
    private Integer createBy;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "complete_time")
    private Date completeTime;

    @Column(name = "update_by")
    private Integer updateBy;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private UniversityTransactionStatus status;

    @Column(name = "order_code")
    private Long orderCode;
}