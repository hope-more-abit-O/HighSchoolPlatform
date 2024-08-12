package com.demo.admissionportal.dto.request.payment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CreateQrRequestDTO implements Serializable {
    private Integer postId;
    private Integer packageId;
}
