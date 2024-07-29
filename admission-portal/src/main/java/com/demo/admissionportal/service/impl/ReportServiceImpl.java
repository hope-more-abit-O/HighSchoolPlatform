package com.demo.admissionportal.service.impl;

import com.demo.admissionportal.constants.*;
import com.demo.admissionportal.dto.entity.ActionerDTO;
import com.demo.admissionportal.dto.entity.report.comment_report.FindAllCommentReportsDTO;
import com.demo.admissionportal.dto.entity.report.post_report.FindAllReportsCompletedDTO;
import com.demo.admissionportal.dto.request.report.comment_report.CreateCommentReportRequest;
import com.demo.admissionportal.dto.request.report.comment_report.UpdateCommentReportRequest;
import com.demo.admissionportal.dto.response.report.comment_report.CommentReportResponse;
import com.demo.admissionportal.dto.response.report.comment_report.ListAllCommentReportResponse;
import com.demo.admissionportal.dto.response.report.comment_report.UpdateCommentReportResponseDTO;
import com.demo.admissionportal.dto.response.report.post_report.FindAllReportsCompletedResponse;
import com.demo.admissionportal.dto.response.report.post_report.ReportPostResponse;
import com.demo.admissionportal.dto.request.report.post_report.CreatePostReportRequest;
import com.demo.admissionportal.dto.request.report.post_report.UpdatePostReportRequest;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.dto.entity.report.post_report.FindAllReportsWithPostDTO;
import com.demo.admissionportal.dto.response.report.post_report.ListAllPostReportResponse;
import com.demo.admissionportal.dto.response.report.post_report.UpdatePostReportResponseDTO;
import com.demo.admissionportal.entity.*;
import com.demo.admissionportal.entity.sub_entity.CommentReport;
import com.demo.admissionportal.entity.sub_entity.PostReport;
import com.demo.admissionportal.repository.*;
import com.demo.admissionportal.repository.sub_repository.CommentReportRepository;
import com.demo.admissionportal.repository.sub_repository.PostReportRepository;
import com.demo.admissionportal.service.ReportService;
import com.demo.admissionportal.util.impl.NameUtils;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
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
    private final UserInfoRepository userInfoRepository;
    private final NameUtils nameUtils;
    private final StaffInfoRepository staffInfoRepository;
    private final ModelMapper modelMapper;
    private final CommentRepository commentRepository;
    private final CommentReportRepository commentReportRepository;

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

    private String generateTicketId() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    @Override
    public ResponseData<ReportPostResponse> getPostReportById(Integer reportId, Authentication authentication) {
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

        ActionerDTO actioner = getUserDetails(report.getCreate_by());

        ReportPostResponse responseDTO = modelMapper.map(report, ReportPostResponse.class);
        responseDTO.setCreateBy(actioner);
        responseDTO.setPostId(post.getId());
        responseDTO.setTitle(post.getTitle());
        responseDTO.setPostCreateBy(post.getCreateBy());
        responseDTO.setPostCreateTime(post.getCreateTime());

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
            if (reportOpt.get().getStatus().equals(ReportStatus.COMPLETED)){
                return new ResponseData<>(ResponseCode.C204.getCode(), "Báo cáo đã được xử lý.");
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

            ActionerDTO actioner = getUserDetails(report.getCreate_by());

            UpdatePostReportResponseDTO responseDTO = new UpdatePostReportResponseDTO(
                    report.getId(),
                    report.getTicket_id(),
                    actioner,
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
    public ResponseData<Page<ListAllPostReportResponse>> findAllPostReports(Pageable pageable, Authentication authentication,
                                                                            Integer reportId, String ticketId, Integer createBy,
                                                                            String content, ReportType reportType, ReportStatus status) {
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

            Page<FindAllReportsWithPostDTO> reportPage = reportRepository.findAllReportsWithPost(pageable, reportId, ticketId, createBy, content, status, reportType);

            Page<ListAllPostReportResponse> responseDTOPage = reportPage.map(report -> {
                ActionerDTO actioner = null;
                if (report.getCreateBy() != null && report.getCreateBy().getId() != null) {
                    actioner = getUserDetails(report.getCreateBy().getId());
                }
                ListAllPostReportResponse responseDTO = modelMapper.map(report, ListAllPostReportResponse.class);
                responseDTO.setCreateBy(actioner);
                return responseDTO;
            });

            return new ResponseData<>(ResponseCode.C200.getCode(), "Danh sách báo cáo bài viết được tìm thấy", responseDTOPage);
        } catch (Exception e) {
            log.error("Error while fetching post reports", e);
            return new ResponseData<>(ResponseCode.C207.getCode(), "Đã xảy ra lỗi khi tìm kiếm báo cáo bài viết");
        }
    }

    @Override
    public ResponseData<Page<FindAllReportsCompletedResponse>> findAllCompletedPostReports(Pageable pageable, Authentication authentication,
                                                                                           Integer reportId, String ticketId, Integer createBy,
                                                                                           ReportType reportType, ReportStatus status
    ) {
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

            Page<FindAllReportsCompletedDTO> reportPage = reportRepository.findAllReportsWithPostByStatus(pageable, reportId, ticketId, createBy, ReportStatus.COMPLETED, reportType);

            Page<FindAllReportsCompletedResponse> responseDTOPage = reportPage.map(report -> {
                ActionerDTO actioner = null;
                if (report.getCreateBy() != null) {
                    actioner = getUserDetails(report.getCreateBy().getId());
                }
                FindAllReportsCompletedResponse responseDTO = modelMapper.map(report, FindAllReportsCompletedResponse.class);
                responseDTO.setCreateBy(actioner);
                return responseDTO;
            });

            return new ResponseData<>(ResponseCode.C200.getCode(), "Danh sách báo cáo đã xử lý được tìm thấy", responseDTOPage);
        } catch (Exception e) {
            log.error("Error while fetching completed post reports", e);
            return new ResponseData<>(ResponseCode.C207.getCode(), "Đã xảy ra lỗi khi tìm kiếm báo cáo đã xử lý");
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
            if (existComment.isEmpty()) {
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
    public ResponseData<Page<ListAllCommentReportResponse>> findAllCommentReports(Pageable pageable, Authentication authentication,
                                                                                  Integer reportId, String ticketId, Integer createBy,
                                                                                  String content, ReportType reportType, ReportStatus status) {
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

            Page<FindAllCommentReportsDTO> reportPage = reportRepository.findAllCommentReports(pageable, reportId, ticketId, createBy, content, status, reportType);
            Page<ListAllCommentReportResponse> responseDTOPage = reportPage.map(report -> modelMapper.map(report, ListAllCommentReportResponse.class));

            return new ResponseData<>(ResponseCode.C200.getCode(), "Danh sách báo cáo bình luận được tìm thấy", responseDTOPage);
        } catch (Exception e) {
            log.error("Error while fetching comment reports", e);
            return new ResponseData<>(ResponseCode.C207.getCode(), "Đã xảy ra lỗi khi tìm kiếm báo cáo bình luận");
        }
    }


    @Override
    public ResponseData<CommentReportResponse> getCommentReportById(Integer reportId, Authentication authentication) {
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

        Optional<CommentReport> commentReportOpt = commentReportRepository.findAll()
                .stream()
                .filter(cr -> cr.getReportId().equals(reportId))
                .findFirst();
        if (commentReportOpt.isEmpty()) {
            return new ResponseData<>(ResponseCode.C203.getCode(), "Báo cáo về bình luận không được tìm thấy !");
        }
        CommentReport commentReport = commentReportOpt.get();
        Integer commentId = commentReport.getCommentId();

        Optional<Comment> commentOpt = commentRepository.findById(commentId);
        if (commentOpt.isEmpty()) {
            return new ResponseData<>(ResponseCode.C203.getCode(), "Bình luận không được tìm thấy !");
        }
        Comment comment = commentOpt.get();

        ActionerDTO actioner = getUserDetails(report.getCreate_by());

        CommentReportResponse responseDTO = modelMapper.map(report, CommentReportResponse.class);
        responseDTO.setCreateBy(actioner);
        responseDTO.setCommentId(comment.getId());
        responseDTO.setContent(comment.getContent());
        responseDTO.setCommentCreateBy(comment.getCommenter_id());
        responseDTO.setCommentType(comment.getComment_type().name());
        responseDTO.setCommentCreateTime(comment.getCreate_time());

        return new ResponseData<>(ResponseCode.C200.getCode(), "Thông tin báo cáo về bình luận được tìm thấy !", responseDTO);
    }

    @Override
    @Transactional
    public ResponseData<UpdateCommentReportResponseDTO> updateCommentReport(Integer reportId, UpdateCommentReportRequest request, Authentication authentication) {
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
            if (reportOpt.get().getStatus().equals(ReportStatus.COMPLETED)){
                return new ResponseData<>(ResponseCode.C204.getCode(), "Báo cáo đã được xử lý.");
            }
            Report report = reportOpt.get();

            commentReport.setReportAction(request.getReportAction());
            commentReportRepository.save(commentReport);

            report.setUpdate_time(new Date());
            report.setUpdate_by(staffId);
            report.setComplete_time(new Date());
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

            UpdateCommentReportResponseDTO responseDTO = new UpdateCommentReportResponseDTO(
                    report.getId(),
                    report.getTicket_id(),
                    actioner,
                    report.getCreate_time(),
                    report.getReport_type().toString(),
                    report.getContent(),
                    comment.getId(),
                    comment.getContent(),
                    comment.getCommenter_id(),
                    comment.getCreate_time(),
                    report.getUpdate_time(),
                    report.getUpdate_by(),
                    report.getComplete_time(),
                    report.getComplete_by(),
                    commentReport.getReportAction(),
                    report.getResponse(),
                    report.getStatus()
            );
            return new ResponseData<>(ResponseCode.C200.getCode(), "Update successful", responseDTO);
        } catch (Exception e) {
            log.error("Error while updating comment report", e);
            return new ResponseData<>(ResponseCode.C207.getCode(), "Đã xảy ra lỗi khi cập nhật báo cáo bình luận");
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
