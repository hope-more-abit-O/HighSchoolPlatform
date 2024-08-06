package com.demo.admissionportal.entity;

import com.demo.admissionportal.constants.UniversityPackageStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.util.Date;

/**
 * The type University package.
 */
@Getter
@Setter
@Entity
@Table(name = "university_package")
public class UniversityPackage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "university_id")
    private Integer universityId;

    @Column(name = "university_transaction_id")
    private Integer universityTransactionId;

    @Column(name = "post_id")
    private Integer postId;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "complete_time")
    private Date completeTime;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private UniversityPackageStatus status;
}