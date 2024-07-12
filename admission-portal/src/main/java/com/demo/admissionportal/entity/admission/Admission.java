package com.demo.admissionportal.entity.admission;

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

    @Size(max = 4)
    @NotNull
    @Column(name = "\"year\"", nullable = false, length = 4)
    private Integer year;

    @Size(max = 400)
    @Nationalized
    @Column(name = "source", length = 200)
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

    @Size(max = 255)
    @NotNull
    @Nationalized
    @ColumnDefault("'PENDING'")
    @Column(name = "status", nullable = false)
    private String status;

    public Admission(Integer year, String source, Integer universityId, Integer createBy) {
        this.year = year;
        this.source = source;
        this.universityId = universityId;
        this.createBy = createBy;
        this.createTime = new Date();
    }
}