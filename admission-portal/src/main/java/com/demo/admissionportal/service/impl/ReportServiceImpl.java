package com.demo.admissionportal.service.impl;

import com.demo.admissionportal.constants.*;
import com.demo.admissionportal.dto.entity.ActionerDTO;
import com.demo.admissionportal.dto.entity.report.comment_report.FindAllCommentReportsDTO;
import com.demo.admissionportal.dto.entity.report.function_report.FindAllFuntionReportDTO;
import com.demo.admissionportal.dto.entity.report.post_report.FindAllReportsWithPostDTO;
import com.demo.admissionportal.dto.request.report.comment_report.CreateCommentReportRequest;
import com.demo.admissionportal.dto.request.report.comment_report.UpdateCommentReportRequest;
import com.demo.admissionportal.dto.request.report.function_report.CreateFunctionReportRequest;
import com.demo.admissionportal.dto.request.report.function_report.UpdateFunctionReportRequest;
import com.demo.admissionportal.dto.request.report.post_report.CreatePostReportRequest;
import com.demo.admissionportal.dto.request.report.post_report.UpdatePostReportRequest;
import com.demo.admissionportal.dto.response.ReportResponse;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.dto.response.file.UploadMultipleFilesResponse;
import com.demo.admissionportal.dto.response.report.FindAllReportsResponse;
import com.demo.admissionportal.dto.response.report.comment_report.CommentReportResponse;
import com.demo.admissionportal.dto.response.report.comment_report.UpdateCommentReportResponse;
import com.demo.admissionportal.dto.response.report.comment_report.UpdateCommentReportResponseDTO;
import com.demo.admissionportal.dto.response.report.function_report.FunctionReportResponse;
import com.demo.admissionportal.dto.response.report.function_report.UpdateFunctionReportResponse;
import com.demo.admissionportal.dto.response.report.function_report.UpdateFunctionReportResponseDTO;
import com.demo.admissionportal.dto.response.report.post_report.ReportPostResponse;
import com.demo.admissionportal.dto.response.report.post_report.UpdatePostReportResponse;
import com.demo.admissionportal.entity.*;
import com.demo.admissionportal.entity.sub_entity.CommentReport;
import com.demo.admissionportal.entity.sub_entity.FunctionReport;
import com.demo.admissionportal.entity.sub_entity.PostReport;
import com.demo.admissionportal.repository.*;
import com.demo.admissionportal.repository.sub_repository.CommentReportRepository;
import com.demo.admissionportal.repository.sub_repository.PostReportRepository;
import com.demo.admissionportal.service.FirebaseStorageService;
import com.demo.admissionportal.service.ReportService;
import com.demo.admissionportal.util.impl.NameUtils;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
@Slf4j
public class ReportServiceImpl implements ReportService {
    private final PostReportRepository postReportRepository;
    private final UserRepository userRepository;
    private final ReportRepository reportRepository;
    private final PostRepository postRepository;
    private final UserInfoRepository userInfoRepository;
    private final StaffInfoRepository staffInfoRepository;
    private final ModelMapper modelMapper;
    private final CommentRepository commentRepository;
    private final CommentReportRepository commentReportRepository;
    private final FunctionReportRepository functionReportRepository;
    private final FirebaseStorageService firebaseStorageService;

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
            if (!existUser.get().getRole().equals(Role.USER)) {
                log.info("User not allowed to create");
                return new ResponseData<>(ResponseCode.C203.getCode(), "Người dùng không được phép !");
            }
            User user = existUser.get();
            Integer userId = user.getId();

            Optional<Post> existPost = postRepository.findById(request.getPostId());
            if (existPost.isEmpty() || existPost.equals(PostStatus.INACTIVE.name())) {
                log.info("Post not found");
                return new ResponseData<>(ResponseCode.C204.getCode(), "Bài viết không được tìm thấy !");
            }
            Post post = existPost.get();
            PostReport postReport = new PostReport();

            Report newReport = modelMapper.map(request, Report.class);
            newReport.setTicket_id(generateTicketId());
            newReport.setCreate_by(userId);
            newReport.setCreate_time(new Date());
            newReport.setReport_type(ReportType.POST);
            newReport.setStatus(ReportStatus.PENDING);

            Report savedReport = reportRepository.save(newReport);
            postReport.setReportId(savedReport.getId());
            postReport.setPostId(post.getId());
            postReport.setReportAction(PostReportActionType.NONE.name());


