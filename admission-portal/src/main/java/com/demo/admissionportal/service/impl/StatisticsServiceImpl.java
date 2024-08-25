package com.demo.admissionportal.service.impl;

import com.demo.admissionportal.constants.ResponseCode;
import com.demo.admissionportal.constants.Role;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.dto.response.holland_test.StatisticsPostResponse;
import com.demo.admissionportal.dto.response.statistics.*;
import com.demo.admissionportal.entity.*;
import com.demo.admissionportal.repository.*;
import com.demo.admissionportal.repository.sub_repository.UserFavoriteRepository;
import com.demo.admissionportal.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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
    private final ConsultantInfoRepository consultantInfoRepository;
    private final UserLikeRepository userLikeRepository;
    private final CommentRepository commentRepository;

    @Override
    public ResponseData<?> getStatistics() {
        try {
            Role user = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getRole();
            switch (user) {
                case ADMIN -> calculatorStatisticsByAdmin();
                case UNIVERSITY ->
                        calculatorStatisticsByUniversity(((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId());
                default -> throw new Exception();
            }
            return new ResponseData<>(ResponseCode.C200.getCode(), "Lấy dữ liệu thống kê thành công", user == Role.ADMIN ?
                    calculatorStatisticsByAdmin() :
                    calculatorStatisticsByUniversity(((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId()));
        } catch (Exception e) {
            log.error("Failed when get statistics: {} ", e.getMessage());
            return new ResponseData<>(ResponseCode.C207.getCode(), "Lấy dữ liệu thống kê thất bại");
        }
    }

    @Override
    public ResponseData<?> getStatisticsV2(String period) {
        try {

            if (period == null || period.isEmpty()) {
                period = "12 tháng";
            }

            if (!isValidPeriod(period)) {
                return new ResponseData<>(ResponseCode.C205.getCode(), "Period không hợp lệ: " + period);
            }

            Role user = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getRole();
            switch (user) {
                case ADMIN -> calculatorStatisticsByAdminV2(period);
//                case UNIVERSITY ->
//                        calculatorStatisticsByUniversityV2(((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId());
                default -> throw new Exception();
            }
            return new ResponseData<>(ResponseCode.C200.getCode(), "Lấy dữ liệu thống kê thành công", user == Role.ADMIN ?
                    calculatorStatisticsByAdminV2(period) : null);
//                    :
//                    calculatorStatisticsByUniversity(((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId()));
        } catch (Exception e) {
            log.error("Failed when get statistics: {} ", e.getMessage());
            return new ResponseData<>(ResponseCode.C207.getCode(), "Lấy dữ liệu thống kê thất bại");
        }
    }

    private boolean isValidPeriod(String period) {
        List<String> validPeriods = Arrays.asList("12 tháng", "6 tháng", "30 ngày", "7 ngày");
        return validPeriods.contains(period);
    }

    private StatisticsUniversityResponse calculatorStatisticsByUniversity(Integer universityId) {
        return StatisticsUniversityResponse.builder()
                .totalLike(calculatorLikeResponseByUniversity(universityId))
                .totalFavorite(calculatorFavoriteResponseByUniversity(universityId))
                .totalComment(calculatorCommentResponseByUniversity(universityId))
                .totalPost(calculatorTotalPost(universityId))
                .transactionDetail(mapToUniversityTransactionDetail(universityId))
                .build();
    }

    private Integer calculatorTotalPost(Integer universityId) {
        return postRepository.totalPostWithUniId(universityId).orElse(0);
    }

    private List<StatisticsTransactionDetailResponse> mapToUniversityTransactionDetail(Integer universityId) {
        List<UniversityTransaction> universityTransactionList = universityTransactionRepository.findUniversityTransactionByUniversityId(universityId);
        UniversityInfo universityInfo = universityInfoRepository.findUniversityInfoById(universityId);
        return universityTransactionList.stream()
                .map(transaction -> {
                    AdsPackage adsPackage = packageRepository.findPackageById(transaction.getPackageId());
                    return StatisticsTransactionDetailResponse.builder()
                            .createBy(universityInfo != null ? universityInfo.getName() : null)
                            .price(adsPackage != null ? adsPackage.getPrice() : 0)
                            .build();
                })
                .collect(Collectors.toList());
    }

    private Integer calculatorCommentResponseByUniversity(Integer universityId) {
        return commentRepository.totalComment(universityId).orElse(0);
    }

    private Integer calculatorFavoriteResponseByUniversity(Integer universityId) {
        return userFavoriteRepository.totalFavoriteCount(universityId).orElse(0);
    }

    private Integer calculatorLikeResponseByUniversity(Integer universityId) {
        return userLikeRepository.totalLikePost(universityId).orElse(0);
    }

    private StatisticsAdminResponse calculatorStatisticsByAdmin() {
        return StatisticsAdminResponse.builder()
                .transaction(calculatorTransactionByAdmin())
                .interact(calculatorInteractResponseByAdmin())
                .account(calculatorAccountResponseByAdmin())
                .post(calculatorPostResponseByAdmin())
                .activityTransaction(calculatorActivityTransaction())
                .build();
    }

    private StatisticsAdminResponseV2 calculatorStatisticsByAdminV2(String period) {
        return StatisticsAdminResponseV2.builder()
                .getRevenueStatistics(getRevenueStatistics(period))
                .getInteractionStatistics(getInteractionStatistics(period))
                .build();
    }

    @Async
    protected List<StatisticsTransactionDetailResponse> calculatorActivityTransaction() {
        List<UniversityTransaction> list = universityTransactionRepository.findAll();
        return list.parallelStream()
                .map(this::mapToTransactionDetail)
                .collect(Collectors.toList());
    }

    private StatisticsTransactionDetailResponse mapToTransactionDetail(UniversityTransaction universityTransaction) {
        StatisticsTransactionDetailResponse responseDTO = new StatisticsTransactionDetailResponse();
        UniversityInfo universityInfo = universityInfoRepository.findUniversityInfoById(universityTransaction.getCreateBy());
        if (universityInfo != null) {
            responseDTO.setCreateBy(universityInfo.getName());
        } else {
            ConsultantInfo consultantInfo = consultantInfoRepository.findConsultantInfoById(universityTransaction.getCreateBy());
            if (consultantInfo != null) {
                UniversityInfo universityInfoByConsultant = universityInfoRepository.findUniversityInfoById(consultantInfo.getUniversityId());
                if (universityInfoByConsultant != null) {
                    responseDTO.setCreateBy(universityInfoByConsultant.getName());
                }
            }
        }
        responseDTO.setPrice(
                packageRepository.findById(universityTransaction.getPackageId())
                        .map(AdsPackage::getPrice)
                        .orElse(null)
        );
        return responseDTO;
    }


    @Async
    protected StatisticsPostResponse calculatorPostResponseByAdmin() {
        Integer totalPost = postRepository.totalPost().orElse(0);
        Integer currentPost = postRepository.currentPost().orElse(0);
        Integer activePost = postRepository.activePost().orElse(0);
        Integer inactivePost = postRepository.inactivePost().orElse(0);
        return StatisticsPostResponse.builder()
                .totalPost(totalPost)
                .currentPost(currentPost)
                .activePost(activePost)
                .inactivePost(inactivePost)
                .build();
    }

    @Async
    protected StatisticsAccountResponse calculatorAccountResponseByAdmin() {
        Integer totalAccount = userRepository.totalAccount().orElse(0);
        Integer currentAccount = userRepository.currentAccount().orElse(0);
        Integer accountActive = userRepository.accountActive().orElse(0);
        Integer accountInactive = userRepository.accountInactive().orElse(0);
        return StatisticsAccountResponse.builder()
                .totalAccount(totalAccount)
                .accountActive(accountActive)
                .accountInactive(accountInactive)
                .currentAccount(currentAccount)
                .build();
    }

    @Async
    protected StatisticsInteractResponse calculatorInteractResponseByAdmin() {
        Integer totalInteraction = userFavoriteRepository.totalInteraction().orElse(0);
        Integer currentInteraction = userFavoriteRepository.currentInteraction().orElse(0);
        return StatisticsInteractResponse.builder()
                .totalInteraction(totalInteraction)
                .currentInteraction(currentInteraction)
                .build();
    }

    @Async
    protected StatisticsTransactionResponse calculatorTransactionByAdmin() {
        Integer totalTransaction = universityTransactionRepository.calculatorTotalTransaction().orElse(0);
        Integer currentTransaction = universityTransactionRepository.calculatorCurrentTransaction().orElse(0);
        return StatisticsTransactionResponse.builder()
                .totalTransaction(totalTransaction)
                .currentTransaction(currentTransaction)
                .build();
    }

    public List<StatisticsAdminResponseV3> getStatistics(String period, String type) {
        List<StatisticsAdminResponseV3> statistics = new ArrayList<>();

        if (period == null || period.isEmpty()) {
            period = "12 tháng";
        }

        switch (type.toLowerCase()) {
            case "interaction":
                List<Object[]> likeData;
                List<Object[]> favoriteData;

                switch (period) {
                    case "12 tháng":
                        likeData = universityTransactionRepository.findLikeAndUnlikeCountByLast12MonthsForAllUniversities();
                        favoriteData = universityTransactionRepository.findFollowAndUnfollowCountByLast12MonthsForAllUniversities();
                        break;
                    case "6 tháng":
                        likeData = universityTransactionRepository.findLikeAndUnlikeCountByLast6MonthsForAllUniversities();
                        favoriteData = universityTransactionRepository.findFollowAndUnfollowCountByLast6MonthsForAllUniversities();
                        break;
                    case "30 ngày":
                        likeData = universityTransactionRepository.findLikeAndUnlikeCountByLast30DaysForAllUniversities();
                        favoriteData = universityTransactionRepository.findFollowAndUnfollowCountByLast30DaysForAllUniversities();
                        break;
                    case "7 ngày":
                        likeData = universityTransactionRepository.findLikeAndUnlikeCountByLast7DaysForAllUniversities();
                        favoriteData = universityTransactionRepository.findFollowAndUnfollowCountByLast7DaysForAllUniversities();
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid period: " + period);
                }

                for (Object[] record : likeData) {
                    Date date = (Date) record[0];
                    Integer likeCount = ((Integer) record[1]).intValue();
                    Integer unlikeCount = ((Integer) record[2]).intValue();
                    statistics.add(new StatisticInteractionByTime(date, likeCount - unlikeCount, "LIKE"));
                }

                for (Object[] record : favoriteData) {
                    Date date = (Date) record[0];
                    Integer followCount = ((Integer) record[1]).intValue();
                    Integer unfollowCount = ((Integer) record[2]).intValue();
                    statistics.add(new StatisticInteractionByTime(date, followCount - unfollowCount, "FOLLOW"));
                }
                break;

            case "revenue":
                List<Object[]> revenueData;

                switch (period) {
                    case "12 tháng":
                        revenueData = universityTransactionRepository.findRevenueByLast12MonthsForAllUniversities();
                        break;
                    case "6 tháng":
                        revenueData = universityTransactionRepository.findRevenueByLast6MonthsForAllUniversities();
                        break;
                    case "30 ngày":
                        revenueData = universityTransactionRepository.findRevenueByLast30DaysForAllUniversities();
                        break;
                    case "7 ngày":
                        revenueData = universityTransactionRepository.findRevenueByLast7DaysForAllUniversities();
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid period: " + period);
                }

                for (Object[] record : revenueData) {
                    Date date = (Date) record[0];
                    Integer revenue = ((BigDecimal) record[1]).intValue();
                    statistics.add(new StatisticRevenueByTime(date, revenue));
                }
                break;

            default:
                throw new IllegalArgumentException("Invalid filter type: " + filterType);
        }

        return statistics;
    }
}
