package com.demo.admissionportal.entity;

import com.demo.admissionportal.constants.MethodStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "method")
public class Method {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 20)
    @NotBlank
    @Column(name = "code", nullable = false, length = 20)
    private String code;

    @Size(max = 300)
    @NotBlank
    @Nationalized
    @Column(name = "name", nullable = false, length = 300)
    private String name;

    @NotBlank
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

    @NotBlank
    @Nationalized
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private MethodStatus status;

    public Method(String code, String name, Integer createBy) {
        this.id = null;
        this.code = code;
        this.name = name;
        this.createBy = createBy;
        this.createTime = new Date();
        this.status = MethodStatus.ACTIVE;
    }

    public void update(String methodName, String methodCode, String methodNote, Integer updaterId) {
        if (methodName != null && methodName.isEmpty())
            this.name = methodName;
        if (methodCode != null && methodCode.isEmpty())
            this.code = methodCode;
        if (methodNote != null && methodNote.isEmpty()) {}
        this.updateTime = new Date();
        this.updateBy = updaterId;
    }

    public void updateStatus(MethodStatus status, String note, Integer updaterId) {
        this.status = status;
//        this.note = note;
        this.updateTime = new Date();
        this.updateBy = updaterId;
    }
}