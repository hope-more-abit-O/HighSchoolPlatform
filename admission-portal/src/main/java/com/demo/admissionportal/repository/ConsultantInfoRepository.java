package com.demo.admissionportal.repository;

import com.demo.admissionportal.entity.ConsultantInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * The interface Consultant info repository.
 */
@Repository
public interface ConsultantInfoRepository extends JpaRepository<ConsultantInfo, Integer> {
    /**
     * Find first by phone optional.
     *
     * @param phone the phone
     * @return the optional
     */
    Optional<ConsultantInfo> findFirstByPhone(String phone);

    /**
     * Find consultant info by id consultant info.
     *
     * @param id the id
     * @return the consultant info
     */
    ConsultantInfo findConsultantInfoById(Integer id);

    List<ConsultantInfo> findAllConsultantInfosById(Integer id);
}
