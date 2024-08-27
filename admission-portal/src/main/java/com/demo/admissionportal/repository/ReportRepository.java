package com.demo.admissionportal.repository;

import com.demo.admissionportal.constants.ReportStatus;
import com.demo.admissionportal.constants.ReportType;
import com.demo.admissionportal.dto.entity.report.comment_report.FindAllCommentReportsByStatusDTO;
import com.demo.admissionportal.dto.entity.report.comment_report.FindAllCommentReportsDTO;
import com.demo.admissionportal.dto.entity.report.function_report.FindAllFunctionReportByStatusDTO;
import com.demo.admissionportal.dto.entity.report.function_report.FindAllFuntionReportDTO;
import com.demo.admissionportal.dto.entity.report.post_report.FindAllReportsCompletedDTO;
import com.demo.admissionportal.dto.entity.report.post_report.FindAllReportsWithPostDTO;
import com.demo.admissionportal.entity.Report;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * The interface Report repository.
 */
@Repository
public interface ReportRepository extends JpaRepository<Report, Integer> {

    @Query("SELECT new com.demo.admissionportal.dto.entity.report.post_report.FindAllReportsWithPostDTO(" +
            "r.id, r.ticket_id, r.create_by, r.create_time, r.content, r.status, r.report_type, post.url, pr.reportAction) " +
            "FROM Report r " +
            "JOIN PostReport pr ON r.id = pr.reportId " +
            "JOIN Post post ON pr.postId = post.id " +
            "WHERE (:reportId IS NULL OR r.id = :reportId) " +
            "AND (:ticketId IS NULL OR r.ticket_id LIKE %:ticketId%) " +
            "AND (:createBy IS NULL OR r.create_by = :createBy) " +
            "AND (:reportType IS NULL OR r.report_type = :reportType) " +
            "AND (:status IS NULL OR r.status = :status)")
    Page<FindAllReportsWithPostDTO> findAllReportsWithPosts(Pageable pageable,
                                                           @Param("reportId") Integer reportId,
                                                           @Param("ticketId") String ticketId,
                                                           @Param("createBy") Integer createBy,
                                                           @Param("reportType") ReportType reportType,
                                                           @Param("status") ReportStatus status);

    /**
     * Find all comment reports page.
     *
     * @param pageable the pageable
     * @param reportId the report id
     * @param ticketId the ticket id
     * @param createBy the create by
     * @param status   the status
     * @return the page
     */
    @Query("SELECT new com.demo.admissionportal.dto.entity.report.comment_report.FindAllCommentReportsDTO(" +
            "r.id, r.ticket_id, r.create_by, p.title, r.create_time, r.status, cr.reportAction, r.report_type, cr.commentContent, cr.isBanned) " +
            "FROM Report r " +
            "JOIN CommentReport cr ON r.id = cr.reportId " +
            "JOIN Comment c ON cr.commentId = c.id " +
            "JOIN Post p ON c.postId = p.id " +
            "WHERE (:reportId IS NULL OR r.id = :reportId) " +
            "AND (:ticketId IS NULL OR r.ticket_id LIKE %:ticketId%) " +
            "AND (:createBy IS NULL OR r.create_by = :createBy) " +
            "AND (:reportType IS NULL OR r.report_type = :reportType) " +
            "AND (:status IS NULL OR r.status = :status)")
    Page<FindAllCommentReportsDTO> findAllCommentReports(Pageable pageable,
                                                         @Param("reportId") Integer reportId,
                                                         @Param("ticketId") String ticketId,
                                                         @Param("createBy") Integer createBy,
                                                         @Param("reportType") ReportType reportType,
                                                         @Param("status") ReportStatus status);

    @Query("SELECT new com.demo.admissionportal.dto.entity.report.comment_report.FindAllCommentReportsByStatusDTO(" +
            "r.id, r.ticket_id, r.create_by, r.create_time, r.report_type, r.status) " +
            "FROM Report r " +
            "JOIN CommentReport cr ON r.id = cr.reportId " +
            "JOIN Comment c ON cr.commentId = c.id " +
            "JOIN Post p ON c.postId = p.id " +
            "WHERE (:reportId IS NULL OR r.id = :reportId) " +
            "AND (:ticketId IS NULL OR r.ticket_id LIKE %:ticketId%) " +
            "AND (:createBy IS NULL OR r.create_by = :createBy) " +
            "AND (:reportType IS NULL OR r.report_type = :reportType) " +
            "AND (:status IS NULL OR r.status = :status)")
    Page<FindAllCommentReportsByStatusDTO> findAllCommentReport(Pageable pageable,
                                                                @Param("reportId") Integer reportId,
                                                                @Param("ticketId") String ticketId,
                                                                @Param("createBy") Integer createBy,
                                                                @Param("reportType") ReportType reportType,
                                                                @Param("status") ReportStatus status);



