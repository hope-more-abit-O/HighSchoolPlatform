package com.demo.admissionportal.repository;

import com.demo.admissionportal.entity.UserIdentificationNumberRegister;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserIdentificationNumberRegisterRepository extends JpaRepository<UserIdentificationNumberRegister, Integer> {
    List<UserIdentificationNumberRegister> findByUserId(Integer userId);
}
