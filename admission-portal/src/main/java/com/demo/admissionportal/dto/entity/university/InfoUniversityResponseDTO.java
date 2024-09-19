package com.demo.admissionportal.dto.entity.university;

import com.demo.admissionportal.constants.AccountStatus;
import com.demo.admissionportal.constants.UniversityType;
import com.demo.admissionportal.dto.entity.user.InfoUserResponseDTO;
import com.demo.admissionportal.entity.*;
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
    private String specificAddress;
    private String provinceName;
    private Integer provinceId;
    private String districtName;
    private Integer districtId;
    private String wardName;
    private Integer wardId;

    public static InfoUniversityResponseDTO fromEntity(UniversityInfo universityInfo, Province province) {
        return InfoUniversityResponseDTO.builder()
                .id(universityInfo.getId())
                .name(universityInfo.getName())
                .code(universityInfo.getCode())
                .type(universityInfo.getType().name())
                .description(universityInfo.getDescription())
                .coverImage(universityInfo.getCoverImage())
                .provinceName(province.getName())
                .provinceId(province.getId())
                .build();
    }

    public static InfoUniversityResponseDTO fromEntity(UniversityInfo universityInfo, Province province, District district, Ward ward, UniversityCampus universityCampus) {
        return InfoUniversityResponseDTO.builder()
                .id(universityInfo.getId())
                .name(universityInfo.getName())
                .code(universityInfo.getCode())
                .type(universityInfo.getType().name())
                .description(universityInfo.getDescription())
                .coverImage(universityInfo.getCoverImage())
                .specificAddress(universityCampus.getSpecificAddress())
                .provinceName(province.getName())
                .provinceId(province.getId())
                .districtId(district.getId())
                .districtName(district.getName())
                .wardId(ward.getId())
                .wardName(ward.getName())
                .build();
    }
}
