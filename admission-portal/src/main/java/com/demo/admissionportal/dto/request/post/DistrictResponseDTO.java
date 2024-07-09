package com.demo.admissionportal.dto.request.post;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * The type District response dto.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DistrictResponseDTO implements Serializable {
    private Integer id;
    private String name;
}
