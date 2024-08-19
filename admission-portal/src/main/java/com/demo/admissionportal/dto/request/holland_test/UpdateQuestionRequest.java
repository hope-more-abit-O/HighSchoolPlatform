package com.demo.admissionportal.dto.request.holland_test;

import com.demo.admissionportal.constants.QuestionStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateQuestionRequest implements Serializable {
    private String content;
    private Integer type;
    private List<Integer> jobIds;
    @Enumerated(EnumType.STRING)
    private QuestionStatus status;
}
