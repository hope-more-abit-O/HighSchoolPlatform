package com.demo.admissionportal.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * The type Test response.
 */
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "test_response")
public class TestResponse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "questionnaire_id")
    private Integer questionnaireId;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "user_Id")
    private Integer userId;
}
