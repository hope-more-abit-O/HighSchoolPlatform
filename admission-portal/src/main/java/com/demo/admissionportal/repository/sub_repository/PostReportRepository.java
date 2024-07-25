package com.demo.admissionportal.repository.sub_repository;

import com.demo.admissionportal.entity.sub_entity.PostReport;
import com.demo.admissionportal.entity.sub_entity.id.PostReportId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostReportRepository extends JpaRepository<PostReport, PostReportId> {
}
