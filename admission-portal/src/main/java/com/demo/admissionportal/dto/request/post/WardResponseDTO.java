package com.demo.admissionportal.dto.request.post;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * The type Ward response dto.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WardResponseDTO implements Serializable {
    private Integer id;
    private String name;
}
