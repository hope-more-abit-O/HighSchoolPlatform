package com.demo.admissionportal.dto.entity.university;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) used for transferring comprehensive university information responses.
 *
 * @Field name The name of the university.
 * @Field code The university's code (optional).
 * @Field type The type of university (optional).
 * @Field description A description of the university (optional).
 * @Field coverImage A URL or path to the university's cover image (optional).
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FullUniversityResponseDTO {
    private String name;
    private String code;
    private String type;
    private Integer createUniversityRequestId;
    private String description;
    private String coverImage;
}
