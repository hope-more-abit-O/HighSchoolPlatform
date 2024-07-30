package com.demo.admissionportal.dto.request.major;

import com.demo.admissionportal.constants.MajorStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class UpdateMajorStatusRequest {
    private Integer majorId;
    private MajorStatus majorStatus;
    private String note;
}
