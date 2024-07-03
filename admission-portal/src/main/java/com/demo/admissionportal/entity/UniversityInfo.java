package com.demo.admissionportal.entity;


import com.demo.admissionportal.constants.UniversityType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "university_info")
@Builder
public class UniversityInfo{

    @Id
    @Column(name = "university_id", nullable = false, updatable = false)
    private Integer universityId;

    @NotNull
    @Column
    private Integer createUniversityRequestId;

    @NotNull
    @Column(nullable = false)
    private String name;

    @NotNull
    @Column(nullable = false)
    private String code;

    @Column(nullable = false, columnDefinition = "varchar(max)")
    private String description;

    @Column(length = 100)
    private String coverImage;

    @NotNull
    @Column
    @Enumerated(EnumType.STRING)
    private UniversityType type;

    public UniversityInfo(Integer universityId, Integer createUniversityRequestId, String name, UniversityType type) {
        this.universityId = universityId;
        this.createUniversityRequestId = createUniversityRequestId;
        this.name = name;
        this.description = "";
        this.type = type;
        this.coverImage = "cover.png";
    }
    public UniversityInfo(Integer universityId, CreateUniversityRequest createUniversityRequest) {
        this.universityId = universityId;
        this.createUniversityRequestId = createUniversityRequest.getId();
        this.name = createUniversityRequest.getUniversityName();
        this.code = createUniversityRequest.getUniversityCode();
        this.description = "";
        this.type = createUniversityRequest.getUniversityType();
        this.coverImage = "cover.png";
    }
}
