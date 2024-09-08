package com.demo.admissionportal.entity.sub_entity.id;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserIdentificationNumberId implements Serializable {
    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "identification_number")
    private String identificationNumber;

}
