package com.demo.admissionportal.repository;

import com.demo.admissionportal.constants.ReportStatus;
import com.demo.admissionportal.dto.response.report.post_report.FindAllReportsWithPostResponseDTO;
import com.demo.admissionportal.entity.Report;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface ReportRepository extends JpaRepository<Report, Integer> {

    @Query("SELECT new com.demo.admissionportal.dto.response.report.post_report.FindAllReportsWithPostResponseDTO(" +
            "r.id, r.ticket_id, r.create_by, r.create_time, r.content, r.status, post.url) " +
            "FROM Report r " +
            "JOIN PostReport pr ON r.id = pr.reportId " +
            "JOIN Post post ON pr.postId = post.id " +
            "WHERE (:reportId IS NULL OR r.id = :reportId) " +
            "AND (:ticketId IS NULL OR r.ticket_id LIKE %:ticketId%) " +
            "AND (:createBy IS NULL OR r.create_by = :createBy) " +
            "AND (:content IS NULL OR r.content LIKE %:content%) " +
            "AND (:status IS NULL OR r.status = :status)")
    Page<FindAllReportsWithPostResponseDTO> findAllReportsWithPost(@Param("reportId") Integer reportId,
                                                                   @Param("ticketId") String ticketId,
                                                                   @Param("createBy") Integer createBy,
                                                                   @Param("content") String content,
                                                                   @Param("status") ReportStatus status,
                                                                   Pageable pageable);
}
