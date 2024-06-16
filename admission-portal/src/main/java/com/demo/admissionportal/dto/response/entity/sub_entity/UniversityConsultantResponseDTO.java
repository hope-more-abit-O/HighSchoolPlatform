package com.demo.admissionportal.dto.response.entity.sub_entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO representing the response for university-consultant association data.
 *
 * @author hopeless
 * @version 1.0
 * @since 16/06/2024
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UniversityConsultantResponseDTO {

    /**
     * The ID of the university.
     */
    private Integer universityId;

    /**
     * The ID of the consultant.
     */
    private Integer consultantId;

    /**
     * The time when the association was created, formatted as dd/MM/yyyy.
     */
    private String createTime;
}