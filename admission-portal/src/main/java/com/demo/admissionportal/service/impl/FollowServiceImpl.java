package com.demo.admissionportal.service.impl;

import com.demo.admissionportal.constants.FavoriteStatus;
import com.demo.admissionportal.constants.ResponseCode;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.dto.response.follow.FollowResponseDTO;
import com.demo.admissionportal.dto.response.follow.FollowUniMajorResponseDTO;
import com.demo.admissionportal.dto.response.follow.UserFollowMajorResponseDTO;
import com.demo.admissionportal.dto.response.follow.UserFollowUniversityMajorResponseDTO;
import com.demo.admissionportal.entity.*;
import com.demo.admissionportal.entity.sub_entity.id.UserFollowMajorId;
import com.demo.admissionportal.entity.sub_entity.id.UserFollowUniversityMajorId;
import com.demo.admissionportal.repository.MajorRepository;
import com.demo.admissionportal.repository.UniversityInfoRepository;
import com.demo.admissionportal.repository.UniversityTrainingProgramRepository;
import com.demo.admissionportal.repository.UserRepository;
import com.demo.admissionportal.repository.sub_repository.FollowRepository;
import com.demo.admissionportal.repository.sub_repository.FollowUniversityMajorRepository;
import com.demo.admissionportal.service.FollowService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class FollowServiceImpl implements FollowService {
    private final FollowRepository followRepository;
    private final UserRepository userRepository;
    private final MajorRepository majorRepository;
    private final FollowUniversityMajorRepository followUniversityMajorRepository;
    private final UniversityTrainingProgramRepository universityTrainingProgramRepository;
    private final UniversityInfoRepository universityInfoRepository;

    @Override
    public ResponseData<FollowResponseDTO> createFollowMajor(Integer majorId) {
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

    @Override
    public ResponseData<FollowResponseDTO> getFollowMajor(Integer majorId) {
        Integer userId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        UserFollowMajor follow = followRepository.findByUserIdAndMajorId(userId, majorId);
        FollowResponseDTO followResponseDTO = new FollowResponseDTO();
        followResponseDTO.setCurrentStatus(follow.getStatus().name);
        return new ResponseData<>(ResponseCode.C200.getCode(), "Lấy follow thành công", followResponseDTO);
    }

    @Override
    public ResponseData<List<UserFollowMajorResponseDTO>> getListFollowMajor() {
        try {
            Integer userId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
            List<UserFollowMajor> listFavoriteUser = followRepository.findByUserId(userId).stream().filter(userFollowMajor -> userFollowMajor.getStatus().equals(FavoriteStatus.FOLLOW)).collect(Collectors.toList());
            List<UserFollowMajorResponseDTO> result = listFavoriteUser.stream()
                    .map(this::mapUserFollow)
                    .collect(Collectors.toList());
            return new ResponseData<>(ResponseCode.C200.getCode(), "Lấy danh sách follow major thành công", result);
        } catch (Exception ex) {
            log.error("Error while get list favorite");
            return new ResponseData<>(ResponseCode.C207.getCode(), "Lấy danh sách follow major thất bại", null);
        }
    }

    private UserFollowMajorResponseDTO mapUserFollow(UserFollowMajor userFollowMajor) {
        UserFollowMajorResponseDTO result = new UserFollowMajorResponseDTO();
        result.setMajorId(userFollowMajor.getMajor().getId());
        result.setAvatar(userFollowMajor.getMajor().getName());
        result.setDateFollow(userFollowMajor.getCreateTime());
        return result;
    }

    @Override
    public ResponseData<FollowUniMajorResponseDTO> createFollowUniMajor(Integer universityMajorId) {
        try {
            UserFollowUniversityMajor result;
            FollowUniMajorResponseDTO followUniMajorResponseDTO = new FollowUniMajorResponseDTO();
            Integer userId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
            if (universityMajorId == null) {
                return new ResponseData<>(ResponseCode.C205.getCode(), "majorId null");
            }
            UserFollowUniversityMajor checkExistedFollowUniMajor = followUniversityMajorRepository.findByUserIdAndUniversityMajor(userId, universityMajorId);

            if (checkExistedFollowUniMajor == null) {
                UserFollowUniversityMajor userFollowUniversityMajor = new UserFollowUniversityMajor();
                UserFollowUniversityMajorId userFollowMajorId = new UserFollowUniversityMajorId();
                userFollowMajorId.setUserId(userId);
                userFollowMajorId.setUniversityMajor(universityMajorId);

                userFollowUniversityMajor.setId(userFollowMajorId);
                User user = userRepository.findUserById(userId);
                if (user == null) {
                    throw new IllegalStateException("User not found with id: " + userId);
                }
                userFollowUniversityMajor.setUser(user);

                UniversityTrainingProgram universityTrainingProgram = universityTrainingProgramRepository.findById(universityMajorId).orElse(null);
                userFollowUniversityMajor.setUniversityMajor(universityTrainingProgram);
                userFollowUniversityMajor.setCreateTime(new Date());
                userFollowUniversityMajor.setStatus(FavoriteStatus.FOLLOW);
                result = followUniversityMajorRepository.save(userFollowUniversityMajor);
            } else {
                if (checkExistedFollowUniMajor.getStatus().equals(FavoriteStatus.FOLLOW)) {
                    checkExistedFollowUniMajor.setStatus(FavoriteStatus.UNFOLLOW);
                } else {
                    checkExistedFollowUniMajor.setStatus(FavoriteStatus.FOLLOW);
                }
                checkExistedFollowUniMajor.setUpdateTime(new Date());
                result = followUniversityMajorRepository.save(checkExistedFollowUniMajor);
            }
            followUniMajorResponseDTO.setCurrentStatus(result.getStatus().name);
            return new ResponseData<>(ResponseCode.C200.getCode(), "Cập nhật follow major university thành công", followUniMajorResponseDTO);
        } catch (Exception ex) {
            log.error("Error while creating follow for majorId {}: {}", universityMajorId, ex.getMessage(), ex);
            return new ResponseData<>(ResponseCode.C207.getCode(), "Lỗi khi tạo follow major");
        }
    }

    @Override
    public ResponseData<FollowUniMajorResponseDTO> getFollowUniMajor(Integer universityMajorId) {
        Integer userId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        UserFollowUniversityMajor userFollowMajor = followUniversityMajorRepository.findByUserIdAndUniversityMajor(userId, universityMajorId);
        FollowUniMajorResponseDTO followUniMajorResponseDTO = new FollowUniMajorResponseDTO();
        followUniMajorResponseDTO.setCurrentStatus(userFollowMajor.getStatus().name);
        return new ResponseData<>(ResponseCode.C200.getCode(), "Lấy follow major university thành công", followUniMajorResponseDTO);
    }

    @Override
    public ResponseData<List<UserFollowUniversityMajorResponseDTO>> getListFollowUniMajor() {
        try {
            Integer userId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
            List<UserFollowUniversityMajor> listFollowUniMajor = followUniversityMajorRepository.findByUserId(userId);
            List<UserFollowUniversityMajorResponseDTO> result = listFollowUniMajor.stream()
                    .map(this::mapUserFollowUniversityMajor)
                    .collect(Collectors.toList());
            return new ResponseData<>(ResponseCode.C200.getCode(), "Lấy danh sách follow university major thành công", result);
        } catch (Exception ex) {
            log.error("Error while get list follow univerisity major");
            return new ResponseData<>(ResponseCode.C207.getCode(), "Lấy danh sách follow university major thất bại", null);
        }
    }

    private UserFollowUniversityMajorResponseDTO mapUserFollowUniversityMajor(UserFollowUniversityMajor userFollowUniversityMajor) {
        Major major = majorRepository.findById(userFollowUniversityMajor.getUniversityMajor().getId()).orElse(null);
        User university = userRepository.findUserById(userFollowUniversityMajor.getUniversityMajor().getUniversityId());
        UniversityInfo userUniversityInfo = universityInfoRepository.findUniversityInfoById(userFollowUniversityMajor.getUniversityMajor().getUniversityId());
        UserFollowUniversityMajorResponseDTO response = new UserFollowUniversityMajorResponseDTO();
        response.setMajorName(major.getName());
        response.setUniversityMajorId(userFollowUniversityMajor.getUniversityMajor().getId());
        response.setUniversityName(userUniversityInfo.getName());
        response.setLanguage(userFollowUniversityMajor.getUniversityMajor().getLanguage());
        response.setTraining_specific(userFollowUniversityMajor.getUniversityMajor().getTrainingSpecific());
        response.setCreateTime(userFollowUniversityMajor.getCreateTime());
        response.setAvatar(university.getAvatar());
        return response;
    }
}