            PostReport savedPostReport = postReportRepository.save(postReport);


            return new ResponseData<>(ResponseCode.C200.getCode(), "Báo cáo đã được tạo thành công", savedPostReport);
        } catch (Exception e) {
            log.error("Error while creating post report", e);
            return new ResponseData<>(ResponseCode.C207.getCode(), "Đã xảy ra lỗi khi tạo báo cáo bài viết");
        }
    }

    private String generateTicketId() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    @Override
    @Transactional
    public ResponseData<UpdatePostReportResponse> updatePostReport(Integer reportId, UpdatePostReportRequest request, Authentication authentication) {
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
            if (reportOpt.get().getStatus().equals(ReportStatus.COMPLETED)){
                return new ResponseData<>(ResponseCode.C204.getCode(), "Báo cáo đã được xử lý.");
            }
            Report report = reportOpt.get();

            postReport.setReportAction(request.getReportAction());
            postReportRepository.save(postReport);

            if (request.getReportAction().equals(PostReportActionType.DELETE.name())) {
                Optional<Post> postOpt = postRepository.findById(postReport.getPostId());
                if (postOpt.isPresent()) {
                    Post post = postOpt.get();
                    post.setStatus(PostStatus.INACTIVE);
                    postRepository.save(post);
                }
            }

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

            ActionerDTO actioner = getUserDetails(report.getCreate_by());

            UpdatePostReportResponse responseDTO = modelMapper.map(report, UpdatePostReportResponse.class);
            responseDTO.setPostId(post.getId());
            responseDTO.setPostTitle(post.getTitle());
            responseDTO.setPostCreateBy(post.getCreateBy());
            responseDTO.setPostCreateTime(post.getCreateTime());
            responseDTO.setCreateBy(actioner);
            responseDTO.setReportAction(postReport.getReportAction());
            responseDTO.setCompleteBy(staffId);
            responseDTO.setUpdateBy(staffId);
            return new ResponseData<>(ResponseCode.C200.getCode(), "Cập nhật báo cáo bài viết thành công !", responseDTO);
        } catch (Exception e) {
            log.error("Error while updating post report", e);
            return new ResponseData<>(ResponseCode.C207.getCode(), "Đã xảy ra lỗi khi cập nhật báo cáo bài viết");
        }
    }
    @Override
    public ResponseData<Page<FindAllReportsResponse>> findAllReports(Pageable pageable, Authentication authentication, Integer reportId, String ticketId, Integer createBy, String content, ReportType reportType, ReportStatus status) {
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

            List<FindAllReportsResponse> allReports = new ArrayList<>();
            Page<FindAllReportsWithPostDTO> postReports = reportRepository.findAllReportsWithPosts(pageable, reportId, ticketId, createBy, reportType, status);
            postReports.forEach(report -> {
                ActionerDTO actioner = null;
                if (report.getCreateBy() != null && report.getCreateBy().getId() != null) {
                    actioner = getUserDetails(report.getCreateBy().getId());
                }
                FindAllReportsResponse response = modelMapper.map(report, FindAllReportsResponse.class);
                response.setCreateBy(actioner);
                response.setCommentContent(null);
                response.setPostTitle(null);
                allReports.add(response);
            });

            Page<FindAllCommentReportsDTO> commentReports = reportRepository.findAllCommentReports(pageable, reportId, ticketId, createBy, status);
            commentReports.forEach(report -> {
                ActionerDTO actioner = null;
                if (report.getCreateBy() != null && report.getCreateBy().getId() != null) {
                    actioner = getUserDetails(report.getCreateBy().getId());
                }
                FindAllReportsResponse response = modelMapper.map(report, FindAllReportsResponse.class);
                response.setCreateBy(actioner);
                response.setPostUrl(null);
                allReports.add(response);
            });

            Page<FindAllFuntionReportDTO> functionReports = reportRepository.findAllFunctionReport(pageable, reportId, ticketId, createBy, reportType, status);
            functionReports.forEach(report -> {
                ActionerDTO actioner = null;
                if (report.getCreateBy() != null && report.getCreateBy().getId() != null) {
                    actioner = getUserDetails(report.getCreateBy().getId());
                }
                FindAllReportsResponse response = modelMapper.map(report, FindAllReportsResponse.class);
                response.setCreateBy(actioner);
                response.setCommentContent(null);
                response.setPostUrl(null);
                response.setPostTitle(null);
                allReports.add(response);
            });
            Page<FindAllReportsResponse> reportPage = new PageImpl<>(allReports, pageable, postReports.getTotalElements() + commentReports.getTotalElements() + functionReports.getTotalElements());

            return new ResponseData<>(ResponseCode.C200.getCode(), "Danh sách báo cáo được tìm thấy", reportPage);
        } catch (Exception e) {
            log.error("Error while fetching reports", e);
            return new ResponseData<>(ResponseCode.C207.getCode(), "Đã xảy ra lỗi khi tìm kiếm báo cáo");
        }
    }
    //Comment Report
    @Override
    @Transactional
    public ResponseData<CommentReport> createCommentReport(CreateCommentReportRequest request, Authentication authentication) {
        try {
            String username = authentication.getName();
            Optional<User> existUser = userRepository.findByUsername(username);
            if (existUser.isEmpty()) {
                log.info("User not found");
                return new ResponseData<>(ResponseCode.C203.getCode(), "Người dùng không được tìm thấy !");
            }
            if (!existUser.get().getRole().equals(Role.USER)) {
                log.info("User not allowed to create");
                return new ResponseData<>(ResponseCode.C203.getCode(), "Người dùng không được phép !");
            }
            User user = existUser.get();
            Integer userId = user.getId();

            Optional<Comment> existComment = commentRepository.findById(request.getCommentId());
            if (existComment.isEmpty() || existComment.equals(CommentStatus.DELETE.name())) {
                log.info("Comment not found");
                return new ResponseData<>(ResponseCode.C204.getCode(), "Bình luận không được tìm thấy !");
            }
            Comment comment = existComment.get();

            Report newReport = new Report();
            newReport.setTicket_id(generateTicketId());
            newReport.setCreate_by(userId);
            newReport.setCreate_time(new Date());
            newReport.setContent(request.getContent());
            newReport.setReport_type(ReportType.COMMENT);
            newReport.setStatus(ReportStatus.PENDING);
            Report savedReport = reportRepository.save(newReport);

            CommentReport commentReport = new CommentReport();
            commentReport.setReportId(savedReport.getId());
            commentReport.setCommentContent(comment.getContent());
            commentReport.setCommentId(comment.getId());
            commentReport.setReportAction(PostReportActionType.NONE.name());

            if (comment.getCommentParentId() == null) {
                comment.setComment_type(CommentType.ROOT);
            } else {
                comment.setComment_type(CommentType.CHILD);
            }

            commentReportRepository.save(commentReport);

            return new ResponseData<>(ResponseCode.C200.getCode(), "Báo cáo đã được tạo thành công", commentReport);
        } catch (Exception e) {
            log.error("Error while creating comment report", e);
            return new ResponseData<>(ResponseCode.C207.getCode(), "Đã xảy ra lỗi khi tạo báo cáo bình luận");
        }
    }

    @Override
    public ResponseData<ReportResponse> getReportById(Integer reportId, Authentication authentication) {
        String username = authentication.getName();
        Optional<User> existUser = userRepository.findByUsername(username);
        if (existUser.isEmpty()) {
            log.info("Staff not found");
            return new ResponseData<>(ResponseCode.C203.getCode(), "Người dùng không được tìm thấy !");
        }
        if (!existUser.get().getRole().equals(Role.STAFF)) {
            log.info("User not allowed to get");
            return new ResponseData<>(ResponseCode.C203.getCode(), "Người dùng không được phép !");
        }

        Optional<Report> reportOpt = reportRepository.findById(reportId);
        if (reportOpt.isEmpty()) {
            return new ResponseData<>(ResponseCode.C203.getCode(), "Báo cáo không được tìm thấy !");
        }
        Report report = reportOpt.get();

        ReportResponse responseDTO = modelMapper.map(report, ReportResponse.class);

        Optional<PostReport> postReportOpt = postReportRepository.findAll()
                .stream()
                .filter(pr -> pr.getReportId().equals(reportId))
                .findFirst();

        Optional<CommentReport> commentReportOpt = commentReportRepository.findAll()
                .stream()
                .filter(cr -> cr.getReportId().equals(reportId))
                .findFirst();

        Optional<FunctionReport> functionReportOpt = functionReportRepository.findAll()
                .stream()
                .filter(fr -> fr.getReportId().equals(reportId))
                .findFirst();

        if (postReportOpt.isPresent()) {
            ReportPostResponse postResponse = handlePostReport(postReportOpt.get());
            responseDTO.setFunction(null);
            responseDTO.setComment(null);
            responseDTO.setPost(postResponse);
        } else if (commentReportOpt.isPresent()) {
            CommentReportResponse commentResponse = handleCommentReport(commentReportOpt.get());
            responseDTO.setPost(null);
            responseDTO.setFunction(null);
            responseDTO.setComment(commentResponse);
        } else if (functionReportOpt.isPresent()) {
            FunctionReportResponse functionResponse = handleFunctionReport(functionReportOpt.get());
            responseDTO.setPost(null);
            responseDTO.setComment(null);
            responseDTO.setFunction(functionResponse);
        } else {
            return new ResponseData<>(ResponseCode.C203.getCode(), "Không tìm thấy báo cáo liên quan !");
        }

        ActionerDTO actioner = getUserDetails(report.getCreate_by());
        responseDTO.setCreateBy(actioner);

        return new ResponseData<>(ResponseCode.C200.getCode(), "Thông tin báo cáo được tìm thấy !", responseDTO);
    }

    private ReportPostResponse handlePostReport(PostReport postReport) {
        Optional<Post> postOpt = postRepository.findById(postReport.getPostId());
        if (postOpt.isEmpty()) {
            throw new RuntimeException("Bài viết không được tìm thấy !");
        }
        Post post = postOpt.get();

        ReportPostResponse responseDTO = modelMapper.map(postReport, ReportPostResponse.class);
        responseDTO.setPostId(post.getId());
        responseDTO.setTitle(post.getTitle());
        responseDTO.setPostCreateBy(post.getCreateBy());
        responseDTO.setPostCreateTime(post.getCreateTime());
        responseDTO.setStatus(post.getStatus().name);
        return responseDTO;
    }
    private CommentReportResponse handleCommentReport(CommentReport commentReport) {
        Optional<Comment> commentOpt = commentRepository.findById(commentReport.getCommentId());
        if (commentOpt.isEmpty()) {
            throw new RuntimeException("Bình luận không được tìm thấy !");
        }
        Comment comment = commentOpt.get();
        CommentReportResponse responseDTO = modelMapper.map(commentReport, CommentReportResponse.class);
        responseDTO.setCommentId(comment.getId());
        responseDTO.setContent(comment.getContent());
        responseDTO.setCommentCreateBy(comment.getCommenter_id());
        responseDTO.setCommentCreateTime(comment.getCreate_time().toString());
        responseDTO.setCommentType(comment.getComment_type().name());
        responseDTO.setCommentStatus(comment.getComment_status().name());
        return responseDTO;
    }
    private FunctionReportResponse handleFunctionReport(FunctionReport functionReport) {
        FunctionReportResponse responseDTO = modelMapper.map(functionReport, FunctionReportResponse.class);
        responseDTO.setProofs(functionReport.getProofs());
        return responseDTO;
    }
    @Override
    @Transactional
    public ResponseData<UpdateCommentReportResponse> updateCommentReport(Integer reportId, UpdateCommentReportRequest request, Authentication authentication) {
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

            Optional<CommentReport> commentReportOpt = commentReportRepository.findAll()
                    .stream()
                    .filter(cr -> cr.getReportId().equals(reportId))
                    .findFirst();
            if (commentReportOpt.isEmpty()) {
                return new ResponseData<>(ResponseCode.C203.getCode(), "Báo cáo về bình luận không được tìm thấy !");
            }
            CommentReport commentReport = commentReportOpt.get();

            Optional<Report> reportOpt = reportRepository.findById(reportId);
            if (reportOpt.isEmpty()) {
                return new ResponseData<>(ResponseCode.C203.getCode(), "Báo cáo không được tìm thấy !");
            }
            if (reportOpt.get().getStatus().equals(ReportStatus.COMPLETED)) {
                return new ResponseData<>(ResponseCode.C204.getCode(), "Báo cáo đã được xử lý.");
            }
            Report report = reportOpt.get();

            commentReport.setReportAction(request.getReportAction());
            commentReportRepository.save(commentReport);

            if (request.getReportAction().equals(PostReportActionType.DELETE.name())) {
                Optional<Comment> commentOpt = commentRepository.findById(commentReport.getCommentId());
                if (commentOpt.isPresent()) {
                    Comment comment = commentOpt.get();
                    comment.setComment_status(CommentStatus.DELETE);
                    commentRepository.save(comment);

                    if (Boolean.TRUE.equals(request.getIsBanned())) {
                        Optional<User> commentUserOpt = userRepository.findById(comment.getCommenter_id());
                        if (commentUserOpt.isPresent()) {
                            User commentUser = commentUserOpt.get();
                            commentUser.setStatus(AccountStatus.INACTIVE);
                            userRepository.save(commentUser);
                        }
                    }
                }
            }
            Date now = new Date();
            report.setUpdate_time(now);
            report.setUpdate_by(staffId);
            report.setComplete_time(now);
            report.setComplete_by(staffId);
            report.setResponse(request.getResponse());
            report.setStatus(ReportStatus.COMPLETED);
            reportRepository.save(report);

            Optional<Comment> commentOpt = commentRepository.findById(commentReport.getCommentId());
            if (commentOpt.isEmpty()) {
                return new ResponseData<>(ResponseCode.C203.getCode(), "Bình luận không được tìm thấy !");
            }
            Comment comment = commentOpt.get();

            ActionerDTO actioner = getUserDetails(report.getCreate_by());

            UpdateCommentReportResponseDTO responseDTO = modelMapper.map(report, UpdateCommentReportResponseDTO.class);
            responseDTO.setActioner(actioner);
            responseDTO.setCommentId(comment.getId());
            responseDTO.setCommentContent(comment.getContent());
            responseDTO.setCommenterId(comment.getCommenter_id());
            responseDTO.setCommentCreateTime(comment.getCreate_time());
            responseDTO.setReportAction(PostReportActionType.valueOf(commentReport.getReportAction()));

            UpdateCommentReportResponse updateCommentReportResponse = modelMapper.map(responseDTO, UpdateCommentReportResponse.class);
            updateCommentReportResponse.setCompleteBy(staffId);
            updateCommentReportResponse.setUpdateBy(staffId);
            return new ResponseData<>(ResponseCode.C200.getCode(), "Cập nhật báo cáo bình luận thành công !", updateCommentReportResponse);
        } catch (Exception e) {
            log.error("Error while updating comment report", e);
            return new ResponseData<>(ResponseCode.C207.getCode(), "Đã xảy ra lỗi khi cập nhật báo cáo bình luận");
        }
    }
    //Function
    @Override
    @Transactional
    public ResponseData<FunctionReport> createFunctionReport(CreateFunctionReportRequest request, Authentication authentication) {
        try {
            String username = authentication.getName();
            Optional<User> existUser = userRepository.findByUsername(username);
            if (existUser.isEmpty()) {
                log.info("User not found");
                return new ResponseData<>(ResponseCode.C203.getCode(), "Người dùng không được tìm thấy !");
            }
            User user = existUser.get();
            Integer userId = user.getId();

            ResponseData<UploadMultipleFilesResponse> uploadResponse = firebaseStorageService.uploadMultipleFiles(request.getProofs());
            List<String> fileUrls = uploadResponse.getData().getUrl();
            String proofs = String.join(",", fileUrls);

            Report newReport = new Report();
            newReport.setTicket_id(generateTicketId());
            newReport.setCreate_by(userId);
            newReport.setCreate_time(new Date());
            newReport.setContent(request.getContent());
            newReport.setReport_type(ReportType.FUNCTION);
            newReport.setStatus(ReportStatus.PENDING);
            Report savedReport = reportRepository.save(newReport);

            FunctionReport functionReport = new FunctionReport();
            functionReport.setReportId(savedReport.getId());
            functionReport.setContent(request.getContent());
            functionReport.setProofs(proofs);
            functionReport.setReportAction(PostReportActionType.NONE.name());
            functionReportRepository.save(functionReport);

            return new ResponseData<>(ResponseCode.C200.getCode(), "Báo cáo chức năng Web thành công !", functionReport);
        } catch (Exception e) {
            log.error("Error while creating function report", e);
            return new ResponseData<>(ResponseCode.C207.getCode(), "Error while creating function report");
        }
    }

    @Override
    @Transactional
    public ResponseData<UpdateFunctionReportResponse> updateFunctionReport(Integer reportId, UpdateFunctionReportRequest request, Authentication authentication) {
        try {
            String username = authentication.getName();
            Optional<User> existUser = userRepository.findByUsername(username);
            if (existUser.isEmpty()) {
                log.info("User not found");
                return new ResponseData<>(ResponseCode.C203.getCode(), "Người dùng không được tìm thấy !");
            }
            if (!existUser.get().getRole().equals(Role.STAFF)) {
                log.info("User not allowed to update");
                return new ResponseData<>(ResponseCode.C203.getCode(), "Người dùng không được phép !");
            }
            User user = existUser.get();
            Integer staffId = user.getId();

            Optional<FunctionReport> functionReportOpt = functionReportRepository.findById(reportId);
            if (functionReportOpt.isEmpty()) {
                return new ResponseData<>(ResponseCode.C203.getCode(), "Báo cáo về chức năng không được tìm thấy !");
            }
            FunctionReport functionReport = functionReportOpt.get();

            Optional<Report> reportOpt = reportRepository.findById(reportId);
            if (reportOpt.isEmpty()) {
                return new ResponseData<>(ResponseCode.C203.getCode(), "Báo cáo không được tìm thấy !");
            }
            if (reportOpt.get().getStatus().equals(ReportStatus.COMPLETED)) {
                return new ResponseData<>(ResponseCode.C204.getCode(), "Báo cáo đã được xử lý.");
            }
            Report report = reportOpt.get();

            functionReport.setReportAction(PostReportActionType.NONE.name());
            functionReportRepository.save(functionReport);

            report.setUpdate_time(new Date());
            report.setUpdate_by(staffId);
            report.setComplete_time(new Date());
            report.setComplete_by(staffId);
            report.setResponse(request.getResponse());
            report.setStatus(ReportStatus.COMPLETED);
            reportRepository.save(report);

            ActionerDTO actioner = getUserDetails(report.getCreate_by());

            UpdateFunctionReportResponseDTO responseDTO = modelMapper.map(report, UpdateFunctionReportResponseDTO.class);
            responseDTO.setCreateBy(actioner);
            responseDTO.setReportContent(functionReport.getContent());
            responseDTO.setProofs(functionReport.getProofs());
            responseDTO.setReportAction(PostReportActionType.valueOf(functionReport.getReportAction()));

            UpdateFunctionReportResponse result = modelMapper.map(responseDTO, UpdateFunctionReportResponse.class);
            result.setCompleteBy(staffId);
            result.setUpdateBy(staffId);
            result.setReportAction(String.valueOf(responseDTO.getReportAction()));

            return new ResponseData<>(ResponseCode.C200.getCode(), "Cập nhật báo cáo chức năng thành công !", result);
        } catch (Exception e) {
            log.error("Error while updating function report", e);
            return new ResponseData<>(ResponseCode.C207.getCode(), "Đã xảy ra lỗi khi cập nhật báo cáo chức năng");
        }
    }

    private ActionerDTO getStaffDetails(Integer userId) {
        return userRepository.findById(userId)
                .map(user -> {
                    StaffInfo staffInfo = staffInfoRepository.findById(userId).orElse(null);
                    String fullName;
                    if (staffInfo != null) {
                        fullName = NameUtils.getFullName(staffInfo.getFirstName(), staffInfo.getMiddleName(), staffInfo.getLastName());
                    } else {
                        fullName = "Nhân viên UAP";
                    }
                    ActionerDTO actionerDTO = new ActionerDTO(user.getId(), fullName, null, null);
                    actionerDTO.setRole(modelMapper.map(user.getRole(), String.class));
                    actionerDTO.setStatus(modelMapper.map(user.getStatus(), String.class));
                    return actionerDTO;
                }).orElse(null);
    }

    private ActionerDTO getUserDetails(Integer userId) {
        return userRepository.findById(userId)
                .map(user -> {
                    UserInfo userInfo = userInfoRepository.findById(userId).orElse(null);
                    String fullName;
                    if (userInfo != null) {
                        fullName = NameUtils.getFullName(userInfo.getFirstName(), userInfo.getMiddleName(), userInfo.getLastName());
                    } else {
                        fullName = "Người dùng UAP";
                    }
                    ActionerDTO actionerDTO = new ActionerDTO(user.getId(), fullName, null, null);
                    actionerDTO.setRole(modelMapper.map(user.getRole(), String.class));
                    actionerDTO.setStatus(modelMapper.map(user.getStatus(), String.class));
                    return actionerDTO;
                }).orElse(null);
    }
}
