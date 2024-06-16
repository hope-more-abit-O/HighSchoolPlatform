package com.demo.admissionportal.dto.response.entity.sub_entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO representing the response for staff-university association data.
 *
 * @author hopeless
 * @version 1.0
 * @since 13/06/2024
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class StaffUniversityResponseDTO {

    /**
     * The ID of the staff member.
     */
    private Integer staffId;

    /**
     * The ID of the university.
     */
    private Integer universityId;

    /**
     * The time when the association was created, formatted as dd/MM/yyyy.
     */
    private String createTime;
}