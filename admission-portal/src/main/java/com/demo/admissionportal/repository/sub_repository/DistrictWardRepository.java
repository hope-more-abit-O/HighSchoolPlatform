package com.demo.admissionportal.repository.sub_repository;

import com.demo.admissionportal.entity.sub_entity.DistrictWard;
import com.demo.admissionportal.entity.sub_entity.id.DistrictWardId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * The interface District ward repository.
 */
@Repository
public interface DistrictWardRepository extends JpaRepository<DistrictWard, DistrictWardId> {
    /**
     * Find by district id list.
     *
     * @param districtId the district id
     * @return the list
     */
    List<DistrictWard> findByDistrictId(Integer districtId);

}
