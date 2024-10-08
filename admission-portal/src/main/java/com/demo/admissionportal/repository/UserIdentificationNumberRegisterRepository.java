package com.demo.admissionportal.repository;

import com.demo.admissionportal.entity.UserIdentificationNumberRegister;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserIdentificationNumberRegisterRepository extends JpaRepository<UserIdentificationNumberRegister, Integer> {
    List<UserIdentificationNumberRegister> findByIdUserId(Integer userId);
    boolean existsByIdIdentificationNumber(String identificationNumber);
    List<UserIdentificationNumberRegister> findByIdIdentificationNumber(String identificationNumber);
    List<UserIdentificationNumberRegister> findByIdUserIdAndIdIdentificationNumber(Integer userId, String identificationNumber);
}
