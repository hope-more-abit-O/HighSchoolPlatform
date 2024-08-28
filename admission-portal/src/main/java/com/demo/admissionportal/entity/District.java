package com.demo.admissionportal.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Nationalized;

import java.io.Serializable;

/**
 * Represents a District entity, mapped to the "district" table in the database.
 *
 * <p>This entity holds information about districts, including:
 * <ul>
 *     <li> **id:** The unique identifier for the district. </li>
 *     <li> **name:** The name of the district, which must not be null and may be subject to internationalization (based on the `@Nationalized` annotation).</li>
 * </ul>
 *
 * <p> Example Usage:
 * <pre>
 * {@code
 * District district = new District();
 * district.setId(1);
 * district.setName("Example District");
 *
 * // ... Further operations, such as persisting the district object using a repository.
 * }
 * </pre>
 *
 */
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "district")
public class District implements Serializable {
    @Id
    @Column(name = "id")
    private Integer id;

    @NotNull
    @Nationalized
    @Column(name = "name")
    private String name;

    public District(Integer id) {
        this.id = id;
    }
}