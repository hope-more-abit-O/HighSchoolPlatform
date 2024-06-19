package com.demo.admissionportal.entity.address;

import com.google.firebase.database.annotations.NotNull;
import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "address")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @Column(name = "address_detail_id", nullable = false)
    private Integer addressDetailId; // Assuming a foreign key relationship

    @Column(name = "specific_address")
    private String specificAddress;

    public Address(Integer addressDetailId, String specificAddress) {
        this.addressDetailId = addressDetailId;
        this.specificAddress = specificAddress;
    }
}