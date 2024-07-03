package com.demo.admissionportal.entity;

import com.demo.admissionportal.util.EnumPassword;
import com.demo.admissionportal.util.EnumPhone;
import com.demo.admissionportal.util.EnumStaffUsernameValidator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "staff_info")
@Builder
public class StaffInfo {

    @Id
    @Column(name = "staff_id", nullable = false, updatable = false)
    private Integer staffId;

    @Column(name = "admin_id", nullable = false, updatable = false)
    private Integer adminId;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String middleName;

    @Column(nullable = false)
    private String lastName;

    @NotNull(message = "Số điện thoại không thể để trống !")
    @EnumPhone(message = "Số điện thoại phải bắt đầu bằng số 0 và chứa 10-11 chữ số!")
    private String phone;

}
