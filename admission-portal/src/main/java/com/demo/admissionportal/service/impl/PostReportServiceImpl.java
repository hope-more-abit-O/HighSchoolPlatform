package com.demo.admissionportal.service.impl;

import com.demo.admissionportal.constants.ReportStatus;
import com.demo.admissionportal.constants.ReportType;
import com.demo.admissionportal.constants.ResponseCode;
import com.demo.admissionportal.dto.request.post_report.CreatePostReportRequest;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.entity.Report;
import com.demo.admissionportal.entity.User;
import com.demo.admissionportal.entity.sub_entity.PostReport;

import com.demo.admissionportal.repository.PostRepository;
import com.demo.admissionportal.repository.ReportRepository;
import com.demo.admissionportal.repository.sub_repository.PostReportRepository;
import com.demo.admissionportal.repository.UserRepository;
import com.demo.admissionportal.service.PostReportService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class PostReportServiceImpl implements PostReportService {
    private final PostReportRepository postReportRepository;
    private final UserRepository userRepository;
    private final ReportRepository reportRepository;
    private final PostRepository postRepository;

    @Override
    public ResponseData<PostReport> createPostReport(CreatePostReportRequest request, Authentication authentication) {
        try {
            String username = authentication.getName();
            Optional<User> existUser = userRepository.findByUsername(username);
            if (existUser.isEmpty()) {
                log.info("User not found");
                return new ResponseData<>(ResponseCode.C203.getCode(), "Người dùng không được tìm thấy !");
            }
            List<Report> reports = reportRepository.findAllById()
            Report newReport = new Report();
            User user = existUser.get();
            Integer userId = user.getId();
            newReport.setId(newReport.getId());
            newReport.setCreate_by(userId);
            newReport.setCreate_time(new Date());
            newReport.setContent(request.getContent());
            newReport.setReport_type(ReportType.POST);
            newReport.setStatus(ReportStatus.PENDING);
            reportRepository.save(newReport);

            PostReport postId = postReportRepository.findPostById(request.getPost_id());
            if (postId == null ){
                return new ResponseData<>(ResponseCode.C203.getCode(), "Không tìm thấy bài viết này.");
            }
            List<PostReport> postReports = new ArrayList<>();
            for (Report report: reports){
                PostReport reportPost = new PostReport(reportPost.getReportId(), null);
            }

            return null;
        } catch (Exception e){
            return null;
        }
    }
}
