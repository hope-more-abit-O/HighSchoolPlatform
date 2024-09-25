package com.demo.admissionportal.service.impl;

import com.demo.admissionportal.constants.AdmissionStatus;
import com.demo.admissionportal.constants.FavoriteStatus;
import com.demo.admissionportal.constants.ResponseCode;
import com.demo.admissionportal.constants.Role;
import com.demo.admissionportal.dto.request.follow.UpdateFollowUniRequestDTO;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.dto.response.follow.*;
import com.demo.admissionportal.entity.*;
import com.demo.admissionportal.entity.admission.Admission;
import com.demo.admissionportal.entity.admission.AdmissionTrainingProgram;
import com.demo.admissionportal.entity.admission.AdmissionTrainingProgramSubjectGroup;
import com.demo.admissionportal.entity.sub_entity.id.UserFollowMajorId;
import com.demo.admissionportal.entity.sub_entity.id.UserFollowUniversityMajorId;
import com.demo.admissionportal.repository.*;
import com.demo.admissionportal.repository.admission.AdmissionRepository;
import com.demo.admissionportal.repository.admission.AdmissionTrainingProgramRepository;
import com.demo.admissionportal.repository.admission.AdmissionTrainingProgramSubjectGroupRepository;
import com.demo.admissionportal.repository.sub_repository.FollowRepository;
import com.demo.admissionportal.repository.sub_repository.FollowUniversityMajorRepository;
import com.demo.admissionportal.service.FollowService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;
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
    private final AdmissionTrainingProgramRepository admissionTrainingProgramRepository;
    private final UserInfoRepository userInfoRepository;
    private final UniversityCampusRepository universityCampusRepository;
    private final ProvinceRepository provinceRepository;
    private final AdmissionRepository admissionRepository;
    private final AdmissionTrainingProgramSubjectGroupRepository admissionTrainingProgramSubjectGroupRepository;
    private final SubjectGroupRepository subjectGroupRepository;

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

    @Transactional(rollbackOn = Exception.class)
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
                List<UserFollowUniversityMajor> indexOfFollow = followUniversityMajorRepository.findByUserIdV2(userId);
                userFollowUniversityMajor.setUniversityMajor(universityTrainingProgram);
                userFollowUniversityMajor.setCreateTime(new Date());
                userFollowUniversityMajor.setStatus(FavoriteStatus.FOLLOW);
                userFollowUniversityMajor.setIndexOfFollow(indexOfFollow.size() + 1);
                result = followUniversityMajorRepository.save(userFollowUniversityMajor);
                followUniMajorResponseDTO.setCurrentStatus(result.getStatus().name);
            } else {
                followUniversityMajorRepository.delete(checkExistedFollowUniMajor);
                updateAllIndex(checkExistedFollowUniMajor, userId);
                followUniMajorResponseDTO.setCurrentStatus(FavoriteStatus.UNFOLLOW.name);
            }
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
    public ResponseData<List<UserFollowUniversityMajorResponseDTO>> getListFollowUniMajor(Integer year) {
        try {
            Integer userId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
            List<UserFollowUniversityMajor> listFollowUniMajor = followUniversityMajorRepository.findByUserId(userId).stream().filter(userFollowUniversityMajor -> userFollowUniversityMajor.getStatus().equals(FavoriteStatus.FOLLOW)).toList();
            List<UserFollowUniversityMajorResponseDTO> result = listFollowUniMajor.stream()
                    .map(p -> mapUserFollowUniversityMajor(p, year))
                    .sorted(Comparator.comparing(UserFollowUniversityMajorResponseDTO::getIndex))
                    .collect(Collectors.toList());
            return new ResponseData<>(ResponseCode.C200.getCode(), "Lấy danh sách follow university major thành công", result);
        } catch (Exception ex) {
            log.error("Error while get list follow univerisity major");
            return new ResponseData<>(ResponseCode.C207.getCode(), "Lấy danh sách follow university major thất bại", null);
        }
    }

    private UserFollowUniversityMajorResponseDTO mapUserFollowUniversityMajor(UserFollowUniversityMajor userFollowUniversityMajor, Integer year) {
        UniversityTrainingProgram universityTrainingProgram = universityTrainingProgramRepository.findById(userFollowUniversityMajor.getId().getUniversityMajor()).orElse(null);
        Major major = majorRepository.findById(universityTrainingProgram.getMajorId()).orElse(null);
        User user = userRepository.findUserById(userFollowUniversityMajor.getUniversityMajor().getUniversityId());
        UniversityInfo userUniversityInfo = universityInfoRepository.findUniversityInfoById(userFollowUniversityMajor.getUniversityMajor().getUniversityId());
        UniversityCampus universityCampus = universityCampusRepository.findHeadQuartersCampusByUniversityId(userUniversityInfo.getId());
        Province province = provinceRepository.findProvinceById(universityCampus.getProvinceId());
        Admission admission = admissionRepository.findByUniversityIdAndYearAndAdmissionStatus(userUniversityInfo.getId(), year, AdmissionStatus.ACTIVE).orElse(null);
        AdmissionTrainingProgram admissionTrainingProgram = admissionTrainingProgramRepository.findById(admission.getId()).orElse(null);
        Collection<Integer> admissionTrainingProgramIds = Collections.singleton(admissionTrainingProgram.getId());
        List<AdmissionTrainingProgramSubjectGroup> admissionTrainingProgramSubjectGroup = admissionTrainingProgramSubjectGroupRepository.findById_AdmissionTrainingProgramIdIn(admissionTrainingProgramIds);
        String subjectGroups = admissionTrainingProgramSubjectGroup.stream()
                .map(this::mapToSubjectGroup)
                .collect(Collectors.joining(", "));
        UserFollowUniversityMajorResponseDTO response = new UserFollowUniversityMajorResponseDTO();
        response.setIndex(userFollowUniversityMajor.getIndexOfFollow());
        response.setUniversityId(user.getId());
        response.setMajorName(major.getName());
        response.setMajorCode(major.getCode());
        response.setUniversityMajorId(userFollowUniversityMajor.getUniversityMajor().getId());
        response.setUniversityName(userUniversityInfo.getName());
        response.setUniversityCode(userUniversityInfo.getCode());
        response.setUniversityType(userUniversityInfo.getType().name);
        response.setRegion(province.getRegion().name);
        response.setLanguage(userFollowUniversityMajor.getUniversityMajor().getLanguage());
        response.setTraining_specific(userFollowUniversityMajor.getUniversityMajor().getTrainingSpecific());
        response.setCreateTime(userFollowUniversityMajor.getCreateTime());
        response.setAvatar(user.getAvatar());
        response.setSubjectGroups(subjectGroups);
        response.setFcmToken(userFollowUniversityMajor.getFcmToken());
        return response;
    }

    private String mapToSubjectGroup(AdmissionTrainingProgramSubjectGroup admissionTrainingProgramSubjectGroup) {
        SubjectGroup subjectGroup = subjectGroupRepository.findById(admissionTrainingProgramSubjectGroup.getId().getSubjectGroupId()).orElse(null);
        return subjectGroup.getName();
    }


    @Override
    public ResponseData<List<UsersFollowMajorResponseDTO>> getListUserFollowMajor() {
        try {
            Role role = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getRole();
            Integer userId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
            List<UsersFollowMajorResponseDTO> responseDTOS = new ArrayList<>();
            switch (role) {
                case UNIVERSITY -> responseDTOS = findListUsersFollow(userId);
                case CONSULTANT -> {
                    UniversityInfo university = universityInfoRepository.findUniversityInfoByConsultantId(userId);
                    responseDTOS = findListUsersFollow(university.getId());
                }
            }
            if (responseDTOS == null) {
                return new ResponseData<>(ResponseCode.C207.getCode(), "Lấy danh sách users follow university major thất bại", null);
            }
            return new ResponseData<>(ResponseCode.C200.getCode(), "Lấy danh sách users follow university major thành công", responseDTOS);
        } catch (Exception ex) {
            log.error("Error while get list user follow university major by university");
            return new ResponseData<>(ResponseCode.C207.getCode(), "Lấy danh sách users follow university major thất bại", null);
        }
    }

    private List<UsersFollowMajorResponseDTO> findListUsersFollow(Integer universityId) {
        try {
            List<UserFollowUniversityMajor> userFollowUniversityMajors = followUniversityMajorRepository.listUsersFollow(universityId);
            return userFollowUniversityMajors.stream()
                    .map(this::mapToUser)
                    .collect(Collectors.toList());
        } catch (Exception ex) {
            log.error("Error while findListUsersFollow");
        }
        return null;
    }

    private UsersFollowMajorResponseDTO mapToUser(UserFollowUniversityMajor userFollowUniversityMajor) {
        Major major = majorRepository.findById(userFollowUniversityMajor.getUniversityMajor().getMajorId()).orElse(null);
        UserInfo userInfo = userInfoRepository.findUserInfoById(userFollowUniversityMajor.getUser().getId());
        UniversityTrainingProgram universityTrainingProgram = universityTrainingProgramRepository.findById(userFollowUniversityMajor.getUniversityMajor().getId()).orElse(null);
        return UsersFollowMajorResponseDTO.builder()
                .userId(userFollowUniversityMajor.getUser().getId())
                .major(major.getName())
                .email(userFollowUniversityMajor.getUser().getEmail())
                .fullName(userInfo.getFirstName() + " " + userInfo.getMiddleName() + " " + userInfo.getLastName())
                .avatar(userFollowUniversityMajor.getUser().getAvatar())
                .universityTrainingProgramId(universityTrainingProgram.getId())
                .majorId(major.getId())
                .majorName(major.getName())
                .language(universityTrainingProgram.getLanguage())
                .training_specific(universityTrainingProgram.getTrainingSpecific())
                .majorCode(major.getCode())
                .fcmToken(userFollowUniversityMajor.getFcmToken())
                .build();
    }

    @Override
    public ResponseData<String> updateIndexFollow(List<UpdateFollowUniRequestDTO> updateFollowUniRequestDTOS) {
        try {
            Integer userId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
            List<UpdateFollowUniRequestDTO> updateFollowUni = updateFollowUniRequestDTOS.stream()
                    .map(e -> mapUpdateFollowUni(e, userId))
                    .collect(Collectors.toList());
            if (updateFollowUni != null) {
                return new ResponseData<>(ResponseCode.C200.getCode(), "Cập nhật thứ tự nguyện vọng thành công");
            }
            throw new Exception();
        } catch (Exception ex) {
            log.error("Error while update index of follow university major");
            return new ResponseData<>(ResponseCode.C207.getCode(), "Cập nhật thứ tự nguyện vọng thất bại", null);
        }
    }

    private UpdateFollowUniRequestDTO mapUpdateFollowUni(UpdateFollowUniRequestDTO updateFollowUniRequestDTO, Integer userId) {
        UserFollowUniversityMajor userFollowUniversityMajor = followUniversityMajorRepository.findByUserIdAndUniversityMajor(userId, updateFollowUniRequestDTO.getUniversityMajorId());
        if (userFollowUniversityMajor != null) {
            userFollowUniversityMajor.setIndexOfFollow(updateFollowUniRequestDTO.getIndexOfFollow());
            followUniversityMajorRepository.save(userFollowUniversityMajor);
        }
        return null;
    }


    private void updateAllIndex(UserFollowUniversityMajor checkExistedFollowUniMajor, Integer userId) {
        // Find high and update it
        List<UserFollowUniversityMajor> userFollowUniversityMajors = followUniversityMajorRepository.findHighIndex(checkExistedFollowUniMajor.getIndexOfFollow(), userId);
        if (userFollowUniversityMajors != null) {
            // Update index
            userFollowUniversityMajors.stream()
                    .peek(e -> e.setIndexOfFollow(e.getIndexOfFollow() - 1))
                    .collect(Collectors.toList());
        }
    }


    @Override
    public ResponseData<List<UsersFollowMajorResponseDTO>> getListUserFollowMajorV2(Integer universityId) {
        try {
            if (universityId == null) {
                return new ResponseData<>(ResponseCode.C207.getCode(), "universityId null");
            }
            List<UsersFollowMajorResponseDTO> responseDTOS = findListUsersFollow(universityId);
            if (responseDTOS == null) {
                return new ResponseData<>(ResponseCode.C207.getCode(), "Lấy danh sách users follow university major v2 thất bại", null);
            }
            return new ResponseData<>(ResponseCode.C200.getCode(), "Lấy danh sách users follow university major v2 thành công", responseDTOS);
        } catch (Exception ex) {
            log.error("Error while get list user follow university major v2 by university");
            return new ResponseData<>(ResponseCode.C207.getCode(), "Lấy danh sách users follow university major v2 thất bại", null);
        }
    }

    @Override
    public ResponseData<String> saveFCMToken(String fcmToken) {
        try {
            Integer userId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
            List<UserFollowUniversityMajor> result = followUniversityMajorRepository.findByUserId(userId);
            result.forEach(p -> mapToFCMToken(fcmToken, userId, p));
            return new ResponseData<>(ResponseCode.C200.getCode(), "Lưu danh sách FCM token thành công");
        } catch (Exception ex) {
            log.error("Error save FCMToken");
            return new ResponseData<>(ResponseCode.C207.getCode(), "Lưu danh sách FCM token thất bại", null);
        }
    }

    private void mapToFCMToken(String fcmToken, Integer userId, UserFollowUniversityMajor userFollowUniversityMajor) {
        UserFollowUniversityMajor userFollow = followUniversityMajorRepository.findByUserIdAndUniversityMajor(userId, userFollowUniversityMajor.getUniversityMajor().getId());
        userFollow.setFcmToken(fcmToken);
        followUniversityMajorRepository.save(userFollow);
    }
}
