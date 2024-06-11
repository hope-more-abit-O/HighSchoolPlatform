package com.demo.admissionportal.repository;

import com.demo.admissionportal.entity.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Integer> {
    Staff findByName(String name);
    Staff findByEmailOrUsername(String email, String username);
}
