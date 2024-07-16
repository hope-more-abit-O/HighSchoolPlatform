package com.demo.admissionportal.dto.response.post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * The type Type response dto.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TypeResponseDTO implements Serializable {
    private String name;
}