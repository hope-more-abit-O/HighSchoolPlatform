package com.demo.admissionportal.dto.entity.university;

import com.demo.admissionportal.constants.AccountStatus;
import com.demo.admissionportal.constants.UniversityType;
import com.demo.admissionportal.dto.entity.user.InfoUserResponseDTO;
import com.demo.admissionportal.entity.UniversityInfo;
import com.demo.admissionportal.entity.User;
import lombok.*;

/**
 * Data Transfer Object (DTO) used for transferring basic university information responses.
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
public class InfoUniversityResponseDTO {
    private Integer id;
    private String name;
    private String code;
    private String type;
    private String description;
    private String coverImage;
}
