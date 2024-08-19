package com.demo.admissionportal.entity.sub_entity.id;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class UniversityMajorId implements Serializable {
    @Serial
    private static final long serialVersionUID = 6053930313302589781L;

    @NotNull
    @Column(name = "university_id", nullable = false)
    private Integer universityId;

    @NotNull
    @Column(name = "major_id", nullable = false)
    private Integer majorId;
}
