package com.demo.admissionportal.dto.request;


import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UpdateSubjectGroupRequestDTO {
    @Size(max = 3, message = "Tên nhóm môn học không được vượt quá 3 ký tự")
    private String name;
    private List<Integer> subjectIds;
    private String status;
    public boolean isEmpty() {
        return (name == null && name.isEmpty() || subjectIds == null && subjectIds.isEmpty());
    }
}
