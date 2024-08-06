package com.demo.admissionportal.dto.response.favorite;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * The type Total count response dto.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TotalCountResponseDTO implements Serializable {
    private Integer totalCount;
}
