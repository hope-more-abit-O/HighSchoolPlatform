package com.demo.admissionportal.dto.response.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO representing the response for consultant data.
 *
 * @author hopeless
 * @version 1.0
 * @since 16/06/2024
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ConsultantResponseDTO {

    /**
     * The ID of the consultant.
     */
    private Integer id;

    /**
     * The username of the consultant.
     */
    private String username;

    /**
     * The name of the consultant.
     */
    private String name;

    /**
     * The email address of the consultant.
     */
    private String email;

    /**
     * The avatar (profile picture) of the consultant.
     */
    private String avatar;

    /**
     * The phone number of the consultant.
     */
    private String phone;

    /**
     * The status of the consultant.
     */
    private String status;
}