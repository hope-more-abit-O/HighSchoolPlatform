package com.demo.admissionportal.entity;

import com.demo.admissionportal.entity.sub_entity.id.TestResponseAnswerId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@IdClass(TestResponseAnswerId.class)
@Table(name = "test_response_answer")
public class TestResponseAnswer {
    @Id
    @Column(name = "question_id")
    private Integer questionId;

    @Id
    @Column(name = "test_response_id")
    private Integer testResponseId;
}
