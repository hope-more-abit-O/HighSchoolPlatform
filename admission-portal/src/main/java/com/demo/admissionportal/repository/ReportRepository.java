package com.demo.admissionportal.repository;

import com.demo.admissionportal.constants.ReportStatus;
import com.demo.admissionportal.constants.ReportType;
import com.demo.admissionportal.dto.entity.report.comment_report.FindAllCommentReportsDTO;
import com.demo.admissionportal.dto.entity.report.post_report.FindAllReportsCompletedDTO;
import com.demo.admissionportal.dto.entity.report.post_report.FindAllReportsWithPostDTO;
import com.demo.admissionportal.entity.Report;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * The interface Report repository.
 */
@Repository
public interface ReportRepository extends JpaRepository<Report, Integer> {

    /**
     * Find all reports with post page.
     *
     * @param pageable   the pageable
     * @param reportId   the report id
     * @param ticketId   the ticket id
     * @param createBy   the create by
     * @param content    the content
     * @param status     the status
     * @param reportType the report type
     * @return the page
     */
    @Query("SELECT new com.demo.admissionportal.dto.entity.report.post_report.FindAllReportsWithPostDTO(" +
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

    /**
     * Find all reports with post by status page.
     *
     * @param pageable   the pageable
     * @param reportId   the report id
     * @param ticketId   the ticket id
     * @param createBy   the create by
     * @param reportType the report type
     * @return the page
     */
    @Query("SELECT new com.demo.admissionportal.dto.entity.report.post_report.FindAllReportsCompletedDTO(" +
            "r.id, r.ticket_id, r.create_by, r.create_time, r.status, r.report_type) " +
            "FROM Report r " +
            "JOIN PostReport pr ON r.id = pr.reportId " +
            "JOIN Post post ON pr.postId = post.id " +
            "WHERE (:reportId IS NULL OR r.id = :reportId) " +
            "AND (:ticketId IS NULL OR r.ticket_id LIKE %:ticketId%) " +
            "AND (:createBy IS NULL OR r.create_by = :createBy) " +
            "AND (:reportType IS NULL OR r.report_type = :reportType) " +
            "AND r.status = 'COMPLETED'")
    Page<FindAllReportsCompletedDTO> findAllReportsWithPostByStatus(Pageable pageable,
                                                                    @Param("reportId") Integer reportId,
                                                                    @Param("ticketId") String ticketId,
                                                                    @Param("createBy") Integer createBy,
                                                                    @Param("reportType") ReportType reportType);

    /**
     * Find all comment reports by status page.
     *
     * @param pageable   the pageable
     * @param reportId   the report id
     * @param ticketId   the ticket id
     * @param reportType the report type
     * @return the page
     */
    @Query("SELECT new com.demo.admissionportal.dto.entity.report.comment_report.FindAllCommentReportsDTO(" +
            "r.id, r.ticket_id, post.title,  r.create_time, r.status, r.report_type, comment.content) " +
            "FROM Report r " +
            "JOIN CommentReport cr ON r.id = cr.reportId " +
            "JOIN Comment comment ON cr.commentId = comment.id " +
            "JOIN Post post ON comment.postId = post.id " +
            "WHERE (:reportId IS NULL OR r.id = :reportId) " +
            "AND (:ticketId IS NULL OR r.ticket_id LIKE %:ticketId%) " +
            "AND (:reportType IS NULL OR r.report_type = :reportType) " +
            "AND r.status = 'COMPLETED'")
    Page<FindAllCommentReportsDTO> findAllCommentReportsByStatus(Pageable pageable,
                                                                 @Param("reportId") Integer reportId,
                                                                 @Param("ticketId") String ticketId,
                                                                 @Param("reportType") ReportType reportType);

    /**
     * Find all comment reports page.
     *
     * @param pageable   the pageable
     * @param reportId   the report id
     * @param ticketId   the ticket id
     * @param createBy   the create by
     * @param content    the content
     * @param status     the status
     * @param reportType the report type
     * @return the page
     */
    @Query("SELECT new com.demo.admissionportal.dto.entity.report.comment_report.FindAllCommentReportsDTO(" +
            "r.id, r.ticket_id, p.title, r.create_time, r.status, r.report_type, cr.commentContent) " +
            "FROM Report r " +
            "JOIN CommentReport cr ON r.id = cr.reportId " +
            "JOIN Comment c ON cr.commentId = c.id " +
            "JOIN Post p ON c.postId = p.id " +
            "WHERE (:reportId IS NULL OR r.id = :reportId) " +
            "AND (:ticketId IS NULL OR r.ticket_id LIKE %:ticketId%) " +
            "AND (:createBy IS NULL OR r.create_by = :createBy) " +
            "AND (:content IS NULL OR r.content LIKE %:content%) " +
            "AND (:reportType IS NULL OR r.report_type = :reportType) " +
            "AND (:status IS NULL OR r.status = :status)")
    Page<FindAllCommentReportsDTO> findAllCommentReports(Pageable pageable,
                                                         @Param("reportId") Integer reportId,
                                                         @Param("ticketId") String ticketId,
                                                         @Param("createBy") Integer createBy,
                                                         @Param("content") String content,
                                                         @Param("status") ReportStatus status,
                                                         @Param("reportType") ReportType reportType);



}
