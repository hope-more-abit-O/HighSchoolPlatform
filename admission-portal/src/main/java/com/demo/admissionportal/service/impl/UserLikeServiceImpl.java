package com.demo.admissionportal.service.impl;

import com.demo.admissionportal.constants.LikeStatus;
import com.demo.admissionportal.constants.ResponseCode;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.dto.response.favorite.FavoriteResponseDTO;
import com.demo.admissionportal.dto.response.like.LikeResponseDTO;
import com.demo.admissionportal.entity.Post;
import com.demo.admissionportal.entity.User;
import com.demo.admissionportal.entity.UserFavorite;
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
                Post post = postRepository.findFirstById(postID);
                userLike.setPost(post);
                userLike.setCreateTime(new Date());
                userLike.setStatus(LikeStatus.LIKE);
                result = userLikeRepository.save(userLike);
                post.setLike(post.getLike() + 1);
                postRepository.save(post);
            } else {
                Post post = postRepository.findFirstById(postID);
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
    public ResponseData<LikeResponseDTO> getLike(Integer universityID) {
        Integer userId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        UserLike like = userLikeRepository.findByUserIdAndUniversityId(userId, universityID);
        LikeResponseDTO likeResponseDTO = new LikeResponseDTO();
        likeResponseDTO.setCurrentStatus(like.getStatus().name);
        return new ResponseData<>(ResponseCode.C200.getCode(), "Lấy like thành công", likeResponseDTO);
    }
}
