package com.demo.admissionportal.entity.admission;

import com.demo.admissionportal.constants.AdmissionConfirmStatus;
import com.demo.admissionportal.constants.AdmissionScoreStatus;
import com.demo.admissionportal.constants.AdmissionStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
@Table(name = "admission")
public class Admission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @Column(name = "\"year\"", nullable = false, length = 4)
    private Integer year;

    @Size(max = 500)
    @Nationalized
    @Column(name = "note", length = 500)
    private String note;

    @Size(max = 400)
    @Nationalized
    @Column(name = "source", length = 400)
    private String source;

    @NotNull
    @Column(name = "university_id", nullable = false)
    private Integer universityId;

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

    @NotNull
    @Nationalized
    @ColumnDefault("'PENDING'")
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private AdmissionStatus admissionStatus;

    @NotNull
    @Nationalized
    @ColumnDefault("'EMPTY'")
    @Enumerated(EnumType.STRING)
    @Column(name = "score_status", nullable = false)
    private AdmissionScoreStatus scoreStatus;

    @NotNull
    @Nationalized
    @ColumnDefault("'PENDING'")
    @Enumerated(EnumType.STRING)
    @Column(name = "confirm_status", nullable = false)
    private AdmissionConfirmStatus confirmStatus;

    public Admission(Integer year, String source, Integer universityId, Integer createBy) {
        this.year = year;
        this.source = source;
        this.universityId = universityId;
        this.createBy = createBy;
        this.createTime = new Date();
        this.admissionStatus = AdmissionStatus.PENDING;
        this.scoreStatus = AdmissionScoreStatus.EMPTY;
        this.confirmStatus = AdmissionConfirmStatus.PENDING;
    }

    public Admission(Integer year, String source, Integer universityId, Integer createBy, AdmissionStatus admissionStatus) {
        this.year = year;
        this.source = source;
        this.universityId = universityId;
        this.createBy = createBy;
        this.createTime = new Date();
        this.admissionStatus = admissionStatus;
        this.scoreStatus = AdmissionScoreStatus.EMPTY;
        this.confirmStatus = AdmissionConfirmStatus.UPDATE_PENDING;
    }

    public void modifyOldAdmissionWhenStaffConfirmUpdateAdmission(Integer staffId){
        this.admissionStatus = AdmissionStatus.STAFF_INACTIVE;
        this.confirmStatus = AdmissionConfirmStatus.STAFF_UPDATED;
        this.updateBy = staffId;
        this.updateTime = new Date();
    }

    public void modifyNewAdmissionWhenStaffConfirmUpdateAdmission(Integer staffId, AdmissionStatus status, AdmissionConfirmStatus confirmStatus){
        this.admissionStatus = status;
        this.confirmStatus = confirmStatus;
        this.updateBy = staffId;
        this.updateTime = new Date();
    }

    public void modifyNewAdmissionWhenStaffConfirmUpdateAdmission(Integer staffId){
        this.admissionStatus = AdmissionStatus.INACTIVE;
        this.confirmStatus = AdmissionConfirmStatus.CONFIRMED;
        this.updateBy = staffId;
        this.updateTime = new Date();
    }

    public void staffReject(Integer staffId){
        this.confirmStatus = AdmissionConfirmStatus.REJECTED;
        this.admissionStatus = AdmissionStatus.STAFF_INACTIVE;
        this.updateBy = staffId;
        this.updateTime = new Date();
    }

    public void universityInactive(Integer universityId){
        this.admissionStatus = AdmissionStatus.INACTIVE;
        this.updateBy = universityId;
        this.updateTime = new Date();
    }

    public void universityUpdateCancel(Integer universityId){
        this.admissionStatus = AdmissionStatus.UPDATE_CANCEL;
        this.confirmStatus = AdmissionConfirmStatus.UPDATE_CANCEL;
        this.updateBy = universityId;
        this.updateTime = new Date();
    }

    public void universityUpdatePending(Integer universityId){
        this.admissionStatus = AdmissionStatus.PENDING;
        this.confirmStatus = AdmissionConfirmStatus.UPDATE_PENDING;
        this.updateBy = universityId;
        this.updateTime = new Date();
    }
}