package com.demo.admissionportal.entity;

import com.demo.admissionportal.constants.ReportStatus;
import com.demo.admissionportal.constants.ReportType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "report")
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "ticket_id")
    private String ticket_id;
    @Column(name = "create_by")
    private Integer create_by;
    @Column(name = "create_time")
    private Date create_time;
    @Column(name = "update_time")
    private Date update_time;
    @Column(name = "update_by")
    private Integer update_by;
    @Column(name = "complete_time")
    private Date complete_time;
    @Column(name = "complete_by")
    private Integer complete_by;
    @Column(name = "content")
    private String content;
    @Column(name = "response")
    private String response;
    @Enumerated(value = EnumType.STRING)
    @Column(name = "report_type")
    private ReportType report_type;
    @Enumerated(value = EnumType.STRING)
    @Column(name = "status")
    private ReportStatus status;
}
