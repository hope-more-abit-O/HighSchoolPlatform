package com.demo.admissionportal.repository;

import com.demo.admissionportal.entity.address.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Integer> {
}