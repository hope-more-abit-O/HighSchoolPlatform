package com.demo.admissionportal.dto.response.like;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class TotalLikeResponseDTO implements Serializable {
    private String currentStatus;
    private Integer total;
}
