package com.demo.admissionportal.dto.entity;

import com.demo.admissionportal.entity.Subject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SubjectDTO {
    private Integer subjectId;
    private String subjectName;

    public SubjectDTO(Subject subject) {
        this.subjectId = subject.getId();
        this.subjectName = subject.getName();
    }
}
