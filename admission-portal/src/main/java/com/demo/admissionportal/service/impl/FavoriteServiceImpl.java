package com.demo.admissionportal.service.impl;

import com.demo.admissionportal.constants.FavoriteStatus;
import com.demo.admissionportal.constants.ResponseCode;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.dto.response.favorite.FavoriteResponseDTO;
import com.demo.admissionportal.dto.response.favorite.TotalCountResponseDTO;
import com.demo.admissionportal.dto.response.favorite.UserFavoriteResponseDTO;
import com.demo.admissionportal.entity.UniversityInfo;
import com.demo.admissionportal.entity.User;
import com.demo.admissionportal.entity.UserFavorite;
import com.demo.admissionportal.entity.sub_entity.id.UserFavoriteId;
import com.demo.admissionportal.repository.UniversityInfoRepository;
import com.demo.admissionportal.repository.UserRepository;
import com.demo.admissionportal.repository.sub_repository.UserFavoriteRepository;
import com.demo.admissionportal.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

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
    private final UserFavoriteRepository userFavoriteRepository;
    private final UserRepository userRepository;
    private final UniversityInfoRepository universityInfoRepository;

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
}
