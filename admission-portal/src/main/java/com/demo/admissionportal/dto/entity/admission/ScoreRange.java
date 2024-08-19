package com.demo.admissionportal.dto.entity.admission;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScoreRange {
    private Integer admissionMethodId;
    private Float minimum;
    private Float maximum;
}
