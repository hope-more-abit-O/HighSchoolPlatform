package com.demo.admissionportal.repository.sub_repository;

import com.demo.admissionportal.entity.sub_entity.CommentReport;
import com.demo.admissionportal.entity.sub_entity.id.CommentReportId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentReportRepository extends JpaRepository<CommentReport, CommentReportId> {
}
