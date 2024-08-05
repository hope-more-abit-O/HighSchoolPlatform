package com.demo.admissionportal.dto.entity.subject;

import com.demo.admissionportal.entity.Subject;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class InfoSubjectDTO {
    private Integer id;
    private String name;

    public InfoSubjectDTO(Subject subject) {
        this.id = subject.getId();
        this.name = subject.getName();
    }
}
