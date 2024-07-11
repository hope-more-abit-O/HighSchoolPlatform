package com.demo.admissionportal.repository;

import com.demo.admissionportal.entity.Major;
import com.demo.admissionportal.entity.Method;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface MethodRepository extends JpaRepository<Method, Integer> {
    Optional<Method> findByNameLike(String name);

    Optional<Method> findFirstByName(String methodName);

    Optional<Method> findFirstByNameAndCode(String methodName, String methodCode);

    Optional<Method> findFirstByNameOrCode(String methodName, String methodCode);

    List<Method> findByNameInOrCodeIn(Set<String> methodNames, Set<String> methodCodes);

    List<Method> findByIdIn(List<Integer> ids);
}
