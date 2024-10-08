package com.demo.admissionportal.service.impl;

import com.demo.admissionportal.constants.FavoriteStatus;
import com.demo.admissionportal.constants.ResponseCode;
import com.demo.admissionportal.constants.Role;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.dto.response.favorite.FavoriteResponseDTO;
import com.demo.admissionportal.dto.response.favorite.FavoriteUsersListResponseDTO;
import com.demo.admissionportal.dto.response.favorite.TotalCountResponseDTO;
import com.demo.admissionportal.dto.response.favorite.UserFavoriteResponseDTO;
import com.demo.admissionportal.entity.*;
import com.demo.admissionportal.entity.sub_entity.id.UserFavoriteId;
import com.demo.admissionportal.repository.UniversityInfoRepository;
import com.demo.admissionportal.repository.UserInfoRepository;
import com.demo.admissionportal.repository.UserRepository;
import com.demo.admissionportal.repository.sub_repository.FollowRepository;
import com.demo.admissionportal.repository.sub_repository.FollowUniversityMajorRepository;
import com.demo.admissionportal.repository.sub_repository.UserFavoriteRepository;
import com.demo.admissionportal.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The type Favorite service.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class FavoriteServiceImpl implements FavoriteService {
    private final UserInfoRepository userInfoRepository;
    private final UserFavoriteRepository userFavoriteRepository;
    private final UserRepository userRepository;
    private final UniversityInfoRepository universityInfoRepository;
    private final FollowRepository followRepository;
    private final FollowUniversityMajorRepository followUniversityMajorRepository;

    @Override
    public ResponseData<FavoriteResponseDTO> createFavorite(Integer universityID) {
        try {
            UserFavorite result;
            FavoriteResponseDTO favoriteResponseDTO = new FavoriteResponseDTO();
            Integer userId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
            if (universityID == null) {
                return new ResponseData<>(ResponseCode.C205.getCode(), "universityID null");
            }
            UserFavorite checkExistedFavorite = userFavoriteRepository.findByUserIdAndUniversityId(userId, universityID);
            if (checkExistedFavorite == null) {
                UserFavorite userFavorite = new UserFavorite();
                UserFavoriteId userFavoriteId = new UserFavoriteId();
                userFavoriteId.setUserId(userId);
                userFavoriteId.setUniversityId(universityID);

                userFavorite.setId(userFavoriteId);
                User user = userRepository.findUserById(userId);
                userFavorite.setUser(user);
                UniversityInfo universityUser = universityInfoRepository.findUniversityInfoById(universityID);
                userFavorite.setUniversity(universityUser);
                userFavorite.setCreateTime(new Date());
                userFavorite.setStatus(FavoriteStatus.FOLLOW);
                result = userFavoriteRepository.save(userFavorite);
            } else {
                if (checkExistedFavorite.getStatus().equals(FavoriteStatus.FOLLOW)) {
                    checkExistedFavorite.setStatus(FavoriteStatus.UNFOLLOW);
                } else {
                    checkExistedFavorite.setStatus(FavoriteStatus.FOLLOW);
                }
                checkExistedFavorite.setUpdateTime(new Date());
                result = userFavoriteRepository.save(checkExistedFavorite);
            }
            favoriteResponseDTO.setCurrentStatus(result.getStatus().name);
            return new ResponseData<>(ResponseCode.C200.getCode(), "Cập nhật favorite thành công", favoriteResponseDTO);
        } catch (Exception ex) {
            log.error("Error while create favorite : {}", ex.getMessage());
            return new ResponseData<>(ResponseCode.C207.getCode(), "Lỗi khi tạo favorite");
        }
    }

    @Override
    public ResponseData<FavoriteResponseDTO> getFavorite(Integer universityID) {
        Integer userId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        UserFavorite favorite = userFavoriteRepository.findByUserIdAndUniversityId(userId, universityID);
        FavoriteResponseDTO favoriteResponseDTO = new FavoriteResponseDTO();
        favoriteResponseDTO.setCurrentStatus(favorite.getStatus().name);
        return new ResponseData<>(ResponseCode.C200.getCode(), "Lấy favorite thành công", favoriteResponseDTO);
    }

    @Override
    public ResponseData<TotalCountResponseDTO> getTotalFavorite(Integer universityID) {
        if (universityID == null) {
            return new ResponseData<>(ResponseCode.C205.getCode(), "universityID null");
        }
        Integer totalCount = userFavoriteRepository.totalFavoriteCount(universityID).orElse(0);
        TotalCountResponseDTO totalCountResponseDTO = new TotalCountResponseDTO();
        totalCountResponseDTO.setTotalCount(totalCount);
        return new ResponseData<>(ResponseCode.C200.getCode(), "Lấy số lượng favorite thành công", totalCountResponseDTO);
    }

    @Override
    public ResponseData<List<UserFavoriteResponseDTO>> getListFavorite() {
        try {
            Integer userId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
            List<UserFavorite> listFavoriteUser = userFavoriteRepository.findByUserId(userId);
            List<UserFavoriteResponseDTO> result = listFavoriteUser.stream()
                    .map(this::mapUserFavorite)
                    .collect(Collectors.toList());
            return new ResponseData<>(ResponseCode.C200.getCode(), "Lấy danh sách favorite thành công", result);
        } catch (Exception ex) {
            log.error("Error while get list favorite");
            return new ResponseData<>(ResponseCode.C207.getCode(), "Lấy danh sách favorite thất bại", null);
        }
    }

    private UserFavoriteResponseDTO mapUserFavorite(UserFavorite userFavorite) {
        UserFavoriteResponseDTO result = new UserFavoriteResponseDTO();
        result.setUniversityId(userFavorite.getUniversity().getId());
        result.setDateFavorite(userFavorite.getCreateTime());
        result.setUniversityName(userFavorite.getUniversity().getName());
        User university = userRepository.findUserById(userFavorite.getUniversity().getId());
        result.setAvatar(university.getAvatar());
        return result;
    }

    @Override
    public ResponseData<List<FavoriteUsersListResponseDTO>> getFavoriteListUsers() {
        try {
            Role role = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getRole();
            Integer userId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
            List<FavoriteUsersListResponseDTO> responseDTOS = new ArrayList<>();
            switch (role) {
                case UNIVERSITY -> responseDTOS = findListUsersFavorite(userId);
                case CONSULTANT -> {
                    UniversityInfo university = universityInfoRepository.findUniversityInfoByConsultantId(userId);
                    responseDTOS = findListUsersFavorite(university.getId());
                }
            }
            if (responseDTOS == null) {
                return new ResponseData<>(ResponseCode.C207.getCode(), "Lấy danh sách users follow university thất bại", null);
            }
            return new ResponseData<>(ResponseCode.C200.getCode(), "Lấy danh sách users follow university thành công", responseDTOS);
        } catch (Exception ex) {
            log.error("Error while get list user follow university major by university");
            return new ResponseData<>(ResponseCode.C207.getCode(), "Lấy danh sách users follow university thất bại", null);
        }
    }

    private List<FavoriteUsersListResponseDTO> findListUsersFavorite(Integer universityId) {
        try {
            List<UserFavorite> listUsersFollow = userFavoriteRepository.listUsersFavorite(universityId);
            return listUsersFollow.stream()
                    .map(this::mapToUser)
                    .collect(Collectors.toList());
        } catch (Exception ex) {
            log.error("Error while findListUsersFollow");
        }
        return null;
    }

    private FavoriteUsersListResponseDTO mapToUser(UserFavorite userFavorite) {
        UserInfo userInfo = userInfoRepository.findUserInfoById(userFavorite.getUser().getId());
        List<UserFollowUniversityMajor> userFollowUniversityMajor = followUniversityMajorRepository.findFCMToken(userFavorite.getUser().getId()).orElse(null);
        return FavoriteUsersListResponseDTO.builder()
                .userId(userFavorite.getUser().getId())
                .email(userFavorite.getUser().getEmail())
                .fullName(userInfo.getFirstName() + " " + userInfo.getMiddleName() + " " + userInfo.getLastName())
                .avatar(userFavorite.getUser().getAvatar())
                .fcmToken(userFollowUniversityMajor.get(0).getFcmToken())
                .build();
    }
}
