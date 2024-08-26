package com.demo.admissionportal.service.impl;

import com.demo.admissionportal.constants.FavoriteStatus;
import com.demo.admissionportal.constants.ResponseCode;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.dto.response.follow.FollowResponseDTO;
import com.demo.admissionportal.entity.Major;
import com.demo.admissionportal.entity.User;
import com.demo.admissionportal.entity.UserFollowMajor;
import com.demo.admissionportal.entity.sub_entity.id.UserFollowMajorId;
import com.demo.admissionportal.repository.MajorRepository;
import com.demo.admissionportal.repository.UserRepository;
import com.demo.admissionportal.repository.sub_repository.FollowRepository;
import com.demo.admissionportal.service.FollowService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Slf4j
@RequiredArgsConstructor
@Service
public class FollowServiceImpl implements FollowService {
    private final FollowRepository followRepository;
    private final UserRepository userRepository;
    private final MajorRepository majorRepository;

    @Override
    public ResponseData<FollowResponseDTO> createFollow(Integer majorId) {
        try {
            UserFollowMajor result;
            FollowResponseDTO followResponseDTO = new FollowResponseDTO();
            Integer userId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();

            if (majorId == null) {
                return new ResponseData<>(ResponseCode.C205.getCode(), "majorId null");
            }

            log.info("Finding existing follow relationship for userId {} and majorId {}", userId, majorId);
            UserFollowMajor checkExistedFollow = followRepository.findByUserIdAndMajorId(userId, majorId);

            if (checkExistedFollow == null) {
                log.info("No existing follow found, creating new one for userId {} and majorId {}", userId, majorId);
                UserFollowMajor userFollowMajor = new UserFollowMajor();
                UserFollowMajorId userFollowMajorId = new UserFollowMajorId();
                userFollowMajorId.setUserId(userId);
                userFollowMajorId.setMajorId(majorId);

                userFollowMajor.setId(userFollowMajorId);
                User user = userRepository.findUserById(userId);
                if (user == null) {
                    throw new IllegalStateException("User not found with id: " + userId);
                }
                userFollowMajor.setUser(user);

                Major major = majorRepository.findById(majorId).orElse(null);
                if (major == null) {
                    throw new IllegalStateException("Major not found with id: " + majorId);
                }
                userFollowMajor.setMajor(major);
                userFollowMajor.setCreateTime(new Date());
                userFollowMajor.setStatus(FavoriteStatus.FOLLOW);
                result = followRepository.save(userFollowMajor);
            } else {
                log.info("Existing follow found, updating status for userId {} and majorId {}", userId, majorId);
                if (checkExistedFollow.getStatus().equals(FavoriteStatus.FOLLOW)) {
                    checkExistedFollow.setStatus(FavoriteStatus.UNFOLLOW);
                } else {
                    checkExistedFollow.setStatus(FavoriteStatus.FOLLOW);
                }
                checkExistedFollow.setUpdateTime(new Date());
                result = followRepository.save(checkExistedFollow);
            }
            followResponseDTO.setCurrentStatus(result.getStatus().name);
            return new ResponseData<>(ResponseCode.C200.getCode(), "Cập nhật follow major thành công", followResponseDTO);
        } catch (Exception ex) {
            log.error("Error while creating follow for majorId {}: {}", majorId, ex.getMessage(), ex);
            return new ResponseData<>(ResponseCode.C207.getCode(), "Lỗi khi tạo follow major");
        }
    }

}
