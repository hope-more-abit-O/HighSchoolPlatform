package com.demo.admissionportal.service.impl;

import com.demo.admissionportal.constants.CommentStatus;
import com.demo.admissionportal.constants.CommentType;
import com.demo.admissionportal.constants.ResponseCode;
import com.demo.admissionportal.dto.request.comment.CommentRequestDTO;
import com.demo.admissionportal.dto.request.comment.ReplyCommentRequestDTO;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.dto.response.comment.CommentDetailResponseDTO;
import com.demo.admissionportal.dto.response.comment.CommentResponseDTO;
import com.demo.admissionportal.dto.response.comment.ReplyCommentDetailResponseDTO;
import com.demo.admissionportal.dto.response.comment.UserDetailCommentResponseDTO;
import com.demo.admissionportal.entity.Comment;
import com.demo.admissionportal.entity.StaffInfo;
import com.demo.admissionportal.entity.User;
import com.demo.admissionportal.entity.UserInfo;
import com.demo.admissionportal.repository.CommentRepository;
import com.demo.admissionportal.repository.StaffInfoRepository;
import com.demo.admissionportal.repository.UserInfoRepository;
import com.demo.admissionportal.repository.UserRepository;
import com.demo.admissionportal.service.CommentService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The type Comment service.
 */
