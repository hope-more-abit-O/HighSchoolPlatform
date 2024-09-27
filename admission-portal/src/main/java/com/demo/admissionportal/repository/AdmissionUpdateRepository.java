package com.demo.admissionportal.repository;

import com.demo.admissionportal.constants.AdmissionUpdateStatus;
import com.demo.admissionportal.entity.admission.AdmissionUpdate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface AdmissionUpdateRepository extends JpaRepository<AdmissionUpdate, Integer> {
    List<AdmissionUpdate> findByBeforeAdmissionIdAndStatus(Integer beforeAdmissionId, AdmissionUpdateStatus status);

    Optional<AdmissionUpdate> findFirstByBeforeAdmissionIdAndStatus(Integer beforeAdmissionId, AdmissionUpdateStatus status);

    List<AdmissionUpdate> findByBeforeAdmissionId(Integer beforeAdmissionId);

    @Query(value = """
SELECT TOP(1) *
FROM admission_update
WHERE before_admission_id = :beforeAdmissionId
  AND (status = :status
  OR status = :status2)
ORDER BY id DESC
""", nativeQuery = true)
    Optional<AdmissionUpdate> findFirstByBeforeAdmissionIdAndStatus(Integer beforeAdmissionId, String status, String status2);

//    Page<AdmissionUpdate> findAll(Pageable pageable, Integer id, Integer beforeAdmissionId, Date createTime, Integer createBy, Integer updateBy, Date updateTime, List<AdmissionUpdateStatus> status);

    @Query(value = """
select au.*
from admission_update au
inner join admission a on a.id = au.before_admission_id
where (:id is null or au.id = :id)
and (:beforeAdmissionId is null or au.before_admission_id = :beforeAdmissionId)
and (:createTime is null or au.create_time >= :createTime)
and (:createBy is null or au.create_by = :createBy)
and au.status in :statuses
and (:universityId is null or a.university_id = :universityId)
""", countQuery = """
select count(au.id)
from admission_update au
inner join admission a on a.id = au.before_admission_id
where (:id is null or au.id = :id)
and (:beforeAdmissionId is null or au.before_admission_id = :beforeAdmissionId)
and (:createTime is null or au.create_time >= :createTime)
and (:createBy is null or au.create_by = :createBy)
and au.status in :statuses
and (:universityId is null or a.university_id = :universityId)
""",nativeQuery = true)
    Page<AdmissionUpdate> findAllBy(Pageable pageable, Integer id, Integer beforeAdmissionId, Date createTime, Integer createBy, Integer universityId, List<String> statuses);
    @Query(value = """
select au.*
from admission_update au
inner join admission a on a.id = au.before_admission_id
where (:id is null or au.id = :id)
and (:beforeAdmissionId is null or au.before_admission_id = :beforeAdmissionId)
and (:createTime is null or au.create_time >= :createTime)
and (:createBy is null or au.create_by = :createBy)
and (:universityId is null or a.university_id = :universityId)
""", countQuery = """
select count(au.id)
from admission_update au
inner join admission a on a.id = au.before_admission_id
where (:id is null or au.id = :id)
and (:beforeAdmissionId is null or au.before_admission_id = :beforeAdmissionId)
and (:createTime is null or au.create_time >= :createTime)
and (:createBy is null or au.create_by = :createBy)
and (:universityId is null or a.university_id = :universityId)
""",nativeQuery = true)
    Page<AdmissionUpdate> findAllBy(Pageable pageable, Integer id, Integer beforeAdmissionId, Date createTime, Integer createBy, Integer universityId);

    Optional<AdmissionUpdate> findFirstByAfterAdmissionIdAndStatusOrderByCreateTimeAsc(Integer afterAdmissionId, AdmissionUpdateStatus status);
}
