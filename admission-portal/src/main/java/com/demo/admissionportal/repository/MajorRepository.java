package com.demo.admissionportal.repository;

import com.demo.admissionportal.entity.Major;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface MajorRepository extends JpaRepository<Major, Integer> {
    Optional<Major> findByName(String methodName);

    List<Major> findByNameInOrCodeIn(Set<String> majorNames, Set<String> majorCodes);

    List<Major> findByIdIn(Collection<Integer> ids);

    List<Major> findByNameIn(Set<String> majorsNames);

    List<Major> findByCodeIn(Set<String> majorCodes);
}
