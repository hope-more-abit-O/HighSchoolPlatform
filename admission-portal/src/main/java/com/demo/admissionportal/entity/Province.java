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

/**
 * Represents a Province entity, mapped to the "province" table in the database.
 *
 * <p>This entity holds essential information about provinces, including:
 * <ul>
 *    <li><b>id:</b> The unique identifier of the province. </li>
 *    <li><b>name:</b> The name of the province, which must not be null
 *     and may support internationalization (indicated by the `@Nationalized` annotation). </li>
 * </ul>
 *
 * <p> Example Usage:
 * <pre>
 * {@code
 * // Creating a new Province instance:
 * Province province = new Province();
 * province.setId(1);
 * province.setName("California"); // Set the name of the province
 *
 * // ... you can now use a repository to save this province to the database.
 * }
 * </pre>
 *
 */
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "province")
public class Province {
    @Id
    @Column(name = "id")
    private Integer id;

    @NotNull
    @Nationalized
    @Column(name = "name")
    private String name;

}