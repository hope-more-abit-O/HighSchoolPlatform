package com.demo.admissionportal.dto.request.method;

import com.demo.admissionportal.constants.MethodStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateMethodStatusRequest {
    private Integer id;
    private String note;
    private MethodStatus status;
}
