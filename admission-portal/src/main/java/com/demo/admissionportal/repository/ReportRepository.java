package com.demo.admissionportal.repository;

import com.demo.admissionportal.constants.ReportStatus;
import com.demo.admissionportal.constants.ReportType;
import com.demo.admissionportal.dto.entity.report.FindAllReportsCompletedDTO;
import com.demo.admissionportal.dto.entity.report.FindAllReportsWithPostDTO;
import com.demo.admissionportal.entity.Report;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface ReportRepository extends JpaRepository<Report, Integer> {

    @Query("SELECT new com.demo.admissionportal.dto.entity.report.FindAllReportsWithPostDTO(" +
            "r.id, r.ticket_id, r.create_by, r.create_time, r.content, r.status, r.report_type, post.url) " +
            "FROM Report r " +
            "JOIN PostReport pr ON r.id = pr.reportId " +
            "JOIN Post post ON pr.postId = post.id " +
            "WHERE (:reportId IS NULL OR r.id = :reportId) " +
            "AND (:ticketId IS NULL OR r.ticket_id LIKE %:ticketId%) " +
            "AND (:createBy IS NULL OR r.create_by = :createBy) " +
            "AND (:content IS NULL OR r.content LIKE %:content%) " +
            "AND (:reportType IS NULL OR r.report_type = :reportType) " +
            "AND (:status IS NULL OR r.status = :status)")
    Page<FindAllReportsWithPostDTO> findAllReportsWithPost(Pageable pageable,
                                                           @Param("reportId") Integer reportId,
                                                           @Param("ticketId") String ticketId,
                                                           @Param("createBy") Integer createBy,
                                                           @Param("content") String content,
                                                           @Param("status") ReportStatus status,
                                                           @Param("reportType") ReportType reportType);

    @Query("SELECT new com.demo.admissionportal.dto.entity.report.FindAllReportsCompletedDTO(" +
            "r.id, r.ticket_id, r.create_by, r.create_time, r.status, r.report_type) " +
            "FROM Report r " +
            "JOIN PostReport pr ON r.id = pr.reportId " +
            "JOIN Post post ON pr.postId = post.id " +
            "WHERE (:reportId IS NULL OR r.id = :reportId) " +
            "AND (:ticketId IS NULL OR r.ticket_id LIKE %:ticketId%) " +
            "AND (:createBy IS NULL OR r.create_by = :createBy) " +
            "AND (:reportType IS NULL OR r.report_type = :reportType) " +
            "AND (:status IS NULL OR r.status = :status)")
    Page<FindAllReportsCompletedDTO> findAllReportsWithPostByStatus(
            Pageable pageable,
            @Param("reportId") Integer reportId,
            @Param("ticketId") String ticketId,
            @Param("createBy") Integer createBy,
            @Param("status") ReportStatus status,
            @Param("reportType") ReportType reportType
            );

}
