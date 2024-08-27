package com.demo.admissionportal.service.impl;

import com.demo.admissionportal.constants.LikeStatus;
import com.demo.admissionportal.constants.ResponseCode;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.dto.response.like.LikeResponseDTO;
import com.demo.admissionportal.dto.response.like.TotalLikeResponseDTO;
import com.demo.admissionportal.entity.Post;
import com.demo.admissionportal.entity.User;
import com.demo.admissionportal.entity.UserLike;
import com.demo.admissionportal.entity.sub_entity.id.UserLikeId;
import com.demo.admissionportal.repository.PostRepository;
import com.demo.admissionportal.repository.UserLikeRepository;
import com.demo.admissionportal.repository.UserRepository;
import com.demo.admissionportal.service.UserLikeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The type User like service.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserLikeServiceImpl implements UserLikeService {
    private final UserLikeRepository userLikeRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Override
    public ResponseData<LikeResponseDTO> createLike(Integer postID) {
        try {
            Integer userId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
            UserLike checkExisted = userLikeRepository.findByUserIdAndPostId(userId, postID);
            UserLike result;
            LikeResponseDTO responseDTO = new LikeResponseDTO();
            if (postID == null) {
                return new ResponseData<>(ResponseCode.C205.getCode(), "postID null");
            }
            if (checkExisted == null) {
                UserLikeId userLikeId = new UserLikeId();
                userLikeId.setPostId(postID);
                userLikeId.setUserId(userId);

                UserLike userLike = new UserLike();
                userLike.setId(userLikeId);
                User user = userRepository.findUserById(userId);
                userLike.setUser(user);
                Post post = postRepository.findPostWithActive(postID);
                userLike.setPost(post);
                userLike.setCreateTime(new Date());
                userLike.setStatus(LikeStatus.LIKE);
                result = userLikeRepository.save(userLike);
                post.setLike(post.getLike() + 1);
                postRepository.save(post);
            } else {
                Post post = postRepository.findPostWithActive(postID);
                if (checkExisted.getStatus() == LikeStatus.LIKE) {
                    checkExisted.setStatus(LikeStatus.UNLIKE);
                    post.setLike(post.getLike() - 1);
                } else {
                    checkExisted.setStatus(LikeStatus.LIKE);
                    post.setLike(post.getLike() + 1);
                }
                postRepository.save(post);
                checkExisted.setUpdateTime(new Date());
                result = userLikeRepository.save(checkExisted);
            }
            responseDTO.setCurrentStatus(result.getStatus().name);
            return new ResponseData<>(ResponseCode.C200.getCode(), "Cập nhật favorite thành công", responseDTO);
        } catch (Exception ex) {
            log.error("Error while create favorite : {}", ex.getMessage());
            return new ResponseData<>(ResponseCode.C207.getCode(), "Lỗi khi tạo like");
        }
    }

    @Override
    public ResponseData<TotalLikeResponseDTO> getTotalLike(Integer postId) {
        Integer userId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        if (postId == null) {
            return new ResponseData<>(ResponseCode.C205.getCode(), "universityID null");
        }
        UserLike like = userLikeRepository.findByUserIdAndPostId(userId, postId);
        Integer totalCount = userLikeRepository.totalLikeCountPostId(postId).orElse(0);
        TotalLikeResponseDTO totalLikeResponseDTO = new TotalLikeResponseDTO();
        totalLikeResponseDTO.setCurrentStatus(like != null ? like.getStatus().name : LikeStatus.UNLIKE.name);
        totalLikeResponseDTO.setTotal(totalCount);
        return new ResponseData<>(ResponseCode.C200.getCode(), "Lấy số lượng favorite thành công", totalLikeResponseDTO);
    }

    @Override
    public ResponseData<List<LikeResponseDTO>> getLikeByUniversity(Integer universityID) {
        Integer userId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        List<UserLike> like = userLikeRepository.findByUserIdAndUniversityId(userId, universityID);
        if (like == null) {
            return new ResponseData<>(ResponseCode.C203.getCode(), "Không tìm thấy like với uniId: " + universityID);
        }
        List<LikeResponseDTO> likeResponseDTOS = like.stream()
                .map(this::mapToLike)
                .collect(Collectors.toList());

        return new ResponseData<>(ResponseCode.C200.getCode(), "Lấy like thành công", likeResponseDTOS);
    }

    private LikeResponseDTO mapToLike(UserLike userLike) {
        LikeResponseDTO likeResponseDTO = new LikeResponseDTO();
        likeResponseDTO.setPostId(userLike.getPost().getId());
        likeResponseDTO.setCurrentStatus(userLike.getStatus().name);
        return likeResponseDTO;
    }
}
