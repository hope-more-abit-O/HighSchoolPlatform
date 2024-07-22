package com.demo.admissionportal.dto.entity.post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * The type University post response dto.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UniversityPostResponseDTO implements Serializable {
    private Integer id;
    private String name;
    private String location;
}
