package com.demo.admissionportal.dto.response.authen;

import com.demo.admissionportal.constants.UniversityType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * The type University response dto.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UniversityInfoResponseDTO implements Serializable {
    private Integer createUniversityRequestId;
    private String name;
    private String code;
    private String description;
    private String coverImage;
    private UniversityType type;
}
