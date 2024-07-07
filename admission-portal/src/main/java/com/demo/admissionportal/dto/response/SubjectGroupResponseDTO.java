package com.demo.admissionportal.dto.response;

import com.demo.admissionportal.dto.response.sub_entity.SubjectResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class SubjectGroupResponseDTO {
    private Integer id;
    private String name;
    private String status;
    private List<SubjectResponseDTO> subjects;

    // Ensure the constructor initializes the fields correctly
    public SubjectGroupResponseDTO(Integer id, String name, String status, List<SubjectResponseDTO> subjects) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.subjects = subjects;
    }
}
