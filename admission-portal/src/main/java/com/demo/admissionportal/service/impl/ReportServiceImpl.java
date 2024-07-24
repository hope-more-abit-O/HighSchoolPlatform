package com.demo.admissionportal.service.impl;

import com.demo.admissionportal.constants.*;
import com.demo.admissionportal.dto.entity.report.ReportPostResponseDTO;
import com.demo.admissionportal.dto.request.report.post_report.CreatePostReportRequest;
import com.demo.admissionportal.dto.request.report.post_report.UpdatePostReportRequest;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.dto.response.report.post_report.FindAllReportsWithPostResponseDTO;
import com.demo.admissionportal.dto.response.report.post_report.UpdatePostReportResponseDTO;
import com.demo.admissionportal.entity.Post;
import com.demo.admissionportal.entity.Report;
import com.demo.admissionportal.entity.User;
import com.demo.admissionportal.entity.sub_entity.PostReport;
import com.demo.admissionportal.entity.sub_entity.id.PostReportId;
import com.demo.admissionportal.repository.PostRepository;
import com.demo.admissionportal.repository.ReportRepository;
import com.demo.admissionportal.repository.UserRepository;
import com.demo.admissionportal.repository.sub_repository.PostReportRepository;
import com.demo.admissionportal.service.ReportService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class ReportServiceImpl implements ReportService {
    private final PostReportRepository postReportRepository;
    private final UserRepository userRepository;
    private final ReportRepository reportRepository;
    private final PostRepository postRepository;

    @Override
    @Transactional
    public ResponseData<PostReport> createPostReport(CreatePostReportRequest request, Authentication authentication) {
        try {
            String username = authentication.getName();
            Optional<User> existUser = userRepository.findByUsername(username);
            if (existUser.isEmpty()) {
                log.info("User not found");
                return new ResponseData<>(ResponseCode.C203.getCode(), "Người dùng không được tìm thấy !");
            }
            if(!existUser.get().getRole().equals(Role.USER)){
                log.info("User not allow to create");
                return new ResponseData<>(ResponseCode.C203.getCode(), "Người dùng không được phép !");
            }
            User user = existUser.get();
            Integer userId = user.getId();

            Optional<Post> existPost = postRepository.findById(request.getPostId());
            if (existPost.isEmpty()) {
                log.info("Post not found");
                return new ResponseData<>(ResponseCode.C204.getCode(), "Bài viết không được tìm thấy !");
            }
            Post post = existPost.get();

            Report newReport = new Report();
            newReport.setTicket_id(generateTicketId());
            newReport.setCreate_by(userId);
            newReport.setCreate_time(new Date());
            newReport.setContent(request.getContent());
            newReport.setReport_type(ReportType.POST);
            newReport.setStatus(ReportStatus.PENDING);
            Report savedReport = reportRepository.save(newReport);

            PostReport postReport = new PostReport();
            postReport.setReportId(savedReport.getId());
            postReport.setPostId(post.getId());
            postReport.setReportAction(PostReportActionType.NONE.name());
            postReportRepository.save(postReport);

            return new ResponseData<>(ResponseCode.C200.getCode(), "Báo cáo đã được tạo thành công", postReport);
        } catch (Exception e) {
            log.error("Error while creating post report", e);
            return new ResponseData<>(ResponseCode.C207.getCode(), "Đã xảy ra lỗi khi tạo báo cáo bài viết");
        }
    }

    @Override
    public ResponseData<ReportPostResponseDTO> getPostReportById(Integer reportId, Authentication authentication) {
        String username = authentication.getName();
        Optional<User> existUser = userRepository.findByUsername(username);
        if (existUser.isEmpty() ) {
            log.info("Staff not found");
            return new ResponseData<>(ResponseCode.C203.getCode(), "Người dùng không được tìm thấy !");
        }
        if(!existUser.get().getRole().equals(Role.STAFF)){
            log.info("User not allow to get");
            return new ResponseData<>(ResponseCode.C203.getCode(), "Người dùng không được phép !");
        }

        Optional<Report> reportOpt = reportRepository.findById(reportId);
        if (reportOpt.isEmpty()) {
            return new ResponseData<>(ResponseCode.C203.getCode(), "Báo cáo không được tìm thấy !");
        }
        Report report = reportOpt.get();

        Optional<PostReport> postReportOpt = postReportRepository.findAll()
                .stream()
                .filter(pr -> pr.getReportId().equals(reportId))
                .findFirst();
        if (postReportOpt.isEmpty()) {
            return new ResponseData<>(ResponseCode.C203.getCode(), "Báo cáo về bài viết không được tìm thấy !");
        }
        PostReport postReport = postReportOpt.get();
        Integer postId = postReport.getPostId();

        Optional<Post> postOpt = postRepository.findById(postId);
        if (postOpt.isEmpty()) {
            return new ResponseData<>(ResponseCode.C203.getCode(), "Bài viết không được tìm thấy !");
        }
        Post post = postOpt.get();
        ReportPostResponseDTO responseDTO = new ReportPostResponseDTO(
                report.getId(),
                report.getTicket_id(),
                report.getCreate_by(),
                report.getCreate_time(),
                report.getReport_type().toString(),
                report.getContent(),
                post.getId(),
                post.getTitle(),
                post.getCreateBy(),
                post.getCreateTime(),
                report.getStatus()
        );
        return new ResponseData<>(ResponseCode.C200.getCode(), "Thông tin báo cáo về bài viết được tìm thấy !", responseDTO);
    }

    @Override
    @Transactional
    public ResponseData<UpdatePostReportResponseDTO> updatePostReport(Integer reportId, UpdatePostReportRequest request, Authentication authentication) {
        try {
            String username = authentication.getName();
            Optional<User> existUser = userRepository.findByUsername(username);
            if (existUser.isEmpty()) {
                log.info("Staff not found");
                return new ResponseData<>(ResponseCode.C203.getCode(), "Người dùng không được tìm thấy !");
            }
            if (!existUser.get().getRole().equals(Role.STAFF)) {
                log.info("User not allowed to update");
                return new ResponseData<>(ResponseCode.C203.getCode(), "Người dùng không được phép !");
            }
            User user = existUser.get();
            Integer staffId = user.getId();

            Optional<PostReport> postReportOpt = postReportRepository.findAll()
                    .stream()
                    .filter(pr -> pr.getReportId().equals(reportId))
                    .findFirst();
            if (postReportOpt.isEmpty()) {
                return new ResponseData<>(ResponseCode.C203.getCode(), "Báo cáo về bài viết không được tìm thấy !");
            }
            PostReport postReport = postReportOpt.get();

            Optional<Report> reportOpt = reportRepository.findById(reportId);
            if (reportOpt.isEmpty()) {
                return new ResponseData<>(ResponseCode.C203.getCode(), "Báo cáo không được tìm thấy !");
            }
            Report report = reportOpt.get();

            postReport.setReportAction(request.getReportAction());
            postReportRepository.save(postReport);

            report.setUpdate_time(new Date());
            report.setUpdate_by(staffId);
            report.setComplete_time(new Date());
            report.setComplete_by(staffId);
            report.setResponse(request.getResponse());
            report.setStatus(ReportStatus.COMPLETED);
            reportRepository.save(report);

            Optional<Post> postOpt = postRepository.findById(postReport.getPostId());
            if (postOpt.isEmpty()) {
                return new ResponseData<>(ResponseCode.C203.getCode(), "Bài viết không được tìm thấy !");
            }
            Post post = postOpt.get();

            UpdatePostReportResponseDTO responseDTO = new UpdatePostReportResponseDTO(
                    report.getId(),
                    report.getTicket_id(),
                    report.getCreate_by(),
                    report.getCreate_time(),
                    report.getReport_type().toString(),
                    report.getContent(),
                    post.getId(),
                    post.getTitle(),
                    post.getCreateBy(),
                    post.getCreateTime(),
                    report.getUpdate_time(),
                    report.getUpdate_by(),
                    report.getComplete_time(),
                    report.getComplete_by(),
                    postReport.getReportAction(),
                    report.getResponse(),
                    report.getStatus()
            );
            return new ResponseData<>(ResponseCode.C200.getCode(), "Update successful", responseDTO);
        } catch (Exception e) {
            log.error("Error while updating post report", e);
            return new ResponseData<>(ResponseCode.C207.getCode(), "Đã xảy ra lỗi khi cập nhật báo cáo bài viết");
        }
    }

    @Override
    public ResponseData<Page<FindAllReportsWithPostResponseDTO>> findAllPostReports(Pageable pageable, Authentication authentication) {
        try {
            String username = authentication.getName();
            Optional<User> existUser = userRepository.findByUsername(username);
            if (existUser.isEmpty()) {
                log.info("User not found");
                return new ResponseData<>(ResponseCode.C203.getCode(), "Người dùng không được tìm thấy !");
            }
            if (!existUser.get().getRole().equals(Role.STAFF)) {
                log.info("User not allowed to view reports");
                return new ResponseData<>(ResponseCode.C203.getCode(), "Người dùng không được phép !");
            }

            Page<PostReport> postReports = postReportRepository.findAll(pageable);
            Page<FindAllReportsWithPostResponseDTO> responseDTOs = postReports.map(postReport -> {
                Optional<Report> reportOpt = reportRepository.findById(postReport.getReportId());
                if (reportOpt.isPresent()) {
                    Report report = reportOpt.get();
                    return new FindAllReportsWithPostResponseDTO(
                            report.getId(),
                            report.getTicket_id(),
                            report.getCreate_by(),
                            report.getCreate_time(),
                            report.getContent(),
                            report.getStatus(),
                            postRepository.findById(postReport.getPostId()).map(Post::getUrl).orElse(null)
                    );
                }
                return null;
            });

            return new ResponseData<>(ResponseCode.C200.getCode(), "Danh sách báo cáo bài viết được tìm thấy", responseDTOs);
        } catch (Exception e) {
            log.error("Error while fetching post reports", e);
            return new ResponseData<>(ResponseCode.C207.getCode(), "Đã xảy ra lỗi khi tìm kiếm báo cáo bài viết");
        }
    }



    private String generateTicketId() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