    @Query("SELECT new com.demo.admissionportal.dto.entity.report.function_report.FindAllFuntionReportDTO(" +
            "r.id, r.status, r.create_time, r.report_type, r.create_by, r.ticket_id) " +
            "FROM Report r " +
            "JOIN FunctionReport fr ON r.id = fr.reportId " +
            "WHERE (:reportId IS NULL OR r.id = :reportId) " +
            "AND (:ticketId IS NULL OR r.ticket_id LIKE %:ticketId%) " +
            "AND (:createBy IS NULL OR r.create_by = :createBy) " +
            "AND (:status IS NULL OR r.status = :status)")
    Page<FindAllFuntionReportDTO> findAllFunctionReports(Pageable pageable,
                                                         @Param("reportId") Integer reportId,
                                                         @Param("ticketId") String ticketId,
                                                         @Param("createBy") Integer createBy,
                                                         @Param("status") ReportStatus status);

    @Query("SELECT new com.demo.admissionportal.dto.entity.report.function_report.FindAllFuntionReportDTO(" +
            "r.id, r.status, r.create_time, r.report_type, r.create_by, r.ticket_id) " +
            "FROM Report r " +
            "JOIN FunctionReport fr ON r.id = fr.reportId " +
            "WHERE (:reportId IS NULL OR r.id = :reportId) " +
            "AND (:ticketId IS NULL OR r.ticket_id LIKE %:ticketId%) " +
            "AND (:createBy IS NULL OR r.create_by = :createBy) " +
            "AND (:reportType IS NULL OR r.report_type = :reportType) " +
            "AND (:status IS NULL OR r.status = :status)")
    Page<FindAllFuntionReportDTO> findAllFunctionReport(Pageable pageable,
                                                         @Param("reportId") Integer reportId,
                                                         @Param("ticketId") String ticketId,
                                                         @Param("createBy") Integer createBy,
                                                         @Param("reportType") ReportType reportType,
                                                         @Param("status") ReportStatus status);

    @Query("SELECT new com.demo.admissionportal.dto.entity.report.function_report.FindAllFunctionReportByStatusDTO(" +
            "r.id, r.status, r.create_time, r.report_type, r.create_by, r.ticket_id) " +
            "FROM Report r " +
            "JOIN FunctionReport fr ON r.id = fr.reportId " +
            "WHERE (:reportId IS NULL OR r.id = :reportId) " +
            "AND (:ticketId IS NULL OR r.ticket_id LIKE %:ticketId%) " +
            "AND (:createBy IS NULL OR r.create_by = :createBy) " +
            "AND (:reportType IS NULL OR r.report_type = :reportType) " +
            "AND r.status = 'COMPLETED'")
    Page<FindAllFunctionReportByStatusDTO> findAllFunctionReportsByStatus(Pageable pageable,
                                                                          @Param("reportId") Integer reportId,
                                                                          @Param("ticketId") String ticketId,
                                                                          @Param("createBy") Integer createBy,
                                                                          @Param("reportType") ReportType reportType);
    @Modifying
    @Query("""
    UPDATE Report r 
    SET r.status = :status, 
        r.update_time = CURRENT_TIMESTAMP, 
        r.complete_time = CURRENT_TIMESTAMP,
        r.update_by = :updateBy,
        r.response = :response
    WHERE r.id IN (
        SELECT pr.reportId 
        FROM PostReport pr 
        WHERE pr.postId = :postId
    ) AND r.status <> :status
    """)
    int updateReportStatusByPostId(@Param("postId") Integer postId,
                                   @Param("status") ReportStatus status,
                                   @Param("updateBy") Integer updateBy,
                                   @Param("response") String response);


    @Query("""
    UPDATE Report r 
    SET r.status = :status, 
        r.update_time = CURRENT_TIMESTAMP, 
        r.complete_time = CURRENT_TIMESTAMP,
        r.update_by = :updateBy,
        r.response = :response
    WHERE r.id IN (
        SELECT cr.reportId 
        FROM CommentReport cr 
        WHERE cr.commentId = :commentId
    ) AND r.status <> :status
    """)
    int updateReportStatusByCommentId(@Param("postId") Integer commentId,
                                      @Param("status") ReportStatus status,
                                      @Param("updateBy") Integer updateBy,
                                      @Param("response") String response);
}
