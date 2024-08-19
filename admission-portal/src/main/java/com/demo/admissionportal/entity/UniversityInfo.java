package com.demo.admissionportal.entity;


import com.demo.admissionportal.constants.UniversityType;
import com.demo.admissionportal.dto.request.create_univeristy_request.CreateUniversityRequestRequest;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import javax.annotation.Nullable;

/**
 * Represents detailed information about a university.
 *
 * <p> This entity stores information specific to universities, including their name,
 * code, type, and other relevant details.
 *
 * <p> Example Usage:
 * <pre>
 * {@code
 * UniversityInfo universityInfo = UniversityInfo.builder()
 *     .universityId(1)
 *     .createUniversityRequestId(101)
 *     .name("Example University")
 *     .code("EU")
 *     .description("A prestigious institution of higher learning.")
 *     .coverImage("image.jpg")
 *     .type(UniversityType.PUBLIC)
 *     .build();
 *
 * // ... Use a repository to persist the universityInfo object ...
 * }
 * </pre>
 *
 */
@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "university_info")
public class UniversityInfo{

    @Id
    @Column(name = "university_id", nullable = false, updatable = false)
    private Integer id;

    @Nullable
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

    @Column(name = "staff_id")
    private Integer staffId;

    @NotNull
    @Column
    @Enumerated(EnumType.STRING)
    private UniversityType type;

    public UniversityInfo(Integer id, Integer createUniversityRequestId, String name, UniversityType type) {
        this.id = id;
        this.createUniversityRequestId = createUniversityRequestId;
        this.name = name;
        this.description = "";
        this.type = type;
        this.coverImage = "cover.png";
    }
    public UniversityInfo(Integer id, CreateUniversityRequest createUniversityRequest) {
        this.id = id;
        this.createUniversityRequestId = createUniversityRequest.getId();
        this.name = createUniversityRequest.getUniversityName();
        this.code = createUniversityRequest.getUniversityCode();
        this.description = "";
        this.type = createUniversityRequest.getUniversityType();
        this.coverImage = "https://firebasestorage.googleapis.com/v0/b/highschoolvn-dev.appspot.com/o/uploads%2Funiversity.png?alt=media&token=d80d250a-c6c5-406b-9314-cbf9b7da9c2d";
        this.staffId = createUniversityRequest.getCreateBy();
    }
    public UniversityInfo(Integer id, CreateUniversityRequestRequest createUniversityRequest) {
        this.id = id;
        this.createUniversityRequestId = null;
        this.name = createUniversityRequest.getUniversityName();
        this.code = createUniversityRequest.getUniversityCode();
        this.description = "";
        this.type = createUniversityRequest.getUniversityType();
        this.coverImage = "https://firebasestorage.googleapis.com/v0/b/highschoolvn-dev.appspot.com/o/uploads%2Funiversity.png?alt=media&token=d80d250a-c6c5-406b-9314-cbf9b7da9c2d";
    }
}
