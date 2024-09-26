package com.demo.admissionportal.entity.admission;


import com.demo.admissionportal.constants.AdmissionUpdateStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;

import java.util.Date;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "admission_update")
public class AdmissionUpdate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "before_admission_id")
    private Integer beforeAdmissionId;

    @Column(name = "after_admission_id")
    private Integer afterAdmissionId;

    @NotNull
    @Column(name = "create_time", nullable = false)
    private Date createTime;

    @Column(name = "create_by")
    private Integer createBy;

    @ColumnDefault("NULL")
    @Column(name = "update_by")
    private Integer updateBy;

    @Column(name = "update_time")
    private Date updateTime;

    @Column(name = "note")
    private String note;

    @NotNull
    @Nationalized
    @ColumnDefault("'PENDING'")
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private AdmissionUpdateStatus status;

    public AdmissionUpdate(Integer beforeAdmissionId, Integer afterAdmissionId, Integer createBy){
        this.beforeAdmissionId = beforeAdmissionId;
        this.afterAdmissionId = afterAdmissionId;
        this.createTime = new Date();
        this.createBy = createBy;
        this.status = AdmissionUpdateStatus.PENDING;
    }

    public void updateStatus(AdmissionUpdateStatus status, String note, Integer updateBy){
        this.status = status;
        this.updateBy = updateBy;
        this.note = note;
        this.updateTime = new Date();
    }

    public void setExpired(Integer updateBy){
        this.status = AdmissionUpdateStatus.EXPIRED;
        this.updateBy = updateBy;
        this.updateTime = new Date();
    }
}
