package com.demo.admissionportal.service.impl;

import com.demo.admissionportal.constants.ResponseCode;
import com.demo.admissionportal.constants.Role;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.dto.response.holland_test.StatisticsPostResponse;
import com.demo.admissionportal.dto.response.statistics.*;
import com.demo.admissionportal.entity.AdsPackage;
import com.demo.admissionportal.entity.UniversityInfo;
import com.demo.admissionportal.entity.UniversityTransaction;
import com.demo.admissionportal.entity.User;
import com.demo.admissionportal.repository.*;
import com.demo.admissionportal.repository.sub_repository.UserFavoriteRepository;
import com.demo.admissionportal.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * The type Statistics service.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {
    private final UniversityTransactionRepository universityTransactionRepository;
    private final UserFavoriteRepository userFavoriteRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final UniversityInfoRepository universityInfoRepository;
    private final PackageRepository packageRepository;

    @Override
    public ResponseData<StatisticsResponse> getStatistics() {
        try {
            Role user = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getRole();
            StatisticsResponse statisticsResponse = switch (user) {
                case ADMIN -> calculatorStatisticsByAdmin();
                case UNIVERSITY -> calculatorStatisticsByUniversity();
                default -> throw new Exception();
            };
            return new ResponseData<>(ResponseCode.C200.getCode(), "Lấy dữ liệu thống kê thành công", statisticsResponse);
        } catch (Exception e) {
            log.error("Failed when get statistics: {} ", e.getMessage());
            return new ResponseData<>(ResponseCode.C207.getCode(), "Lấy dữ liệu thống kê thất bại");
        }
    }

    private StatisticsResponse calculatorStatisticsByUniversity() {
        return null;
    }

    private StatisticsResponse calculatorStatisticsByAdmin() {
        return StatisticsResponse.builder()
                .transaction(calculatorTransactionByAdmin())
                .interact(calculatorInteractResponseByAdmin())
                .account(calculatorAccountResponseByAdmin())
                .post(calculatorPostResponseByAdmin())
                .activityTransaction(calculatorActivityTransaction())
                .build();
    }

    private List<StatisticsTransactionDetailResponse> calculatorActivityTransaction() {
        List<UniversityTransaction> list = universityTransactionRepository.findAll();
        return list.stream()
                .map(this::mapToTransactionDetail)
                .collect(Collectors.toList());
    }

    private StatisticsTransactionDetailResponse mapToTransactionDetail(UniversityTransaction universityTransaction) {
        StatisticsTransactionDetailResponse responseDTO = new StatisticsTransactionDetailResponse();
        UniversityInfo universityInfo = universityInfoRepository.findUniversityInfoById(universityTransaction.getCreateBy());
        AdsPackage adsPackage = packageRepository.findPackageById(universityTransaction.getPackageId());
        responseDTO.setCreateBy(universityInfo.getName());
        responseDTO.setPrice(adsPackage.getPrice());
        return responseDTO;
    }

    private StatisticsPostResponse calculatorPostResponseByAdmin() {
        Integer totalPost = postRepository.totalPost();
        Integer currentPost = postRepository.currentPost();
        return StatisticsPostResponse.builder()
                .totalPost(totalPost)
                .currentPost(currentPost)
                .build();
    }

    private StatisticsAccountResponse calculatorAccountResponseByAdmin() {
        Integer totalAccount = userRepository.totalAccount();
        Integer currentAccount = userRepository.currentAccount();
        return StatisticsAccountResponse.builder()
                .totalAccount(totalAccount)
                .currentAccount(currentAccount)
                .build();
    }

    private StatisticsInteractResponse calculatorInteractResponseByAdmin() {
        Integer totalInteraction = userFavoriteRepository.totalInteraction();
        Integer currentInteraction = userFavoriteRepository.currentInteraction();
        return StatisticsInteractResponse.builder()
                .totalInteraction(totalInteraction)
                .currentInteraction(currentInteraction)
                .build();
    }

    private StatisticsTransactionResponse calculatorTransactionByAdmin() {
        Integer totalTransaction = universityTransactionRepository.calculatorTotalTransaction();
        Integer currentTransaction = universityTransactionRepository.calculatorCurrentTransaction();
        return StatisticsTransactionResponse.builder()
                .totalTransaction(totalTransaction)
                .currentTransaction(currentTransaction)
                .build();
    }
}
