package com.demo.admissionportal.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * The type Staff info.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "staff_info")
@PrimaryKeyJoinColumn(name = "staff_id")
public class StaffInfo extends User {
    @Column(name = "admin_id")
    private Integer adminId;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "middle_name")
    private String middleName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "phone")
    private String phone;
    @ManyToOne
    @JoinColumn(name = "admin_id", insertable = false, updatable = false)
    private User admin;
}
