package com.demo.admissionportal.dto.request.university_major;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUniversityMajorStatusRequest {
    private List<Integer> inactiveMajorIds;
    private List<Integer> activeMajorIds;
}