@Service
@AllArgsConstructor
@Slf4j
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final UserInfoRepository userInfoRepository;
    private final UserRepository userRepository;
    private final StaffInfoRepository staffInfoRepository;

    @Override
    public ResponseData<?> createComment(CommentRequestDTO requestDTO) {
        try {
            if (requestDTO == null) {
                return new ResponseData<>(ResponseCode.C205.getCode(), "Sai request");
            }
            Comment comment = getComment(requestDTO);
            Comment savedComment = commentRepository.save(comment);
            if (savedComment == null) {
                throw new Exception("Lưu comment thất bại");
            }
            return new ResponseData<>(ResponseCode.C206.getCode(), "Lưu comment thành công", savedComment);
        } catch (Exception ex) {
            log.error("xuất hiện lỗi khi lưu comment bài viết: {}", ex.getMessage());
            return new ResponseData<>(ResponseCode.C207.getCode(), "Xuất hiện lỗi khi lưu comment bài viết:", ex.getMessage());
        }
    }

    private static Comment getComment(CommentRequestDTO requestDTO) {
        Integer userId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        Comment comment = new Comment();
        comment.setPostId(requestDTO.getPost_id());
        comment.setCommenter_id(userId);
        comment.setContent(requestDTO.getContent());
        comment.setCreate_time(new Date());
        comment.setComment_type(CommentType.ROOT);
        comment.setComment_status(CommentStatus.ACTIVE);
        return comment;
    }

    @Override
    public ResponseData<?> createReplyComment(ReplyCommentRequestDTO requestDTO) {
        try {
            if (requestDTO == null) {
                return new ResponseData<>(ResponseCode.C205.getCode(), "Sai request");
            }
            Comment replyComment = getReplyComment(requestDTO);
            Comment savedReplyComment = commentRepository.save(replyComment);
            if (savedReplyComment == null) {
                throw new Exception("Lưu reply comment thất bại");
            }
            return new ResponseData<>(ResponseCode.C206.getCode(), "Lưu reply comment thành công", savedReplyComment);
        } catch (Exception ex) {
            log.error("xuất hiện lỗi khi lưu reply comment bài viết: {}", ex.getMessage());
            return new ResponseData<>(ResponseCode.C207.getCode(), "Xuất hiện lỗi khi lưu reply comment bài viết:", ex.getMessage());
        }
    }

    private static Comment getReplyComment(ReplyCommentRequestDTO requestDTO) {
        Integer accId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        Comment replyComment = new Comment();
        replyComment.setPostId(requestDTO.getPost_id());
        replyComment.setCommenter_id(accId);
        replyComment.setContent(requestDTO.getContent());
        replyComment.setCommentParentId(requestDTO.getComment_parent_id());
        replyComment.setCreate_time(new Date());
        replyComment.setComment_type(CommentType.CHILD);
        replyComment.setComment_status(CommentStatus.ACTIVE);
        return replyComment;
    }

    @Override
    public ResponseData<List<CommentResponseDTO>> getCommentsByPostId(Integer postId) {
        try {
            if (postId == null) {
                return new ResponseData<>(ResponseCode.C205.getCode(), "Sai request");
            }
            List<CommentResponseDTO> responseDTOS = getCommentFromPostId(postId);
            return new ResponseData<>(ResponseCode.C200.getCode(), "Lấy comment từ bài post thành công", responseDTOS);
        } catch (Exception ex) {
            log.error("Error when get comment in post: {}", ex.getMessage());
            return new ResponseData<>(ResponseCode.C201.getCode(), "Lấy comment từ bài post thất bại", null);
        }
    }

    @Override
    public List<CommentResponseDTO> getCommentFromPostId(Integer postId) {
        List<Comment> comments = commentRepository.findByPostId(postId);
        List<CommentResponseDTO> responseDTOS = new ArrayList<>();
        List<Comment> commentsWithNoParentId = comments.stream()
                .filter(comment -> comment.getCommentParentId() == null)
                .toList();
        // Get root comments
        for (Comment comment : commentsWithNoParentId) {
            CommentResponseDTO commentResponseDTO = new CommentResponseDTO();
            // Get child comments
            List<Comment> commentParentId = commentRepository.findByPostIdAndCommentParentId(postId, comment.getId());
            List<Comment> commentChildren = new ArrayList<>(commentParentId);
            commentResponseDTO.setComment(mapToCommentDetailResponse(comment, commentChildren));
            responseDTOS.add(commentResponseDTO);
        }
        return responseDTOS;
    }

    private CommentDetailResponseDTO mapToCommentDetailResponse(Comment comment, List<Comment> commentChildren) {
        return CommentDetailResponseDTO.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .postId(comment.getPostId())
                .user_id(mapToUserDetailResponse(comment.getCommenter_id()))
                .create_time(comment.getCreate_time())
                .comment_type(comment.getComment_type())
                .replyComment(mapToReplyCommentDetailResponseList(commentChildren))
                .build();
    }

    private UserDetailCommentResponseDTO mapToUserDetailResponse(Integer commenterId) {
        UserInfo userInfo = userInfoRepository.findUserInfoById(commenterId);
        User user = userRepository.findUserById(commenterId);
        StaffInfo staffInfo = staffInfoRepository.findStaffInfoById(commenterId);
        String fullName = null;
        if (userInfo != null) {
            fullName = userInfo.getFirstName() + " " + userInfo.getMiddleName() + " " + userInfo.getLastName();
        } else if (staffInfo != null) {
            fullName = staffInfo.getFirstName() + " " + staffInfo.getMiddleName() + " " + staffInfo.getLastName();
        }
        return UserDetailCommentResponseDTO.builder()
                .id(user.getId())
                .fullName(fullName)
                .avatar(user.getAvatar())
                .role(user.getRole().name())
                .build();
    }

    private List<ReplyCommentDetailResponseDTO> mapToReplyCommentDetailResponseList(List<Comment> comments) {
        return comments.stream()
                .map(this::mapToReplyCommentDetailResponse)
                .collect(Collectors.toList());
    }

    private ReplyCommentDetailResponseDTO mapToReplyCommentDetailResponse(Comment comment) {
        return ReplyCommentDetailResponseDTO.builder()
                .replayComment_id(comment.getId())
                .content(comment.getContent())
                .create_time(comment.getCreate_time())
                .user_id(mapToUserDetailResponse(comment.getCommenter_id()))
                .comment_type(comment.getComment_type())
                .build();
    }
}
