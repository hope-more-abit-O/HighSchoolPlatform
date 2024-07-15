package com.demo.admissionportal.entity;

import com.demo.admissionportal.constants.MajorStatus;
import com.demo.admissionportal.dto.entity.major.CreateMajorDTO;
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
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "major")
public class Major {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 20)
    @NotNull
    @Column(name = "code", nullable = false, length = 20)
    private String code;

    @Size(max = 300)
    @NotNull
    @Nationalized
    @Column(name = "name", nullable = false, length = 300)
    private String name;

    @Size(max = 300)
    @Nationalized
    @Column(name = "note", length = 300)
    private String note;

    @NotNull
    @Column(name = "create_time", nullable = false)
    private Date createTime;

    @ColumnDefault("0")
    @Column(name = "create_by")
    private Integer createBy;

    @Column(name = "update_time")
    private Date updateTime;

    @ColumnDefault("NULL")
    @Column(name = "update_by")
    private Integer updateBy;

    @NotNull
    @Nationalized
    @ColumnDefault("'ACTIVE'")
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private MajorStatus status;

    public Major(CreateMajorDTO majorDTO) {
        this.name = name;
        this.code = majorDTO.getCode();
        this.createTime = new Date();
    }

    public Major(String code, String name, Integer createBy) {
        this.code = code;
        this.name = name;
        this.createBy = createBy;
        this.createTime = new Date();
        this.status = MajorStatus.ACTIVE;
    }
}