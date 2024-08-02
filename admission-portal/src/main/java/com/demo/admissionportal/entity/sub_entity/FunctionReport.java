package com.demo.admissionportal.entity.sub_entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "function_report")
public class FunctionReport {
    @Id
    @Column(name = "report_id")
    private Integer reportId;

    @Column(name = "content")
    private String content;

    @Column(name = "proofs")
    private String proofs;

    @Column(name = "report_action")
    private String reportAction;
}
